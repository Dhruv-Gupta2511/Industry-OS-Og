import os
import re

print("Starting critical hotfix script...")

# 1. Update Database Files
schema_path = "database/01_schema.sql"
if os.path.exists(schema_path):
    with open(schema_path, "r", encoding="utf-8") as f:
        schema = f.read()

    views_sql = """
-- Compatibility Views for direct 'users' and 'orders' queries
CREATE OR REPLACE VIEW users AS 
SELECT id, username, password, role, full_name, email, department_id, employee_id, customer_id, contractor_id, assigned_machine_id, enabled, created_at 
FROM user_account;

CREATE OR REPLACE VIEW orders AS 
SELECT so_id AS order_id, customer_id, order_date, delivery_date, status, total_amount, created_at 
FROM sales_order;
"""
    if "CREATE OR REPLACE VIEW users" not in schema:
        schema += views_sql
        with open(schema_path, "w", encoding="utf-8") as f:
            f.write(schema)
        print("Updated database/01_schema.sql with 'users' and 'orders' views.")

queries_path = "database/05_queries.sql"
if os.path.exists(queries_path):
    with open(queries_path, "r", encoding="utf-8") as f:
        queries = f.read()

    param_queries_sql = """

-- =========================================================================
-- SECTION 7: PARAMETERIZED CRUD STATEMENTS (PREPARED STATEMENTS WITH ?)
-- =========================================================================

-- Q31. Parameterized User Registration Query
-- INSERT INTO user_account (username, password, role, full_name, email, department_id, customer_id, employee_id, contractor_id, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
-- INSERT INTO customer (customer_name, contact_person, email, phone, city, state, credit_limit, gstin) VALUES (?, ?, ?, ?, ?, ?, ?, ?);
-- INSERT INTO employee (first_name, last_name, gender, dob, hire_date, email, phone, dept_id, desig_id, plant_id, shift_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
-- INSERT INTO contractor (company_name, service_type, phone, plant_id, contract_start, contract_end, contract_value) VALUES (?, ?, ?, ?, ?, ?, ?);

-- Q32. Parameterized Delete User Account Query
-- DELETE FROM user_account WHERE id = ?;
-- DELETE FROM user_account WHERE username = ?;
-- DELETE FROM users WHERE id = ?;

-- Q33. Parameterized Delete Order Query (with cascading cleanup)
-- DELETE FROM payment WHERE invoice_id IN (SELECT invoice_id FROM invoice WHERE so_id = ?);
-- DELETE FROM invoice WHERE so_id = ?;
-- DELETE FROM shipment WHERE so_id = ?;
-- DELETE FROM sales_order_item WHERE so_id = ?;
-- DELETE FROM sales_order WHERE so_id = ?;
-- DELETE FROM orders WHERE order_id = ?;
"""
    if "SECTION 7: PARAMETERIZED CRUD STATEMENTS" not in queries:
        queries += param_queries_sql
        with open(queries_path, "w", encoding="utf-8") as f:
            f.write(queries)
        print("Updated database/05_queries.sql with Section 7 parameterized queries.")

print("Database scripts updated.")
