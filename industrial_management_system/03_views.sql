USE industrial_mgmt;

-- Keep machine.last_service in sync with the maintenance history loaded in 02_data.sql
UPDATE machine AS m
SET last_service = (SELECT MAX(ml.maint_date) FROM maintenance_log ml WHERE ml.machine_id = m.machine_id);

-- 1. Employee directory with all lookups resolved
CREATE OR REPLACE VIEW v_employee_details AS
SELECT e.emp_id,
       CONCAT(e.first_name, ' ', e.last_name) AS full_name,
       d.dept_name, g.title AS designation, g.grade,
       p.plant_name, s.shift_name, e.hire_date, e.status,
       CONCAT(m.first_name, ' ', m.last_name) AS manager_name
FROM employee e
JOIN department  d ON d.dept_id  = e.dept_id
JOIN designation g ON g.desig_id = e.desig_id
JOIN plant       p ON p.plant_id = e.plant_id
JOIN shift       s ON s.shift_id = e.shift_id
LEFT JOIN employee m ON m.emp_id = e.manager_id;

-- 2. Raw materials whose total stock (all warehouses) is below reorder level
CREATE OR REPLACE VIEW v_low_stock_materials AS
SELECT rm.material_id, rm.material_name, rm.unit, rm.reorder_level,
       COALESCE(SUM(i.quantity), 0) AS total_stock,
       rm.reorder_level - COALESCE(SUM(i.quantity), 0) AS shortfall
FROM raw_material rm
LEFT JOIN inventory i ON i.material_id = rm.material_id
GROUP BY rm.material_id, rm.material_name, rm.unit, rm.reorder_level
HAVING COALESCE(SUM(i.quantity), 0) < rm.reorder_level;

-- 3. Work-order progress: planned vs. good output vs. rejects
CREATE OR REPLACE VIEW v_work_order_progress AS
SELECT w.wo_id, pr.product_name, pl.plant_name, l.line_name, w.status,
       w.planned_qty,
       COALESCE(SUM(b.produced_qty - b.rejected_qty), 0) AS good_qty,
       COALESCE(SUM(b.rejected_qty), 0)                  AS rejected_qty,
       ROUND(100.0 * COALESCE(SUM(b.produced_qty - b.rejected_qty), 0) / w.planned_qty, 1) AS pct_complete
FROM work_order w
JOIN product         pr ON pr.product_id = w.product_id
JOIN production_line l  ON l.line_id     = w.line_id
JOIN plant           pl ON pl.plant_id   = l.plant_id
LEFT JOIN production_batch b ON b.wo_id  = w.wo_id
GROUP BY w.wo_id, pr.product_name, pl.plant_name, l.line_name, w.status, w.planned_qty;

-- 4. Machine maintenance burden (labour + spare parts + downtime)
CREATE OR REPLACE VIEW v_machine_maintenance_cost AS
SELECT mc.machine_id, mc.machine_name, mc.status,
       COUNT(ml.maint_id)                       AS jobs,
       COALESCE(SUM(ml.downtime_hours), 0)      AS downtime_hours,
       COALESCE(SUM(ml.labour_cost), 0)         AS labour_cost,
       COALESCE(SUM((SELECT SUM(u.qty_used * sp.unit_cost)
                     FROM maintenance_part_usage u
                     JOIN spare_part sp ON sp.part_id = u.part_id
                     WHERE u.maint_id = ml.maint_id)), 0) AS parts_cost
FROM machine mc
LEFT JOIN maintenance_log ml ON ml.machine_id = mc.machine_id
GROUP BY mc.machine_id, mc.machine_name, mc.status;

-- 5. Customer sales summary (cancelled orders excluded)
CREATE OR REPLACE VIEW v_customer_sales AS
SELECT c.customer_id, c.customer_name, c.city, c.state, c.credit_limit,
       COUNT(so.so_id)                      AS orders,
       COALESCE(SUM(so.total_amount), 0)    AS total_sales
FROM customer c
LEFT JOIN sales_order so ON so.customer_id = c.customer_id AND so.status <> 'Cancelled'
GROUP BY c.customer_id, c.customer_name, c.city, c.state, c.credit_limit;

-- 6. Invoice balances (receivables)
CREATE OR REPLACE VIEW v_invoice_balance AS
SELECT i.invoice_id, i.so_id, c.customer_name, i.invoice_date, i.due_date, i.status,
       i.amount + i.tax_amount                              AS invoice_total,
       COALESCE(SUM(p.amount), 0)                           AS paid,
       i.amount + i.tax_amount - COALESCE(SUM(p.amount), 0) AS balance
FROM invoice i
JOIN sales_order so ON so.so_id = i.so_id
JOIN customer    c  ON c.customer_id = so.customer_id
LEFT JOIN payment p ON p.invoice_id = i.invoice_id
GROUP BY i.invoice_id, i.so_id, c.customer_name, i.invoice_date, i.due_date, i.status, i.amount, i.tax_amount;

-- 7. Plant dashboard
CREATE OR REPLACE VIEW v_plant_dashboard AS
SELECT p.plant_id, p.plant_name, p.city,
       (SELECT COUNT(*) FROM employee e WHERE e.plant_id = p.plant_id AND e.status <> 'Resigned') AS employees,
       (SELECT COUNT(*) FROM production_line l WHERE l.plant_id = p.plant_id)                       AS prod_lines,
       (SELECT COUNT(*) FROM machine m JOIN production_line l ON l.line_id = m.line_id
         WHERE l.plant_id = p.plant_id)                                                             AS machines,
       (SELECT COALESCE(SUM(ec.units_kwh), 0) FROM energy_consumption ec WHERE ec.plant_id = p.plant_id) AS energy_kwh,
       (SELECT COALESCE(SUM(ec.cost), 0) FROM energy_consumption ec WHERE ec.plant_id = p.plant_id)      AS energy_cost,
       (SELECT COUNT(*) FROM safety_incident s WHERE s.plant_id = p.plant_id)                       AS safety_incidents
FROM plant p;

-- 8. Quality summary per product
CREATE OR REPLACE VIEW v_quality_by_product AS
SELECT pr.product_id, pr.product_name,
       COUNT(qi.insp_id)                                        AS inspections,
       SUM(CASE WHEN qi.result = 'Pass'   THEN 1 ELSE 0 END)    AS passed,
       SUM(CASE WHEN qi.result = 'Rework' THEN 1 ELSE 0 END)    AS rework,
       SUM(CASE WHEN qi.result = 'Fail'   THEN 1 ELSE 0 END)    AS failed,
       ROUND(100.0 * SUM(CASE WHEN qi.result = 'Pass' THEN 1 ELSE 0 END) / NULLIF(COUNT(qi.insp_id), 0), 1) AS pass_rate_pct
FROM product pr
LEFT JOIN work_order        w  ON w.product_id = pr.product_id
LEFT JOIN production_batch  b  ON b.wo_id      = w.wo_id
LEFT JOIN quality_inspection qi ON qi.batch_id = b.batch_id
GROUP BY pr.product_id, pr.product_name;

-- 9. Monthly attendance summary
CREATE OR REPLACE VIEW v_attendance_summary AS
SELECT e.emp_id, CONCAT(e.first_name, ' ', e.last_name) AS full_name, d.dept_name,
       SUM(CASE WHEN a.status = 'Present'  THEN 1 ELSE 0 END) AS present_days,
       SUM(CASE WHEN a.status = 'Half Day' THEN 1 ELSE 0 END) AS half_days,
       SUM(CASE WHEN a.status = 'Absent'   THEN 1 ELSE 0 END) AS absent_days,
       SUM(CASE WHEN a.status = 'Leave'    THEN 1 ELSE 0 END) AS leave_days
FROM employee e
JOIN department d ON d.dept_id = e.dept_id
LEFT JOIN attendance a ON a.emp_id = e.emp_id
GROUP BY e.emp_id, e.first_name, e.last_name, d.dept_name;

-- 10. Product profitability
CREATE OR REPLACE VIEW v_product_margin AS
SELECT p.product_id, p.product_name, pc.pcat_name, p.unit_price, p.standard_cost,
       p.unit_price - p.standard_cost                                  AS margin_per_unit,
       ROUND(100.0 * (p.unit_price - p.standard_cost) / p.unit_price, 2) AS margin_pct,
       p.stock_qty
FROM product p
JOIN product_category pc ON pc.pcat_id = p.pcat_id;
