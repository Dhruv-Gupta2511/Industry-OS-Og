USE industrial_mgmt;

DELIMITER $$

-- TRIGGERS

-- T1-T3: keep sales_order.total_amount equal to the sum of its line items
CREATE TRIGGER trg_soitem_ai AFTER INSERT ON sales_order_item FOR EACH ROW
BEGIN
    UPDATE sales_order
    SET total_amount = (SELECT COALESCE(SUM(line_total), 0) FROM sales_order_item WHERE so_id = NEW.so_id)
    WHERE so_id = NEW.so_id;
END$$

CREATE TRIGGER trg_soitem_au AFTER UPDATE ON sales_order_item FOR EACH ROW
BEGIN
    UPDATE sales_order
    SET total_amount = (SELECT COALESCE(SUM(line_total), 0) FROM sales_order_item WHERE so_id = NEW.so_id)
    WHERE so_id = NEW.so_id;
END$$

CREATE TRIGGER trg_soitem_ad AFTER DELETE ON sales_order_item FOR EACH ROW
BEGIN
    UPDATE sales_order
    SET total_amount = (SELECT COALESCE(SUM(line_total), 0) FROM sales_order_item WHERE so_id = OLD.so_id)
    WHERE so_id = OLD.so_id;
END$$

-- T4: finished-goods stock increases automatically when a production batch is logged
CREATE TRIGGER trg_batch_ai AFTER INSERT ON production_batch FOR EACH ROW
BEGIN
    UPDATE product
    SET stock_qty = stock_qty + (NEW.produced_qty - NEW.rejected_qty)
    WHERE product_id = (SELECT product_id FROM work_order WHERE wo_id = NEW.wo_id);
END$$

-- T5: quality result is derived from defects found (<=2 Pass, 3-4 Rework, >=5 Fail)
CREATE TRIGGER trg_inspection_bi BEFORE INSERT ON quality_inspection FOR EACH ROW
BEGIN
    IF NEW.defects_found > NEW.sample_size THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Defects found cannot exceed sample size';
    END IF;
    SET NEW.result = CASE WHEN NEW.defects_found <= 2 THEN 'Pass'
                          WHEN NEW.defects_found <= 4 THEN 'Rework'
                          ELSE 'Fail' END;
END$$

-- T6: reject payments that would exceed the invoice total
CREATE TRIGGER trg_payment_bi BEFORE INSERT ON payment FOR EACH ROW
BEGIN
    DECLARE v_total DECIMAL(14,2);
    DECLARE v_paid  DECIMAL(14,2);
    SELECT amount + tax_amount INTO v_total FROM invoice WHERE invoice_id = NEW.invoice_id;
    SELECT COALESCE(SUM(amount), 0) INTO v_paid FROM payment WHERE invoice_id = NEW.invoice_id;
    IF v_paid + NEW.amount > v_total THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Payment exceeds outstanding invoice balance';
    END IF;
END$$

-- T7: invoice status follows payments (Unpaid -> Partial -> Paid)
CREATE TRIGGER trg_payment_ai AFTER INSERT ON payment FOR EACH ROW
BEGIN
    UPDATE invoice
    SET status = CASE WHEN (SELECT SUM(amount) FROM payment WHERE invoice_id = NEW.invoice_id) >= amount + tax_amount
                      THEN 'Paid' ELSE 'Partial' END
    WHERE invoice_id = NEW.invoice_id;
END$$

-- T8: logging maintenance updates the machine; a breakdown takes it out of service
CREATE TRIGGER trg_maintenance_ai AFTER INSERT ON maintenance_log FOR EACH ROW
BEGIN
    UPDATE machine
    SET last_service = CASE WHEN last_service IS NULL OR last_service < NEW.maint_date THEN NEW.maint_date ELSE last_service END,
        status = CASE WHEN NEW.maint_type = 'Breakdown' AND status = 'Operational' THEN 'Under Maintenance' ELSE status END
    WHERE machine_id = NEW.machine_id;
END$$

-- T9-T10: spare parts are consumed when used in maintenance (and cannot go negative)
CREATE TRIGGER trg_partusage_bi BEFORE INSERT ON maintenance_part_usage FOR EACH ROW
BEGIN
    IF (SELECT stock_qty FROM spare_part WHERE part_id = NEW.part_id) < NEW.qty_used THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient spare part stock';
    END IF;
END$$

CREATE TRIGGER trg_partusage_ai AFTER INSERT ON maintenance_part_usage FOR EACH ROW
BEGIN
    UPDATE spare_part SET stock_qty = stock_qty - NEW.qty_used WHERE part_id = NEW.part_id;
END$$

-- T11-T12: audit trail
CREATE TRIGGER trg_employee_au AFTER UPDATE ON employee FOR EACH ROW
BEGIN
    IF OLD.status <> NEW.status OR OLD.dept_id <> NEW.dept_id OR OLD.desig_id <> NEW.desig_id THEN
        INSERT INTO audit_log (table_name, action, record_id, details)
        VALUES ('employee', 'UPDATE', NEW.emp_id,
                CONCAT('status ', OLD.status, '->', NEW.status, '; dept ', OLD.dept_id, '->', NEW.dept_id,
                       '; desig ', OLD.desig_id, '->', NEW.desig_id));
    END IF;
END$$

CREATE TRIGGER trg_product_au AFTER UPDATE ON product FOR EACH ROW
BEGIN
    IF OLD.unit_price <> NEW.unit_price THEN
        INSERT INTO audit_log (table_name, action, record_id, details)
        VALUES ('product', 'UPDATE', NEW.product_id,
                CONCAT('unit_price ', OLD.unit_price, '->', NEW.unit_price));
    END IF;
END$$

-- STORED FUNCTIONS

CREATE FUNCTION fn_product_margin_pct(p_product INT) RETURNS DECIMAL(6,2)
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_margin DECIMAL(6,2);
    SELECT ROUND(100 * (unit_price - standard_cost) / unit_price, 2) INTO v_margin
    FROM product WHERE product_id = p_product;
    RETURN v_margin;
END$$

CREATE FUNCTION fn_tenure_years(p_emp INT) RETURNS INT
READS SQL DATA
BEGIN
    DECLARE v_years INT;
    SELECT TIMESTAMPDIFF(YEAR, hire_date, CURDATE()) INTO v_years FROM employee WHERE emp_id = p_emp;
    RETURN v_years;
END$$

CREATE FUNCTION fn_customer_outstanding(p_customer INT) RETURNS DECIMAL(14,2)
READS SQL DATA
BEGIN
    DECLARE v_bal DECIMAL(14,2);
    SELECT COALESCE(SUM(b.balance), 0) INTO v_bal
    FROM v_invoice_balance b
    JOIN sales_order so ON so.so_id = b.so_id
    WHERE so.customer_id = p_customer;
    RETURN v_bal;
END$$

-- STORED PROCEDURES

-- P1: receive a purchase order into a warehouse (adds to inventory, closes PO)
CREATE PROCEDURE sp_receive_purchase_order(IN p_po INT, IN p_warehouse INT)
BEGIN
    DECLARE v_status VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    SELECT status INTO v_status FROM purchase_order WHERE po_id = p_po;
    IF v_status IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Purchase order not found';
    ELSEIF v_status <> 'Placed' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only POs in Placed status can be received';
    END IF;

    START TRANSACTION;
        INSERT INTO inventory (warehouse_id, material_id, quantity, last_updated)
        SELECT p_warehouse, material_id, quantity, CURDATE()
        FROM purchase_order_item WHERE po_id = p_po
        ON DUPLICATE KEY UPDATE quantity = inventory.quantity + VALUES(quantity),
                                last_updated = CURDATE();
        UPDATE purchase_order SET status = 'Received' WHERE po_id = p_po;
    COMMIT;
END$$

-- P2: ship a sales order (checks stock, deducts it, creates shipment, books the vehicle)
CREATE PROCEDURE sp_ship_order(IN p_so INT, IN p_vehicle INT)
BEGIN
    DECLARE v_status VARCHAR(20);
    DECLARE v_city   VARCHAR(50);
    DECLARE v_vstat  VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    SELECT so.status, c.city INTO v_status, v_city
    FROM sales_order so JOIN customer c ON c.customer_id = so.customer_id WHERE so.so_id = p_so;
    SELECT status INTO v_vstat FROM vehicle WHERE vehicle_id = p_vehicle;

    IF v_status IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sales order not found';
    ELSEIF v_status <> 'Placed' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only Placed orders can be shipped';
    ELSEIF v_vstat IS NULL OR v_vstat <> 'Available' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vehicle not available';
    ELSEIF EXISTS (SELECT 1 FROM sales_order_item i JOIN product p ON p.product_id = i.product_id
                   WHERE i.so_id = p_so AND p.stock_qty < i.quantity) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient finished-goods stock for this order';
    END IF;

    START TRANSACTION;
        UPDATE product p JOIN sales_order_item i ON i.product_id = p.product_id
        SET p.stock_qty = p.stock_qty - i.quantity
        WHERE i.so_id = p_so;
        INSERT INTO shipment (so_id, vehicle_id, ship_date, destination, status)
        VALUES (p_so, p_vehicle, CURDATE(), v_city, 'In Transit');
        UPDATE sales_order SET status = 'Shipped' WHERE so_id = p_so;
        UPDATE vehicle SET status = 'On Trip' WHERE vehicle_id = p_vehicle;
    COMMIT;
END$$

-- P3: raise the invoice for a shipped / delivered order (18% GST, 30-day terms)
CREATE PROCEDURE sp_create_invoice(IN p_so INT)
BEGIN
    DECLARE v_status VARCHAR(20);
    DECLARE v_total  DECIMAL(14,2);

    SELECT status, total_amount INTO v_status, v_total FROM sales_order WHERE so_id = p_so;
    IF v_status IS NULL OR v_status NOT IN ('Shipped', 'Delivered') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invoice can only be raised for Shipped/Delivered orders';
    ELSEIF EXISTS (SELECT 1 FROM invoice WHERE so_id = p_so) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invoice already exists for this order';
    END IF;

    INSERT INTO invoice (so_id, invoice_date, due_date, amount, tax_amount)
    VALUES (p_so, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), v_total, ROUND(v_total * 0.18, 2));
END$$

-- P4: run payroll for a month (YYYY-MM): 40% allowances, 12% PF/tax, loss-of-pay for absences
CREATE PROCEDURE sp_run_payroll(IN p_month CHAR(7))
BEGIN
    INSERT INTO payroll (emp_id, pay_month, basic, allowances, overtime_pay, deductions)
    SELECT e.emp_id, p_month, g.base_salary, ROUND(g.base_salary * 0.40, 2), 0,
           ROUND(g.base_salary * 1.40 * 0.12
                 + g.base_salary / 26 * (SELECT COUNT(*) FROM attendance a
                                         WHERE a.emp_id = e.emp_id AND a.status = 'Absent'
                                           AND DATE_FORMAT(a.att_date, '%Y-%m') = p_month), 2)
    FROM employee e
    JOIN designation g ON g.desig_id = e.desig_id
    WHERE e.status = 'Active'
      AND NOT EXISTS (SELECT 1 FROM payroll p WHERE p.emp_id = e.emp_id AND p.pay_month = p_month);
    SELECT ROW_COUNT() AS payroll_rows_created;
END$$

-- P5: can we start this work order? BOM requirement vs. stock in all warehouses
CREATE PROCEDURE sp_material_requirement(IN p_wo INT)
BEGIN
    SELECT rm.material_name, rm.unit,
           ROUND(b.qty_required * w.planned_qty, 2)                    AS required_qty,
           COALESCE((SELECT SUM(i.quantity) FROM inventory i WHERE i.material_id = rm.material_id), 0) AS available_qty,
           CASE WHEN COALESCE((SELECT SUM(i.quantity) FROM inventory i WHERE i.material_id = rm.material_id), 0)
                     >= b.qty_required * w.planned_qty THEN 'OK' ELSE 'SHORT' END AS availability
    FROM work_order w
    JOIN bill_of_materials b ON b.product_id = w.product_id
    JOIN raw_material rm     ON rm.material_id = b.material_id
    WHERE w.wo_id = p_wo;
END$$

-- P6: transfer stock between warehouses atomically
CREATE PROCEDURE sp_transfer_stock(IN p_from INT, IN p_to INT, IN p_material INT, IN p_qty DECIMAL(12,2))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;
    IF p_from = p_to THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Source and destination warehouse must differ';
    END IF;
    IF COALESCE((SELECT quantity FROM inventory WHERE warehouse_id = p_from AND material_id = p_material), 0) < p_qty THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient stock in source warehouse';
    END IF;
    START TRANSACTION;
        UPDATE inventory SET quantity = quantity - p_qty, last_updated = CURDATE()
        WHERE warehouse_id = p_from AND material_id = p_material;
        INSERT INTO inventory (warehouse_id, material_id, quantity, last_updated)
        VALUES (p_to, p_material, p_qty, CURDATE())
        ON DUPLICATE KEY UPDATE quantity = quantity + p_qty, last_updated = CURDATE();
    COMMIT;
END$$

-- P7: Dynamically register user and map to domain tables (CUSTOMER, EMPLOYEE, TRANSPORTER)
CREATE PROCEDURE sp_register_user(
    IN p_username    VARCHAR(60),
    IN p_password    VARCHAR(255),
    IN p_role        VARCHAR(30),
    IN p_full_name   VARCHAR(100),
    IN p_email       VARCHAR(100),
    IN p_department  VARCHAR(60)
)
BEGIN
    DECLARE v_dept_id INT DEFAULT 1;
    DECLARE v_emp_id INT DEFAULT NULL;
    DECLARE v_cust_id INT DEFAULT NULL;
    DECLARE v_contractor_id INT DEFAULT NULL;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    START TRANSACTION;
        IF p_role = 'CUSTOMER' THEN
            INSERT INTO customer (customer_name, contact_person, email, phone, city, state, credit_limit, gstin)
            VALUES (p_full_name, p_full_name, p_email, '+91 9876543210', 'Pune', 'Maharashtra', 2500000.00, CONCAT('27AAB', FLOOR(1000 + (RAND() * 9000)), 'C1Z1'));
            SET v_cust_id = LAST_INSERT_ID();
        ELSEIF p_role = 'TRANSPORTER' THEN
            INSERT INTO contractor (company_name, service_type, phone, plant_id, contract_start, contract_end, contract_value)
            VALUES (p_full_name, 'Heavy Freight Logistics & Transport', '+91 9811223344', 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 2 YEAR), 5000000.00);
            SET v_contractor_id = LAST_INSERT_ID();
        ELSE
            INSERT INTO employee (first_name, last_name, gender, dob, hire_date, email, phone, dept_id, desig_id, plant_id, shift_id, status)
            VALUES (SUBSTRING_INDEX(p_full_name, ' ', 1), 
                    IF(LOCATE(' ', p_full_name) > 0, SUBSTRING(p_full_name, LOCATE(' ', p_full_name) + 1), 'Staff'),
                    'M', '1992-05-14', CURDATE(), p_email, '+91 9822001122', v_dept_id, 3, 1, 1, 'Active');
            SET v_emp_id = LAST_INSERT_ID();
        END IF;

        INSERT INTO user_account (username, password, role, full_name, email, department_id, employee_id, customer_id, contractor_id, enabled)
        VALUES (p_username, p_password, p_role, p_full_name, p_email, v_dept_id, v_emp_id, v_cust_id, v_contractor_id, TRUE);

        INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value)
        VALUES (CONCAT('AUD-', FLOOR(10000 + RAND() * 90000)), 'INSERT', 'USER_REGISTRATION', p_username,
                CONCAT('Registered new user: ', p_full_name, ' (', p_role, ') mapped to relational tables'), 'None', CONCAT('Role: ', p_role, ', Email: ', p_email));
    COMMIT;
END

-- P8: Safely delete user account and audit event
CREATE PROCEDURE sp_delete_user_account(IN p_username VARCHAR(60))
BEGIN
    DECLARE v_id INT;
    DECLARE v_role VARCHAR(30);
    DECLARE v_fullname VARCHAR(100);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    SELECT id, role, full_name INTO v_id, v_role, v_fullname 
    FROM user_account WHERE username = p_username;

    IF v_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User account not found';
    END IF;

    START TRANSACTION;
        DELETE FROM user_account WHERE id = v_id;

        INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value)
        VALUES (CONCAT('AUD-', FLOOR(10000 + RAND() * 90000)), 'DELETE', 'USER_ACCOUNT', p_username,
                CONCAT('User account permanently deleted: ', p_username, ' (', v_role, ')'),
                CONCAT('User ID: ', v_id, ', Name: ', v_fullname), 'ACCOUNT_DELETED');
    COMMIT;
END

-- P9: Safely delete sales order, cascade line items & payments, and log audit
CREATE PROCEDURE sp_delete_sales_order(IN p_so_id INT, IN p_actor VARCHAR(60))
BEGIN
    DECLARE v_total DECIMAL(14,2);
    DECLARE v_status VARCHAR(20);
    DECLARE v_inv_id INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    SELECT total_amount, status INTO v_total, v_status FROM sales_order WHERE so_id = p_so_id;
    IF v_total IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sales order not found';
    END IF;

    START TRANSACTION;
        -- Delete dependent payments & invoice
        SELECT invoice_id INTO v_inv_id FROM invoice WHERE so_id = p_so_id;
        IF v_inv_id IS NOT NULL THEN
            DELETE FROM payment WHERE invoice_id = v_inv_id;
            DELETE FROM invoice WHERE invoice_id = v_inv_id;
        END IF;

        -- Delete shipment
        DELETE FROM shipment WHERE so_id = p_so_id;

        -- Delete order items
        DELETE FROM sales_order_item WHERE so_id = p_so_id;

        -- Delete sales order
        DELETE FROM sales_order WHERE so_id = p_so_id;

        -- Audit record
        INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value)
        VALUES (CONCAT('AUD-', FLOOR(10000 + RAND() * 90000)), 'DELETE', 'SALES_ORDER', COALESCE(p_actor, 'system'),
                CONCAT('Deleted Sales Order #', p_so_id, ' with all line items, payments & invoices'),
                CONCAT('SO ID: ', p_so_id, ', Total: ₹', v_total, ', Status: ', v_status), 'ORDER_DELETED');
    COMMIT;
END

DELIMITER ;

-- DEMO CALLS (uncomment to try)
-- CALL sp_register_user('buyer_new', 'password123', 'CUSTOMER', 'Rohan Enterprises', 'rohan@enterprise.in', 'Sales');
-- CALL sp_delete_user_account('buyer_new');
-- CALL sp_delete_sales_order(1, 'ceo');
-- CALL sp_material_requirement(11);
-- CALL sp_run_payroll('2026-10');
-- CALL sp_transfer_stock(1, 2, 1, 100);
-- SELECT fn_product_margin_pct(1), fn_tenure_years(1), fn_customer_outstanding(1);
