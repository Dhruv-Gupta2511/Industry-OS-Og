# Industrial Management System (RDBMS Project, MySQL 8.0+)

A manufacturing-plant database covering HR, production, maintenance, purchasing, inventory, quality, sales, logistics and finance. **42 tables**, about 1,400 rows of sample data, 10 views, 12 triggers, 3 functions, 6 stored procedures and 30 report queries.

## How to run

Open MySQL Workbench (or the `mysql` CLI) and run the files in this order:

| # | File | Contents |
|---|------|----------|
| 1 | `01_schema.sql` | Creates the `industrial_mgmt` database and all 42 tables with PK, FK, CHECK, UNIQUE, generated columns, indexes |
| 2 | `02_data.sql` | Sample data (3 plants, 43 employees, 22 machines, 26 sales orders, ...) |
| 3 | `03_views.sql` | 10 reporting views |
| 4 | `04_routines_triggers.sql` | Triggers, functions, stored procedures (uses `DELIMITER`) |
| 5 | `05_queries.sql` | 30 report queries + trigger/procedure demos |



## Entities (42 tables)

| Module | Tables |
|--------|--------|
| Organisation & HR | plant, department, designation, shift, employee, attendance, leave_request, payroll, training_program, employee_training, contractor |
| Production & Maintenance | production_line, machine, spare_part, maintenance_log, maintenance_part_usage, energy_consumption, safety_incident |
| Purchasing & Stores | supplier, material_category, raw_material, supplier_material, warehouse, inventory, purchase_order, purchase_order_item |
| Products & Quality | product_category, product, bill_of_materials, work_order, production_batch, defect_type, quality_inspection, defect_log |
| Sales & Finance | customer, sales_order, sales_order_item, vehicle, shipment, invoice, payment |
| Audit | audit_log |

**Many-to-many link tables:** employee_training, supplier_material, bill_of_materials, maintenance_part_usage.
**Self-referencing FK:** employee.manager_id.

## Business flow the database models

1. Purchase order to supplier, then stock is received into a warehouse (`sp_receive_purchase_order`).
2. A work order for a product is run on a production line. The bill of materials says what it consumes. Batches are logged per shift, and finished stock goes up automatically (`trg_batch_ai`).
3. Each batch is inspected. The result is derived from defects found, and defect types are logged (`trg_inspection_bi`).
4. A customer places a sales order, and the order total updates itself from its items (`trg_soitem_*`).
5. The order is shipped (`sp_ship_order` checks stock, deducts it, books a vehicle), invoiced (`sp_create_invoice`) and paid. Payments update the invoice status and over-payment is blocked (`trg_payment_*`).
6. Maintenance logs update machine status and consume spare parts (`trg_maintenance_ai`, `trg_partusage_*`).
7. Monthly payroll is generated with `CALL sp_run_payroll('2026-10');`.

## Triggers, functions, procedures

- **Triggers:** auto order total (3), batch adds finished stock, inspection result auto-grading, payment over-pay guard, invoice status sync, maintenance updates machine, spare-part consume and guard (2), audit on employee and product price changes (2)
- **Functions:** `fn_product_margin_pct`, `fn_tenure_years`, `fn_customer_outstanding`
- **Procedures:** `sp_receive_purchase_order`, `sp_ship_order`, `sp_create_invoice`, `sp_run_payroll`, `sp_material_requirement`, `sp_transfer_stock`

## Notes

- When inserting into `quality_inspection`, pass any value for `result`; the trigger overwrites it from `defects_found`.
- The sample data was loaded before the triggers exist, so it is internally consistent without them (order totals match items, invoice statuses match payments, stock is never negative).
- Sample data is fictional.
