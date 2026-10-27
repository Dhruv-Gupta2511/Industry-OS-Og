USE industrial_mgmt;

-- Q1. Headcount and average base salary per department
SELECT d.dept_name, COUNT(e.emp_id) AS headcount, ROUND(AVG(g.base_salary), 0) AS avg_base_salary
FROM department d
LEFT JOIN employee e    ON e.dept_id = d.dept_id AND e.status <> 'Resigned'
LEFT JOIN designation g ON g.desig_id = e.desig_id
GROUP BY d.dept_id, d.dept_name
ORDER BY headcount DESC;

-- Q2. Managers and the size of their teams (self join)
SELECT CONCAT(m.first_name, ' ', m.last_name) AS manager, d.dept_name, COUNT(e.emp_id) AS direct_reports
FROM employee m
JOIN employee e   ON e.manager_id = m.emp_id
JOIN department d ON d.dept_id = m.dept_id
GROUP BY m.emp_id, m.first_name, m.last_name, d.dept_name
ORDER BY direct_reports DESC, manager;

-- Q3. Top 5 net pay for September 2026
SELECT CONCAT(e.first_name, ' ', e.last_name) AS employee, d.dept_name, p.net_pay
FROM payroll p
JOIN employee e   ON e.emp_id = p.emp_id
JOIN department d ON d.dept_id = e.dept_id
WHERE p.pay_month = '2026-09'
ORDER BY p.net_pay DESC
LIMIT 5;

-- Q4. Employees with 2 or more absences in the attendance window
SELECT full_name, dept_name, present_days, absent_days, leave_days
FROM v_attendance_summary
WHERE absent_days >= 2
ORDER BY absent_days DESC, full_name;

-- Q5. Mandatory-training gaps (anti-join): who still has to complete which course
SELECT e.emp_id, CONCAT(e.first_name, ' ', e.last_name) AS employee, tp.title AS pending_training
FROM employee e
CROSS JOIN training_program tp
WHERE tp.mandatory = TRUE
  AND e.status <> 'Resigned'
  AND NOT EXISTS (SELECT 1 FROM employee_training et
                  WHERE et.emp_id = e.emp_id AND et.training_id = tp.training_id)
ORDER BY e.emp_id, tp.title
LIMIT 25;

-- Q6. Highest-paid employee in every department (window function)
WITH ranked AS (
    SELECT d.dept_name, CONCAT(e.first_name, ' ', e.last_name) AS employee, p.net_pay,
           RANK() OVER (PARTITION BY d.dept_id ORDER BY p.net_pay DESC) AS rk
    FROM payroll p
    JOIN employee e   ON e.emp_id = p.emp_id
    JOIN department d ON d.dept_id = e.dept_id
    WHERE p.pay_month = '2026-09'
)
SELECT dept_name, employee, net_pay FROM ranked WHERE rk = 1 ORDER BY dept_name;

-- Q7. Monthly payroll cost by department
SELECT d.dept_name, p.pay_month, COUNT(*) AS staff, SUM(p.net_pay) AS net_cost
FROM payroll p
JOIN employee e   ON e.emp_id = p.emp_id
JOIN department d ON d.dept_id = e.dept_id
GROUP BY d.dept_name, p.pay_month
ORDER BY p.pay_month, net_cost DESC;

-- Q8. Machines ranked by total maintenance cost (labour + spares)
SELECT machine_name, status, jobs, downtime_hours, labour_cost, parts_cost,
       labour_cost + parts_cost AS total_cost
FROM v_machine_maintenance_cost
ORDER BY total_cost DESC
LIMIT 10;

-- Q9. Downtime hours by plant and production line
SELECT pl.plant_name, l.line_name, ROUND(SUM(ml.downtime_hours), 1) AS downtime_hours, COUNT(*) AS jobs
FROM maintenance_log ml
JOIN machine mc         ON mc.machine_id = ml.machine_id
JOIN production_line l  ON l.line_id = mc.line_id
JOIN plant pl           ON pl.plant_id = l.plant_id
GROUP BY pl.plant_name, l.line_name
ORDER BY downtime_hours DESC;

-- Q10. Spare parts at or below reorder level
SELECT part_name, stock_qty, reorder_level, reorder_level - stock_qty AS to_order
FROM spare_part
WHERE stock_qty <= reorder_level
ORDER BY to_order DESC;

-- Q11. Safety incidents per plant, pivoted by severity
SELECT p.plant_name,
       SUM(CASE WHEN s.severity = 'Minor'    THEN 1 ELSE 0 END) AS minor,
       SUM(CASE WHEN s.severity = 'Moderate' THEN 1 ELSE 0 END) AS moderate,
       SUM(CASE WHEN s.severity = 'Major'    THEN 1 ELSE 0 END) AS major,
       SUM(CASE WHEN s.severity = 'Critical' THEN 1 ELSE 0 END) AS critical,
       COUNT(s.incident_id) AS total
FROM plant p
LEFT JOIN safety_incident s ON s.plant_id = p.plant_id
GROUP BY p.plant_id, p.plant_name;

-- Q12. Running energy cost for the Pune plant (cumulative window)
SELECT record_date, units_kwh, cost,
       SUM(cost) OVER (PARTITION BY plant_id ORDER BY record_date) AS running_cost
FROM energy_consumption
WHERE plant_id = 1
ORDER BY record_date;

-- Q13. Contracts expiring in the next 90 days, and contracts already expired
SELECT company_name, service_type, contract_end,
       CASE WHEN contract_end < CURDATE() THEN 'EXPIRED' ELSE 'EXPIRING SOON' END AS state
FROM contractor
WHERE contract_end < CURDATE()
   OR contract_end BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 90 DAY)
ORDER BY contract_end;

-- Q14. Raw materials below reorder level, with the cheapest supplier to order from
SELECT v.material_name, v.unit, v.total_stock, v.reorder_level, v.shortfall,
       s.supplier_name, sm.quoted_price, sm.lead_time_days
FROM v_low_stock_materials v
JOIN supplier_material sm ON sm.material_id = v.material_id
JOIN supplier s           ON s.supplier_id = sm.supplier_id
WHERE sm.quoted_price = (SELECT MIN(x.quoted_price) FROM supplier_material x WHERE x.material_id = v.material_id)
ORDER BY v.shortfall DESC;

-- Q15. Supplier spend and rating (cancelled POs excluded)
SELECT s.supplier_name, s.rating,
       COUNT(DISTINCT po.po_id)        AS orders,
       COALESCE(SUM(poi.line_total), 0) AS spend
FROM supplier s
LEFT JOIN purchase_order po       ON po.supplier_id = s.supplier_id AND po.status <> 'Cancelled'
LEFT JOIN purchase_order_item poi ON poi.po_id = po.po_id
GROUP BY s.supplier_id, s.supplier_name, s.rating
ORDER BY spend DESC;

-- Q16. Stock value per warehouse
SELECT w.warehouse_name, pl.plant_name, COUNT(i.inv_id) AS materials,
       ROUND(SUM(i.quantity * rm.unit_cost), 2) AS stock_value
FROM warehouse w
JOIN plant pl       ON pl.plant_id = w.plant_id
JOIN inventory i    ON i.warehouse_id = w.warehouse_id
JOIN raw_material rm ON rm.material_id = i.material_id
GROUP BY w.warehouse_id, w.warehouse_name, pl.plant_name
ORDER BY stock_value DESC;

-- Q17. Material shortage across all Planned / In-Progress work orders (CTEs)
WITH need AS (
    SELECT b.material_id, SUM(b.qty_required * w.planned_qty) AS required
    FROM work_order w
    JOIN bill_of_materials b ON b.product_id = w.product_id
    WHERE w.status IN ('Planned', 'In Progress')
    GROUP BY b.material_id
), have AS (
    SELECT material_id, SUM(quantity) AS available FROM inventory GROUP BY material_id
)
SELECT rm.material_name, ROUND(n.required, 1) AS required, COALESCE(h.available, 0) AS available,
       ROUND(n.required - COALESCE(h.available, 0), 1) AS gap
FROM need n
JOIN raw_material rm ON rm.material_id = n.material_id
LEFT JOIN have h     ON h.material_id = n.material_id
WHERE n.required > COALESCE(h.available, 0)
ORDER BY gap DESC;

-- Q18. Work-order progress for open orders
SELECT wo_id, product_name, plant_name, line_name, planned_qty, good_qty, rejected_qty, pct_complete
FROM v_work_order_progress
WHERE status IN ('In Progress', 'Planned')
ORDER BY wo_id;

-- Q19. Material cost per unit (from the BOM) against the standard cost of each product
SELECT p.product_name, p.standard_cost,
       ROUND(SUM(b.qty_required * rm.unit_cost), 2) AS material_cost,
       ROUND(100.0 * SUM(b.qty_required * rm.unit_cost) / p.standard_cost, 1) AS material_pct_of_cost
FROM product p
JOIN bill_of_materials b ON b.product_id = p.product_id
JOIN raw_material rm     ON rm.material_id = b.material_id
GROUP BY p.product_id, p.product_name, p.standard_cost
ORDER BY material_pct_of_cost DESC;

-- Q20. Rejection rate by production line
SELECT pl.plant_name, l.line_name,
       SUM(b.produced_qty) AS produced, SUM(b.rejected_qty) AS rejected,
       ROUND(100.0 * SUM(b.rejected_qty) / SUM(b.produced_qty), 2) AS reject_pct
FROM production_batch b
JOIN work_order w      ON w.wo_id = b.wo_id
JOIN production_line l ON l.line_id = w.line_id
JOIN plant pl          ON pl.plant_id = l.plant_id
GROUP BY pl.plant_name, l.line_name
ORDER BY reject_pct DESC;

-- Q21. Defect Pareto: which defect types cause most problems (cumulative %)
WITH d AS (
    SELECT dt.defect_name, SUM(dl.qty) AS qty
    FROM defect_log dl
    JOIN defect_type dt ON dt.defect_type_id = dl.defect_type_id
    GROUP BY dt.defect_name
)
SELECT defect_name, qty,
       ROUND(100.0 * qty / SUM(qty) OVER (), 1) AS pct,
       ROUND(100.0 * SUM(qty) OVER (ORDER BY qty DESC) / SUM(qty) OVER (), 1) AS cumulative_pct
FROM d
ORDER BY qty DESC;

-- Q22. Inspector performance
SELECT CONCAT(e.first_name, ' ', e.last_name) AS inspector, COUNT(*) AS inspections,
       SUM(CASE WHEN qi.result = 'Pass' THEN 1 ELSE 0 END) AS passed,
       ROUND(100.0 * SUM(CASE WHEN qi.result = 'Pass' THEN 1 ELSE 0 END) / COUNT(*), 1) AS pass_pct
FROM quality_inspection qi
JOIN employee e ON e.emp_id = qi.inspector_id
GROUP BY e.emp_id, e.first_name, e.last_name
ORDER BY inspections DESC;

-- Q23. Quality pass-rate per product (view)
SELECT product_name, inspections, passed, rework, failed, pass_rate_pct
FROM v_quality_by_product
WHERE inspections > 0
ORDER BY pass_rate_pct;

-- Q24. Monthly sales trend (cancelled orders excluded)
SELECT DATE_FORMAT(order_date, '%Y-%m') AS month, COUNT(*) AS orders, SUM(total_amount) AS revenue
FROM sales_order
WHERE status <> 'Cancelled'
GROUP BY DATE_FORMAT(order_date, '%Y-%m')
ORDER BY month;

-- Q25. Customer ranking by revenue (RANK) with share of total
SELECT RANK() OVER (ORDER BY total_sales DESC) AS rnk, customer_name, city, orders, total_sales,
       ROUND(100.0 * total_sales / SUM(total_sales) OVER (), 1) AS share_pct
FROM v_customer_sales
WHERE total_sales > 0
ORDER BY rnk;

-- Q26. Gross profit by product (shipped / delivered orders only)
SELECT p.product_name, SUM(i.quantity) AS units_sold, SUM(i.line_total) AS revenue,
       SUM(i.quantity * p.standard_cost) AS cogs,
       SUM(i.line_total - i.quantity * p.standard_cost) AS gross_profit
FROM sales_order_item i
JOIN sales_order so ON so.so_id = i.so_id
JOIN product p      ON p.product_id = i.product_id
WHERE so.status IN ('Shipped', 'Delivered')
GROUP BY p.product_id, p.product_name
ORDER BY gross_profit DESC;

-- Q27. Late deliveries and how late they were
SELECT s.shipment_id, c.customer_name, so.delivery_date AS promised, s.delivered_on,
       DATEDIFF(s.delivered_on, so.delivery_date) AS days_late
FROM shipment s
JOIN sales_order so ON so.so_id = s.so_id
JOIN customer c     ON c.customer_id = so.customer_id
WHERE s.status = 'Delayed'
ORDER BY days_late DESC;

-- Q28. Receivables: unpaid balances that are past their due date
SELECT invoice_id, customer_name, due_date, invoice_total, paid, balance,
       DATEDIFF(CURDATE(), due_date) AS days_overdue
FROM v_invoice_balance
WHERE balance > 0 AND due_date < CURDATE()
ORDER BY days_overdue DESC;

-- Q29. Credit exposure: outstanding balance as % of each customer's credit limit
SELECT cs.customer_name, cs.credit_limit, ROUND(SUM(b.balance), 2) AS outstanding,
       ROUND(100.0 * SUM(b.balance) / cs.credit_limit, 1) AS limit_used_pct
FROM v_customer_sales cs
JOIN sales_order so       ON so.customer_id = cs.customer_id
JOIN v_invoice_balance b  ON b.so_id = so.so_id
GROUP BY cs.customer_id, cs.customer_name, cs.credit_limit
HAVING SUM(b.balance) > 0
ORDER BY limit_used_pct DESC;

-- Q30. Everyone we are waiting on or waiting for (UNION ALL)
SELECT 'Supplier (open PO)' AS party_type, s.supplier_name AS party, COUNT(po.po_id) AS open_items
FROM supplier s
JOIN purchase_order po ON po.supplier_id = s.supplier_id
WHERE po.status = 'Placed'
GROUP BY s.supplier_id, s.supplier_name
UNION ALL
SELECT 'Customer (unpaid invoice)', customer_name, COUNT(*)
FROM v_invoice_balance
WHERE balance > 0
GROUP BY customer_name
ORDER BY party_type, open_items DESC;

SELECT * FROM v_plant_dashboard;
SELECT * FROM v_product_margin ORDER BY margin_pct DESC;

--  TRIGGER / PROCEDURE DEMOS (MySQL only - run after file 04)
-- Auto-total: add a line item, then see sales_order.total_amount change
--   INSERT INTO sales_order (customer_id, order_date, delivery_date, status) VALUES (1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'Placed');
--   INSERT INTO sales_order_item (so_id, product_id, quantity, unit_price) VALUES (LAST_INSERT_ID(), 5, 10, 3200);
--   SELECT so_id, total_amount FROM sales_order ORDER BY so_id DESC LIMIT 1;
-- Over-payment guard (should fail with 'Payment exceeds outstanding invoice balance'):
--   INSERT INTO payment (invoice_id, payment_date, amount, mode) VALUES (1, CURDATE(), 99999999, 'Cash');
-- Ship an order, invoice it, then pay it:
--   CALL sp_ship_order(<placed_so_id>, 1);
--   CALL sp_create_invoice(<placed_so_id>);
--   SELECT * FROM v_invoice_balance ORDER BY invoice_id DESC LIMIT 1;
-- Receive a purchase order into warehouse 1:
--   CALL sp_receive_purchase_order(<placed_po_id>, 1);
-- Run October payroll:
--   CALL sp_run_payroll('2026-10');

-- =============================================================================
-- SECTION 6 : USER LIFECYCLE & TRANSACTIONAL DELETIONS (CRUD & AUDITING)
-- =============================================================================

-- 26. Register new user and verify relational foreign key linkage
-- A. Register customer
INSERT INTO customer (customer_name, contact_person, email, phone, city, state, credit_limit, gstin)
VALUES ('Omkar Precision Tooling Ltd', 'Omkar Patil', 'omkar@precision.in', '+91 9811224455', 'Pune', 'Maharashtra', 2000000.00, '27AABB1234D1Z2');

INSERT INTO user_account (username, password, role, full_name, email, department_id, customer_id, enabled)
VALUES ('omkar_cust', 'Password@123', 'CUSTOMER', 'Omkar Patil', 'omkar@precision.in', 1, LAST_INSERT_ID(), TRUE);

-- B. Verify joined user profile
SELECT u.id, u.username, u.role, u.full_name, c.customer_name, c.city, c.credit_limit
FROM user_account u
JOIN customer c ON c.customer_id = u.customer_id
WHERE u.username = 'omkar_cust';

-- 27. Safely delete user account and verify audit trail
DELETE FROM user_account WHERE username = 'omkar_cust';

INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value)
VALUES ('AUD-DEL-USR-01', 'DELETE', 'USER_ACCOUNT', 'admin', 'Deleted user account omkar_cust', 'User ID exists', 'DELETED');

SELECT * FROM audit_log WHERE entity_name = 'USER_ACCOUNT' ORDER BY id DESC LIMIT 5;

-- 28. Safely delete a sales order and verify clean-up of associated items
-- Find an order to delete:
-- SELECT so_id, customer_id, total_amount, status FROM sales_order ORDER BY so_id DESC LIMIT 1;
-- Perform deletion (or call sp_delete_sales_order):
DELETE FROM sales_order WHERE so_id = 26;

INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value)
VALUES ('AUD-DEL-SO-01', 'DELETE', 'SALES_ORDER', 'ceo', 'Deleted Sales Order #26 and associated line items', 'Order Total: ₹3210000.00', 'DELETED');

SELECT count(*) AS remaining_order_items FROM sales_order_item WHERE so_id = 26;

-- 29. Full Audit Log Inspection: Review all INSERT, UPDATE, DELETE actions
SELECT action_type, entity_name, username, payload_summary, previous_value, new_value, created_at
FROM audit_log
ORDER BY id DESC
LIMIT 20;
