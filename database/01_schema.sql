DROP DATABASE IF EXISTS industrial_mgmt;
CREATE DATABASE industrial_mgmt;
USE industrial_mgmt;

-- MODULE 1 : ORGANISATION & HUMAN RESOURCES

CREATE TABLE plant (
    plant_id       INT AUTO_INCREMENT PRIMARY KEY,
    plant_name     VARCHAR(80)  NOT NULL UNIQUE,
    city           VARCHAR(50)  NOT NULL,
    state          VARCHAR(50)  NOT NULL,
    daily_capacity INT          NOT NULL CHECK (daily_capacity > 0),
    established    DATE         NOT NULL
);

CREATE TABLE department (
    dept_id    INT AUTO_INCREMENT PRIMARY KEY,
    dept_name  VARCHAR(60) NOT NULL UNIQUE,
    location   VARCHAR(60),
    budget     DECIMAL(12,2) NOT NULL DEFAULT 0
);

CREATE TABLE designation (
    desig_id    INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(60)  NOT NULL UNIQUE,
    grade       CHAR(2)      NOT NULL,
    base_salary DECIMAL(10,2) NOT NULL CHECK (base_salary > 0)
);

CREATE TABLE shift (
    shift_id   INT AUTO_INCREMENT PRIMARY KEY,
    shift_name VARCHAR(30) NOT NULL UNIQUE,
    start_time TIME NOT NULL,
    end_time   TIME NOT NULL
);

CREATE TABLE employee (
    emp_id      INT AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(40) NOT NULL,
    last_name   VARCHAR(40) NOT NULL,
    gender      ENUM('M','F','O') NOT NULL,
    dob         DATE NOT NULL,
    hire_date   DATE NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    phone       VARCHAR(15),
    dept_id     INT NOT NULL,
    desig_id    INT NOT NULL,
    plant_id    INT NOT NULL,
    shift_id    INT NOT NULL,
    manager_id  INT NULL,
    status      ENUM('Active','On Leave','Resigned') NOT NULL DEFAULT 'Active',
    FOREIGN KEY (dept_id)    REFERENCES department(dept_id),
    FOREIGN KEY (desig_id)   REFERENCES designation(desig_id),
    FOREIGN KEY (plant_id)   REFERENCES plant(plant_id),
    FOREIGN KEY (shift_id)   REFERENCES shift(shift_id),
    FOREIGN KEY (manager_id) REFERENCES employee(emp_id)
);

CREATE TABLE attendance (
    att_id     INT AUTO_INCREMENT PRIMARY KEY,
    emp_id     INT NOT NULL,
    att_date   DATE NOT NULL,
    check_in   TIME NULL,
    check_out  TIME NULL,
    status     ENUM('Present','Absent','Half Day','Leave') NOT NULL,
    UNIQUE (emp_id, att_date),
    FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE
);

CREATE TABLE leave_request (
    leave_id    INT AUTO_INCREMENT PRIMARY KEY,
    emp_id      INT NOT NULL,
    leave_type  ENUM('Casual','Sick','Earned','Maternity','Unpaid') NOT NULL,
    from_date   DATE NOT NULL,
    to_date     DATE NOT NULL,
    status      ENUM('Pending','Approved','Rejected') NOT NULL DEFAULT 'Pending',
    approved_by INT NULL,
    CHECK (to_date >= from_date),
    FOREIGN KEY (emp_id)      REFERENCES employee(emp_id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES employee(emp_id)
);

CREATE TABLE payroll (
    payroll_id   INT AUTO_INCREMENT PRIMARY KEY,
    emp_id       INT NOT NULL,
    pay_month    CHAR(7) NOT NULL,
    basic        DECIMAL(10,2) NOT NULL,
    allowances   DECIMAL(10,2) NOT NULL DEFAULT 0,
    overtime_pay DECIMAL(10,2) NOT NULL DEFAULT 0,
    deductions   DECIMAL(10,2) NOT NULL DEFAULT 0,
    net_pay      DECIMAL(10,2) GENERATED ALWAYS AS (basic + allowances + overtime_pay - deductions) STORED,
    UNIQUE (emp_id, pay_month),
    FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE
);

CREATE TABLE training_program (
    training_id    INT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(100) NOT NULL UNIQUE,
    duration_hours INT NOT NULL CHECK (duration_hours > 0),
    mandatory      BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE employee_training (
    emp_id          INT NOT NULL,
    training_id     INT NOT NULL,
    completion_date DATE NOT NULL,
    score           DECIMAL(5,2),
    PRIMARY KEY (emp_id, training_id),
    FOREIGN KEY (emp_id)      REFERENCES employee(emp_id) ON DELETE CASCADE,
    FOREIGN KEY (training_id) REFERENCES training_program(training_id)
);

CREATE TABLE contractor (
    contractor_id  INT AUTO_INCREMENT PRIMARY KEY,
    company_name   VARCHAR(100) NOT NULL,
    service_type   VARCHAR(60)  NOT NULL,
    phone          VARCHAR(15),
    plant_id       INT NOT NULL,
    contract_start DATE NOT NULL,
    contract_end   DATE NOT NULL,
    contract_value DECIMAL(12,2) NOT NULL,
    CHECK (contract_end >= contract_start),
    FOREIGN KEY (plant_id) REFERENCES plant(plant_id)
);

-- MODULE 2 : PRODUCTION LINES, MACHINES & MAINTENANCE

CREATE TABLE production_line (
    line_id       INT AUTO_INCREMENT PRIMARY KEY,
    plant_id      INT NOT NULL,
    line_name     VARCHAR(60) NOT NULL,
    hourly_output INT NOT NULL CHECK (hourly_output > 0),
    status        ENUM('Running','Idle','Shutdown') NOT NULL DEFAULT 'Running',
    UNIQUE (plant_id, line_name),
    FOREIGN KEY (plant_id) REFERENCES plant(plant_id)
);

CREATE TABLE machine (
    machine_id     INT AUTO_INCREMENT PRIMARY KEY,
    line_id        INT NOT NULL,
    machine_name   VARCHAR(80) NOT NULL,
    model_no       VARCHAR(40),
    manufacturer   VARCHAR(60),
    purchase_date  DATE NOT NULL,
    purchase_cost  DECIMAL(12,2) NOT NULL,
    status         ENUM('Operational','Under Maintenance','Decommissioned') NOT NULL DEFAULT 'Operational',
    last_service   DATE,
    FOREIGN KEY (line_id) REFERENCES production_line(line_id)
);

CREATE TABLE spare_part (
    part_id       INT AUTO_INCREMENT PRIMARY KEY,
    part_name     VARCHAR(80) NOT NULL UNIQUE,
    unit_cost     DECIMAL(10,2) NOT NULL,
    stock_qty     INT NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    reorder_level INT NOT NULL DEFAULT 5
);

CREATE TABLE maintenance_log (
    maint_id       INT AUTO_INCREMENT PRIMARY KEY,
    machine_id     INT NOT NULL,
    technician_id  INT NOT NULL,
    maint_type     ENUM('Preventive','Corrective','Breakdown') NOT NULL,
    maint_date     DATE NOT NULL,
    downtime_hours DECIMAL(5,1) NOT NULL DEFAULT 0,
    labour_cost    DECIMAL(10,2) NOT NULL DEFAULT 0,
    description    VARCHAR(255),
    FOREIGN KEY (machine_id)    REFERENCES machine(machine_id),
    FOREIGN KEY (technician_id) REFERENCES employee(emp_id)
);

CREATE TABLE maintenance_part_usage (
    maint_id INT NOT NULL,
    part_id  INT NOT NULL,
    qty_used INT NOT NULL CHECK (qty_used > 0),
    PRIMARY KEY (maint_id, part_id),
    FOREIGN KEY (maint_id) REFERENCES maintenance_log(maint_id) ON DELETE CASCADE,
    FOREIGN KEY (part_id)  REFERENCES spare_part(part_id)
);

CREATE TABLE energy_consumption (
    record_id   INT AUTO_INCREMENT PRIMARY KEY,
    plant_id    INT NOT NULL,
    record_date DATE NOT NULL,
    units_kwh   DECIMAL(10,2) NOT NULL,
    cost        DECIMAL(10,2) NOT NULL,
    UNIQUE (plant_id, record_date),
    FOREIGN KEY (plant_id) REFERENCES plant(plant_id)
);

CREATE TABLE safety_incident (
    incident_id   INT AUTO_INCREMENT PRIMARY KEY,
    plant_id      INT NOT NULL,
    emp_id        INT NULL,
    incident_date DATE NOT NULL,
    severity      ENUM('Minor','Moderate','Major','Critical') NOT NULL,
    description   VARCHAR(255) NOT NULL,
    action_taken  VARCHAR(255),
    FOREIGN KEY (plant_id) REFERENCES plant(plant_id),
    FOREIGN KEY (emp_id)   REFERENCES employee(emp_id)
);

-- MODULE 3 : SUPPLIERS, RAW MATERIALS, WAREHOUSING & PURCHASING

CREATE TABLE supplier (
    supplier_id    INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name  VARCHAR(100) NOT NULL,
    contact_person VARCHAR(60),
    phone          VARCHAR(15),
    email          VARCHAR(100),
    city           VARCHAR(50),
    rating         DECIMAL(2,1) CHECK (rating BETWEEN 1 AND 5),
    gstin          VARCHAR(15) UNIQUE
);

CREATE TABLE material_category (
    cat_id   INT AUTO_INCREMENT PRIMARY KEY,
    cat_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE raw_material (
    material_id   INT AUTO_INCREMENT PRIMARY KEY,
    material_name VARCHAR(80) NOT NULL UNIQUE,
    cat_id        INT NOT NULL,
    unit          VARCHAR(10) NOT NULL,
    unit_cost     DECIMAL(10,2) NOT NULL CHECK (unit_cost > 0),
    reorder_level DECIMAL(10,2) NOT NULL DEFAULT 100,
    FOREIGN KEY (cat_id) REFERENCES material_category(cat_id)
);

CREATE TABLE supplier_material (
    supplier_id    INT NOT NULL,
    material_id    INT NOT NULL,
    quoted_price   DECIMAL(10,2) NOT NULL,
    lead_time_days INT NOT NULL,
    PRIMARY KEY (supplier_id, material_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES raw_material(material_id) ON DELETE CASCADE
);

CREATE TABLE warehouse (
    warehouse_id   INT AUTO_INCREMENT PRIMARY KEY,
    plant_id       INT NOT NULL,
    warehouse_name VARCHAR(60) NOT NULL,
    capacity_units INT NOT NULL,
    manager_id     INT NULL,
    FOREIGN KEY (plant_id)   REFERENCES plant(plant_id),
    FOREIGN KEY (manager_id) REFERENCES employee(emp_id)
);

CREATE TABLE inventory (
    inv_id       INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL,
    material_id  INT NOT NULL,
    quantity     DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    last_updated DATE NOT NULL,
    UNIQUE (warehouse_id, material_id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id),
    FOREIGN KEY (material_id)  REFERENCES raw_material(material_id)
);

CREATE TABLE purchase_order (
    po_id         INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id   INT NOT NULL,
    created_by    INT NOT NULL,
    order_date    DATE NOT NULL,
    expected_date DATE NOT NULL,
    status        ENUM('Placed','Received','Cancelled') NOT NULL DEFAULT 'Placed',
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id),
    FOREIGN KEY (created_by)  REFERENCES employee(emp_id)
);

CREATE TABLE purchase_order_item (
    po_item_id  INT AUTO_INCREMENT PRIMARY KEY,
    po_id       INT NOT NULL,
    material_id INT NOT NULL,
    quantity    DECIMAL(12,2) NOT NULL CHECK (quantity > 0),
    unit_price  DECIMAL(10,2) NOT NULL,
    line_total  DECIMAL(14,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    FOREIGN KEY (po_id)       REFERENCES purchase_order(po_id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES raw_material(material_id)
);

-- MODULE 4 : PRODUCTS, BOM, WORK ORDERS & QUALITY

CREATE TABLE product_category (
    pcat_id   INT AUTO_INCREMENT PRIMARY KEY,
    pcat_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE product (
    product_id    INT AUTO_INCREMENT PRIMARY KEY,
    product_name  VARCHAR(100) NOT NULL,
    sku           VARCHAR(20)  NOT NULL UNIQUE,
    pcat_id       INT NOT NULL,
    unit_price    DECIMAL(10,2) NOT NULL CHECK (unit_price > 0),
    standard_cost DECIMAL(10,2) NOT NULL,
    stock_qty     INT NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    FOREIGN KEY (pcat_id) REFERENCES product_category(pcat_id)
);

CREATE TABLE bill_of_materials (
    product_id   INT NOT NULL,
    material_id  INT NOT NULL,
    qty_required DECIMAL(10,3) NOT NULL CHECK (qty_required > 0),
    PRIMARY KEY (product_id, material_id),
    FOREIGN KEY (product_id)  REFERENCES product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES raw_material(material_id)
);

CREATE TABLE work_order (
    wo_id         INT AUTO_INCREMENT PRIMARY KEY,
    product_id    INT NOT NULL,
    line_id       INT NOT NULL,
    supervisor_id INT NOT NULL,
    planned_qty   INT NOT NULL CHECK (planned_qty > 0),
    start_date    DATE NOT NULL,
    end_date      DATE,
    status        ENUM('Planned','In Progress','Completed','Cancelled') NOT NULL DEFAULT 'Planned',
    FOREIGN KEY (product_id)    REFERENCES product(product_id),
    FOREIGN KEY (line_id)       REFERENCES production_line(line_id),
    FOREIGN KEY (supervisor_id) REFERENCES employee(emp_id)
);

CREATE TABLE production_batch (
    batch_id     INT AUTO_INCREMENT PRIMARY KEY,
    wo_id        INT NOT NULL,
    shift_id     INT NOT NULL,
    batch_no     VARCHAR(20) NOT NULL UNIQUE,
    batch_date   DATE NOT NULL,
    produced_qty INT NOT NULL CHECK (produced_qty >= 0),
    rejected_qty INT NOT NULL DEFAULT 0,
    CHECK (rejected_qty <= produced_qty),
    FOREIGN KEY (wo_id)    REFERENCES work_order(wo_id),
    FOREIGN KEY (shift_id) REFERENCES shift(shift_id)
);

CREATE TABLE defect_type (
    defect_type_id INT AUTO_INCREMENT PRIMARY KEY,
    defect_name    VARCHAR(60) NOT NULL UNIQUE,
    severity       ENUM('Low','Medium','High') NOT NULL
);

CREATE TABLE quality_inspection (
    insp_id       INT AUTO_INCREMENT PRIMARY KEY,
    batch_id      INT NOT NULL,
    inspector_id  INT NOT NULL,
    insp_date     DATE NOT NULL,
    sample_size   INT NOT NULL,
    defects_found INT NOT NULL DEFAULT 0,
    result        ENUM('Pass','Fail','Rework') NOT NULL,
    remarks       VARCHAR(200),
    FOREIGN KEY (batch_id)     REFERENCES production_batch(batch_id),
    FOREIGN KEY (inspector_id) REFERENCES employee(emp_id)
);

CREATE TABLE defect_log (
    defect_id      INT AUTO_INCREMENT PRIMARY KEY,
    insp_id        INT NOT NULL,
    defect_type_id INT NOT NULL,
    qty            INT NOT NULL CHECK (qty > 0),
    FOREIGN KEY (insp_id)        REFERENCES quality_inspection(insp_id) ON DELETE CASCADE,
    FOREIGN KEY (defect_type_id) REFERENCES defect_type(defect_type_id)
);

-- MODULE 5 : CUSTOMERS, SALES, LOGISTICS & FINANCE

CREATE TABLE customer (
    customer_id    INT AUTO_INCREMENT PRIMARY KEY,
    customer_name  VARCHAR(100) NOT NULL,
    contact_person VARCHAR(60),
    phone          VARCHAR(15),
    email          VARCHAR(100),
    city           VARCHAR(50) NOT NULL,
    state          VARCHAR(50) NOT NULL,
    credit_limit   DECIMAL(12,2) NOT NULL DEFAULT 0,
    gstin          VARCHAR(15) UNIQUE
);

CREATE TABLE sales_order (
    so_id         INT AUTO_INCREMENT PRIMARY KEY,
    customer_id   INT NOT NULL,
    order_date    DATE NOT NULL,
    delivery_date DATE NOT NULL,
    status        ENUM('Placed','Shipped','Delivered','Cancelled') NOT NULL DEFAULT 'Placed',
    total_amount  DECIMAL(14,2) NOT NULL DEFAULT 0,
    CHECK (delivery_date >= order_date),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE sales_order_item (
    so_item_id INT AUTO_INCREMENT PRIMARY KEY,
    so_id      INT NOT NULL,
    product_id INT NOT NULL,
    quantity   INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10,2) NOT NULL,
    line_total DECIMAL(14,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    FOREIGN KEY (so_id)      REFERENCES sales_order(so_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE vehicle (
    vehicle_id    INT AUTO_INCREMENT PRIMARY KEY,
    reg_no        VARCHAR(15) NOT NULL UNIQUE,
    vehicle_type  VARCHAR(30) NOT NULL,
    capacity_tons DECIMAL(5,1) NOT NULL,
    driver_name   VARCHAR(60) NOT NULL,
    driver_phone  VARCHAR(15),
    status        ENUM('Available','On Trip','Under Repair') NOT NULL DEFAULT 'Available'
);

CREATE TABLE shipment (
    shipment_id   INT AUTO_INCREMENT PRIMARY KEY,
    so_id         INT NOT NULL,
    vehicle_id    INT NOT NULL,
    ship_date     DATE NOT NULL,
    delivered_on  DATE NULL,
    destination   VARCHAR(100) NOT NULL,
    status        ENUM('In Transit','Delivered','Delayed') NOT NULL DEFAULT 'In Transit',
    FOREIGN KEY (so_id)      REFERENCES sales_order(so_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

CREATE TABLE invoice (
    invoice_id   INT AUTO_INCREMENT PRIMARY KEY,
    so_id        INT NOT NULL UNIQUE,
    invoice_date DATE NOT NULL,
    due_date     DATE NOT NULL,
    amount       DECIMAL(14,2) NOT NULL,
    tax_amount   DECIMAL(14,2) NOT NULL,
    status       ENUM('Unpaid','Partial','Paid') NOT NULL DEFAULT 'Unpaid',
    FOREIGN KEY (so_id) REFERENCES sales_order(so_id) ON DELETE CASCADE
);

CREATE TABLE payment (
    payment_id   INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id   INT NOT NULL,
    payment_date DATE NOT NULL,
    amount       DECIMAL(14,2) NOT NULL CHECK (amount > 0),
    mode         ENUM('Cash','UPI','Bank Transfer','Cheque') NOT NULL,
    ref_no       VARCHAR(30),
    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id) ON DELETE CASCADE
);

-- =============================================================================
-- MODULE 6 : AUDIT & ACTIVITY LOGS (TRANSACTIONAL AUDITING)
-- =============================================================================

CREATE TABLE audit_log (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    audit_id        VARCHAR(30) NOT NULL,
    action_type     VARCHAR(20) NOT NULL, -- INSERT, UPDATE, DELETE
    entity_name     VARCHAR(60) NOT NULL,
    username        VARCHAR(60) NOT NULL,
    payload_summary VARCHAR(500),
    previous_value  VARCHAR(500),
    new_value       VARCHAR(500),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- MODULE 7 : USER ACCOUNTS & AUTHENTICATION (RBAC & DOMAIN TABLE MAPPING)
-- =============================================================================

CREATE TABLE user_account (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    username             VARCHAR(60) NOT NULL UNIQUE,
    password             VARCHAR(255) NOT NULL,
    role                 VARCHAR(30) NOT NULL,
    full_name            VARCHAR(100) NOT NULL,
    email                VARCHAR(100) NOT NULL UNIQUE,
    department_id        INT,
    employee_id          INT,
    customer_id          INT,
    contractor_id        INT,
    assigned_machine_id  INT,
    enabled              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id)       REFERENCES department(dept_id) ON DELETE SET NULL,
    FOREIGN KEY (employee_id)         REFERENCES employee(emp_id) ON DELETE SET NULL,
    FOREIGN KEY (customer_id)         REFERENCES customer(customer_id) ON DELETE SET NULL,
    FOREIGN KEY (contractor_id)       REFERENCES contractor(contractor_id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_machine_id) REFERENCES machine(machine_id) ON DELETE SET NULL
);

-- Helpful indexes for the most frequent joins / filters
CREATE INDEX idx_emp_dept      ON employee(dept_id);
CREATE INDEX idx_att_date      ON attendance(att_date);
CREATE INDEX idx_batch_wo      ON production_batch(wo_id);
CREATE INDEX idx_soitem_so     ON sales_order_item(so_id);
CREATE INDEX idx_so_customer   ON sales_order(customer_id);
CREATE INDEX idx_maint_machine ON maintenance_log(machine_id);
CREATE INDEX idx_user_username ON user_account(username);
CREATE INDEX idx_user_role     ON user_account(role);
CREATE INDEX idx_audit_created ON audit_log(created_at);
CREATE INDEX idx_audit_action  ON audit_log(action_type);

-- Compatibility Views for direct 'users' and 'orders' queries
CREATE OR REPLACE VIEW users AS 
SELECT id, username, password, role, full_name, email, department_id, employee_id, customer_id, contractor_id, assigned_machine_id, enabled, created_at 
FROM user_account;

CREATE OR REPLACE VIEW orders AS 
SELECT so_id AS order_id, customer_id, order_date, delivery_date, status, total_amount, created_at 
FROM sales_order;
