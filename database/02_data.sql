USE industrial_mgmt;

-- plant (3 rows)
INSERT INTO plant (plant_name, city, state, daily_capacity, established) VALUES
  ('Pune Works', 'Pune', 'Maharashtra', 1200, '2008-04-12'),
  ('Jaipur Works', 'Jaipur', 'Rajasthan', 900, '2012-09-03'),
  ('Chennai Works', 'Chennai', 'Tamil Nadu', 1000, '2016-01-20');

-- department (10 rows)
INSERT INTO department (dept_name, location, budget) VALUES
  ('Production', 'Shop Floor', 9500000),
  ('Maintenance', 'Workshop', 4200000),
  ('Quality Control', 'QC Lab', 3100000),
  ('Stores & Inventory', 'Warehouse', 2800000),
  ('Procurement', 'Admin Block', 2500000),
  ('Sales & Marketing', 'Admin Block', 5200000),
  ('Human Resources', 'Admin Block', 1800000),
  ('Finance & Accounts', 'Admin Block', 2200000),
  ('Safety & EHS', 'Admin Block', 1500000),
  ('Logistics', 'Dispatch Yard', 3300000);

-- designation (10 rows)
INSERT INTO designation (title, grade, base_salary) VALUES
  ('Plant Manager', 'G1', 120000),
  ('Department Manager', 'G2', 85000),
  ('Engineer', 'G3', 55000),
  ('Supervisor', 'G3', 48000),
  ('Senior Technician', 'G4', 38000),
  ('Technician', 'G5', 28000),
  ('Operator', 'G5', 24000),
  ('Executive', 'G4', 40000),
  ('Accountant', 'G4', 42000),
  ('Logistics Helper', 'G6', 20000);

-- shift (3 rows)
INSERT INTO shift (shift_name, start_time, end_time) VALUES
  ('Morning', '06:00:00', '14:00:00'),
  ('General', '09:00:00', '17:00:00'),
  ('Night', '22:00:00', '06:00:00');

-- employee (43 rows)
INSERT INTO employee (emp_id, first_name, last_name, gender, dob, hire_date, email, phone, dept_id, desig_id, plant_id, shift_id, manager_id, status) VALUES
  (1, 'Suresh', 'Pillai', 'M', '1972-07-12', '2022-07-11', 'suresh.pillai1@indmfg.in', '9398471886', 1, 1, 1, 1, NULL, 'Active'),
  (2, 'Anil', 'Singh', 'M', '1988-05-03', '2022-03-08', 'anil.singh2@indmfg.in', '9274648506', 1, 1, 2, 2, NULL, 'Active'),
  (3, 'Neha', 'Iyer', 'F', '1984-08-09', '2021-12-20', 'neha.iyer3@indmfg.in', '9324956459', 1, 1, 3, 3, NULL, 'Active'),
  (4, 'Geeta', 'Gupta', 'F', '1998-01-23', '2019-12-28', 'geeta.gupta4@indmfg.in', '9853573823', 1, 2, 1, 1, 1, 'Active'),
  (5, 'Ashok', 'Saxena', 'M', '1998-08-30', '2015-08-08', 'ashok.saxena5@indmfg.in', '9754049436', 2, 2, 2, 2, 1, 'Active'),
  (6, 'Padma', 'Sharma', 'F', '1996-06-25', '2017-11-02', 'padma.sharma6@indmfg.in', '9673528321', 3, 2, 3, 3, 1, 'Active'),
  (7, 'Kavita', 'Shah', 'F', '2000-09-15', '2019-06-29', 'kavita.shah7@indmfg.in', '9275452091', 4, 2, 1, 1, 1, 'Active'),
  (8, 'Rajesh', 'Menon', 'M', '1988-09-25', '2022-07-06', 'rajesh.menon8@indmfg.in', '9389854268', 5, 2, 2, 2, 1, 'Active'),
  (9, 'Swati', 'Nair', 'F', '1996-09-15', '2018-12-05', 'swati.nair9@indmfg.in', '9835098955', 6, 2, 3, 3, 1, 'Active'),
  (10, 'Priya', 'Mehta', 'F', '1982-07-19', '2015-04-04', 'priya.mehta10@indmfg.in', '9345938494', 7, 2, 1, 1, 1, 'Active'),
  (11, 'Rohit', 'Kulkarni', 'M', '1969-06-09', '2021-01-28', 'rohit.kulkarni11@indmfg.in', '9530747414', 8, 2, 2, 2, 1, 'Active'),
  (12, 'Meena', 'Reddy', 'F', '1980-01-04', '2015-06-27', 'meena.reddy12@indmfg.in', '9326541099', 9, 2, 3, 3, 1, 'Active'),
  (13, 'Gaurav', 'Desai', 'M', '1993-06-10', '2021-01-21', 'gaurav.desai13@indmfg.in', '9328306011', 10, 2, 1, 1, 1, 'Active'),
  (14, 'Nitin', 'Mishra', 'M', '1997-05-26', '2025-03-13', 'nitin.mishra14@indmfg.in', '9524806516', 1, 4, 2, 2, 4, 'Active'),
  (15, 'Anjali', 'Rao', 'F', '1996-11-01', '2024-04-16', 'anjali.rao15@indmfg.in', '9253407200', 1, 4, 3, 3, 4, 'Active'),
  (16, 'Shweta', 'Patel', 'F', '1979-11-18', '2017-02-17', 'shweta.patel16@indmfg.in', '9364814270', 1, 4, 1, 1, 4, 'Active'),
  (17, 'Aarti', 'Yadav', 'F', '2001-06-01', '2019-11-23', 'aarti.yadav17@indmfg.in', '9902099969', 1, 7, 2, 2, 4, 'Active'),
  (18, 'Lakshmi', 'Chauhan', 'F', '1994-03-22', '2023-08-11', 'lakshmi.chauhan18@indmfg.in', '9726563708', 1, 7, 3, 3, 4, 'Active'),
  (19, 'Pradeep', 'Verma', 'M', '1985-11-30', '2022-02-13', 'pradeep.verma19@indmfg.in', '9335493870', 1, 7, 1, 1, 4, 'Active'),
  (20, 'Sneha', 'Joshi', 'F', '1974-03-16', '2025-06-06', 'sneha.joshi20@indmfg.in', '9629908599', 1, 7, 2, 2, 4, 'Active'),
  (21, 'Naveen', 'Kapoor', 'M', '1972-01-29', '2015-01-21', 'naveen.kapoor21@indmfg.in', '9217734861', 1, 7, 3, 3, 4, 'Active'),
  (22, 'Divya', 'Biswal', 'F', '1974-11-09', '2017-08-03', 'divya.biswal22@indmfg.in', '9950488739', 1, 7, 1, 1, 4, 'Resigned'),
  (23, 'Bhavna', 'Das', 'F', '1998-07-11', '2023-06-21', 'bhavna.das23@indmfg.in', '9740389325', 2, 3, 2, 2, 5, 'Active'),
  (24, 'Sunita', 'Bose', 'F', '1970-11-06', '2022-08-19', 'sunita.bose24@indmfg.in', '9509760584', 2, 3, 3, 3, 5, 'Active'),
  (25, 'Tarun', 'Pillai', 'M', '1994-09-24', '2024-07-01', 'tarun.pillai25@indmfg.in', '9668132202', 2, 5, 1, 1, 5, 'On Leave'),
  (26, 'Ganesh', 'Singh', 'M', '1979-04-12', '2014-04-05', 'ganesh.singh26@indmfg.in', '9830448745', 2, 5, 2, 2, 5, 'Active'),
  (27, 'Komal', 'Iyer', 'F', '2000-04-30', '2016-07-27', 'komal.iyer27@indmfg.in', '9831980933', 2, 6, 3, 3, 5, 'Active'),
  (28, 'Nisha', 'Gupta', 'F', '1992-02-01', '2019-12-26', 'nisha.gupta28@indmfg.in', '9925276600', 2, 6, 1, 1, 5, 'Active'),
  (29, 'Rekha', 'Saxena', 'F', '1996-10-01', '2021-08-18', 'rekha.saxena29@indmfg.in', '9219778234', 3, 3, 2, 2, 6, 'Active'),
  (30, 'Rahul', 'Sharma', 'M', '1981-03-01', '2023-10-02', 'rahul.sharma30@indmfg.in', '9269820594', 3, 3, 3, 3, 6, 'On Leave'),
  (31, 'Vikram', 'Shah', 'M', '1988-05-08', '2014-01-27', 'vikram.shah31@indmfg.in', '9875340444', 3, 6, 1, 1, 6, 'Active'),
  (32, 'Pooja', 'Menon', 'F', '2000-04-13', '2019-11-28', 'pooja.menon32@indmfg.in', '9637500247', 3, 6, 2, 2, 6, 'Active'),
  (33, 'Arjun', 'Nair', 'M', '2002-03-06', '2018-01-03', 'arjun.nair33@indmfg.in', '9645119047', 4, 8, 3, 3, 7, 'Active'),
  (34, 'Ritu', 'Mehta', 'F', '1972-10-09', '2020-09-10', 'ritu.mehta34@indmfg.in', '9786066793', 4, 8, 1, 1, 7, 'Active'),
  (35, 'Karthik', 'Kulkarni', 'M', '1990-10-09', '2018-06-18', 'karthik.kulkarni35@indmfg.in', '9264109919', 5, 8, 2, 2, 8, 'Active'),
  (36, 'Deepak', 'Reddy', 'M', '1984-10-09', '2017-08-16', 'deepak.reddy36@indmfg.in', '9679153816', 5, 8, 3, 3, 8, 'Active'),
  (37, 'Harish', 'Desai', 'M', '2002-12-05', '2025-11-23', 'harish.desai37@indmfg.in', '9100614068', 6, 8, 1, 1, 9, 'Active'),
  (38, 'Vivek', 'Mishra', 'M', '1994-11-13', '2021-04-09', 'vivek.mishra38@indmfg.in', '9624636385', 6, 8, 2, 2, 9, 'Active'),
  (39, 'Mohan', 'Rao', 'M', '1968-11-15', '2016-07-05', 'mohan.rao39@indmfg.in', '9489747629', 7, 8, 3, 3, 10, 'Active'),
  (40, 'Amit', 'Patel', 'M', '1981-10-17', '2019-05-16', 'amit.patel40@indmfg.in', '9162196678', 8, 9, 1, 1, 11, 'Active'),
  (41, 'Manoj', 'Yadav', 'M', '1978-10-21', '2015-10-08', 'manoj.yadav41@indmfg.in', '9191969690', 8, 9, 2, 2, 11, 'Active'),
  (42, 'Jyoti', 'Chauhan', 'F', '2000-10-30', '2024-11-25', 'jyoti.chauhan42@indmfg.in', '9976198296', 9, 3, 3, 3, 12, 'Active'),
  (43, 'Sanjay', 'Verma', 'M', '1971-02-07', '2025-12-12', 'sanjay.verma43@indmfg.in', '9922308461', 10, 10, 1, 1, 13, 'Active');

-- attendance (25 rows)
INSERT INTO attendance (emp_id, att_date, check_in, check_out, status) VALUES
  (1, '2026-09-14', '06:21:00', '14:30:00', 'Present'),
  (1, '2026-09-15', '06:05:00', '10:08:00', 'Half Day'),
  (1, '2026-09-16', '06:19:00', '14:27:00', 'Present'),
  (1, '2026-09-17', NULL, NULL, 'Leave'),
  (1, '2026-09-18', '06:06:00', '10:22:00', 'Half Day'),
  (1, '2026-09-21', '06:21:00', '14:23:00', 'Present'),
  (1, '2026-09-22', '06:16:00', '14:28:00', 'Present'),
  (1, '2026-09-23', '06:07:00', '14:04:00', 'Present'),
  (1, '2026-09-24', '06:18:00', '14:35:00', 'Present'),
  (1, '2026-09-25', '06:07:00', '14:00:00', 'Present'),
  (2, '2026-09-14', '09:20:00', '17:03:00', 'Present'),
  (2, '2026-09-15', '09:01:00', '17:21:00', 'Present'),
  (2, '2026-09-16', '09:07:00', '17:17:00', 'Present'),
  (2, '2026-09-17', '09:06:00', '17:34:00', 'Present'),
  (2, '2026-09-18', '09:18:00', '17:36:00', 'Present'),
  (2, '2026-09-21', '09:25:00', '17:30:00', 'Present'),
  (2, '2026-09-22', '09:06:00', '17:06:00', 'Present'),
  (2, '2026-09-23', '09:13:00', '17:22:00', 'Present'),
  (2, '2026-09-24', '09:14:00', '17:03:00', 'Present'),
  (2, '2026-09-25', '09:20:00', '17:06:00', 'Present'),
  (3, '2026-09-14', '22:23:00', '06:21:00', 'Present'),
  (3, '2026-09-15', '22:03:00', '06:15:00', 'Present'),
  (3, '2026-09-16', '22:17:00', '06:28:00', 'Present'),
  (3, '2026-09-17', '22:05:00', '06:17:00', 'Present'),
  (3, '2026-09-18', '22:02:00', '06:28:00', 'Present');

-- leave_request (14 rows)
INSERT INTO leave_request (emp_id, leave_type, from_date, to_date, status, approved_by) VALUES
  (22, 'Casual', '2026-09-25', '2026-09-28', 'Approved', 4),
  (21, 'Earned', '2026-09-18', '2026-09-20', 'Pending', 4),
  (10, 'Casual', '2026-09-30', '2026-09-30', 'Approved', 1),
  (6, 'Casual', '2026-09-19', '2026-09-21', 'Approved', 1),
  (36, 'Unpaid', '2026-06-16', '2026-06-20', 'Pending', 8),
  (43, 'Casual', '2026-07-02', '2026-07-04', 'Rejected', 13),
  (4, 'Unpaid', '2026-08-13', '2026-08-15', 'Pending', 1),
  (7, 'Sick', '2026-10-08', '2026-10-09', 'Rejected', 1),
  (15, 'Earned', '2026-06-28', '2026-07-02', 'Pending', 4),
  (8, 'Unpaid', '2026-08-11', '2026-08-12', 'Rejected', 1),
  (36, 'Unpaid', '2026-06-07', '2026-06-09', 'Approved', 8),
  (12, 'Earned', '2026-08-09', '2026-08-11', 'Pending', 1),
  (1, 'Sick', '2026-07-17', '2026-07-21', 'Rejected', 1),
  (5, 'Casual', '2026-07-07', '2026-07-07', 'Approved', 1);

-- payroll (25 rows)
INSERT INTO payroll (emp_id, pay_month, basic, allowances, overtime_pay, deductions) VALUES
  (1, '2026-08', 120000.0, 48000.0, 3081.0, 20160.0),
  (1, '2026-09', 120000.0, 48000.0, 3439.0, 20160.0),
  (2, '2026-08', 120000.0, 48000.0, 3716.0, 20160.0),
  (2, '2026-09', 120000.0, 48000.0, 2792.0, 20160.0),
  (3, '2026-08', 120000.0, 48000.0, 1289.0, 20160.0),
  (3, '2026-09', 120000.0, 48000.0, 3031.0, 20160.0),
  (4, '2026-08', 85000.0, 34000.0, 2552.0, 14280.0),
  (4, '2026-09', 85000.0, 34000.0, 5911.0, 14280.0),
  (5, '2026-08', 85000.0, 34000.0, 2657.0, 14280.0),
  (5, '2026-09', 85000.0, 34000.0, 4649.0, 14280.0),
  (6, '2026-08', 85000.0, 34000.0, 1221.0, 14280.0),
  (6, '2026-09', 85000.0, 34000.0, 173.0, 14280.0),
  (7, '2026-08', 85000.0, 34000.0, 107.0, 14280.0),
  (7, '2026-09', 85000.0, 34000.0, 318.0, 14280.0),
  (8, '2026-08', 85000.0, 34000.0, 322.0, 14280.0),
  (8, '2026-09', 85000.0, 34000.0, 1265.0, 14280.0),
  (9, '2026-08', 85000.0, 34000.0, 101.0, 14280.0),
  (9, '2026-09', 85000.0, 34000.0, 1380.0, 14280.0),
  (10, '2026-08', 85000.0, 34000.0, 167.0, 14280.0),
  (10, '2026-09', 85000.0, 34000.0, 557.0, 14280.0),
  (11, '2026-08', 85000.0, 34000.0, 907.0, 14280.0),
  (11, '2026-09', 85000.0, 34000.0, 1355.0, 14280.0),
  (12, '2026-08', 85000.0, 34000.0, 868.0, 14280.0),
  (12, '2026-09', 85000.0, 34000.0, 994.0, 14280.0),
  (13, '2026-08', 85000.0, 34000.0, 1243.0, 14280.0);

-- training_program (6 rows)
INSERT INTO training_program (title, duration_hours, mandatory) VALUES
  ('Fire Safety & Evacuation', 4, 1),
  ('Machine Guarding & LOTO', 6, 1),
  ('First Aid Basics', 4, 1),
  ('Six Sigma Yellow Belt', 16, 0),
  ('CNC Programming Basics', 24, 0),
  ('Hazardous Material Handling', 8, 1);

-- employee_training (25 rows)
INSERT INTO employee_training (emp_id, training_id, completion_date, score) VALUES
  (1, 4, '2025-01-22', 75.0),
  (2, 5, '2026-04-17', 78.0),
  (3, 4, '2025-04-19', 70.0),
  (4, 5, '2026-06-21', 60.0),
  (4, 4, '2026-02-10', 74.0),
  (5, 3, '2025-06-22', 59.0),
  (5, 2, '2026-06-07', 95.0),
  (5, 6, '2025-04-27', 88.0),
  (6, 2, '2025-06-01', 70.0),
  (6, 3, '2025-04-16', 64.0),
  (6, 5, '2025-09-20', 67.0),
  (7, 5, '2025-06-06', 96.0),
  (8, 2, '2026-05-21', 84.0),
  (9, 5, '2025-11-20', 64.0),
  (9, 4, '2026-03-27', 59.0),
  (9, 3, '2026-04-26', 83.0),
  (10, 3, '2025-12-27', 87.0),
  (10, 6, '2025-03-17', 74.0),
  (10, 1, '2026-04-18', 83.0),
  (11, 1, '2026-01-13', 73.0),
  (12, 6, '2025-04-03', 94.0),
  (13, 5, '2026-08-18', 90.0),
  (13, 4, '2025-02-12', 83.0),
  (13, 6, '2026-08-09', 96.0),
  (14, 3, '2026-09-12', 85.0);

-- contractor (5 rows)
INSERT INTO contractor (company_name, service_type, phone, plant_id, contract_start, contract_end, contract_value) VALUES
  ('SafeGuard Security Services', 'Security', '9811100001', 1, '2025-04-01', '2027-03-31', 2400000),
  ('CleanSpace Facility Mgmt', 'Housekeeping', '9811100002', 1, '2025-07-01', '2026-06-30', 1200000),
  ('Voltex Electricals', 'Electrical Works', '9811100003', 2, '2026-01-01', '2026-12-31', 1850000),
  ('BuildRight Civil Works', 'Civil Construction', '9811100004', 3, '2026-03-01', '2027-02-28', 5600000),
  ('GreenCanteen Caterers', 'Canteen', '9811100005', 2, '2025-10-01', '2026-09-30', 900000);

-- production_line (8 rows)
INSERT INTO production_line (plant_id, line_name, hourly_output, status) VALUES
  (1, 'Assembly Line A', 40, 'Running'),
  (1, 'Machining Line B', 30, 'Running'),
  (1, 'Fabrication Line C', 25, 'Idle'),
  (2, 'Assembly Line A', 35, 'Running'),
  (2, 'Casting Line B', 20, 'Running'),
  (2, 'Welding Line C', 28, 'Running'),
  (3, 'Assembly Line A', 45, 'Running'),
  (3, 'Finishing Line B', 38, 'Shutdown');

-- machine (22 rows)
INSERT INTO machine (line_id, machine_name, model_no, manufacturer, purchase_date, purchase_cost, status) VALUES
  (1, 'CNC Lathe', 'QT-200', 'Mazak', '2016-09-14', 1900000.0, 'Operational'),
  (2, 'Hydraulic Press 200T', 'HP-200', 'Hindustan Hydraulics', '2021-08-09', 7300000.0, 'Operational'),
  (3, 'Welding Robot', 'IRB-1520', 'ABB', '2016-03-08', 7000000.0, 'Operational'),
  (4, 'Vertical Milling Machine', 'VM-4', 'HMT', '2022-06-13', 3500000.0, 'Under Maintenance'),
  (5, 'Surface Grinder', 'SG-60', 'Bhavya', '2024-01-22', 2300000.0, 'Operational'),
  (6, 'Industrial Furnace', 'IF-900', 'Thermax', '2019-04-08', 5900000.0, 'Operational'),
  (7, 'Powder Coating Booth', 'PC-12', 'Nordson', '2016-01-07', 5000000.0, 'Operational'),
  (8, 'Conveyor Drive Unit', 'CD-7', 'Siemens', '2018-09-12', 7800000.0, 'Operational'),
  (1, 'Radial Drilling Machine', 'RD-50', 'Kirloskar', '2020-09-02', 2800000.0, 'Operational'),
  (2, 'Plasma Cutter', 'PL-80', 'Lincoln', '2023-06-14', 1500000.0, 'Operational'),
  (3, 'Packaging Machine', 'PK-3', 'Bosch', '2021-11-14', 5100000.0, 'Operational'),
  (4, 'Screw Air Compressor', 'GA-37', 'Atlas Copco', '2016-10-04', 7200000.0, 'Operational'),
  (5, 'CNC Milling Centre', 'DMU-50', 'DMG Mori', '2022-02-06', 6900000.0, 'Under Maintenance'),
  (6, 'Shot Blasting Unit', 'SB-9', 'Pangborn', '2024-10-26', 6900000.0, 'Operational'),
  (7, 'Overhead Crane 10T', 'OC-10', 'Kone', '2022-03-19', 700000.0, 'Operational'),
  (8, 'Tube Bending Machine', 'TB-40', 'Pedax', '2020-05-18', 6500000.0, 'Operational'),
  (1, 'Heat Treatment Oven', 'HT-5', 'Thermax', '2016-06-27', 5400000.0, 'Operational'),
  (2, 'Spot Welding Machine', 'SW-22', 'ESAB', '2020-03-09', 3700000.0, 'Decommissioned'),
  (3, 'Hydraulic Shear', 'HS-12', 'Amada', '2024-05-19', 700000.0, 'Operational'),
  (4, 'Balancing Machine', 'BM-6', 'Schenck', '2020-01-02', 1300000.0, 'Operational'),
  (5, 'Test Bench Dynamometer', 'TB-1', 'AVL', '2019-11-13', 3500000.0, 'Operational'),
  (6, 'Palletizer', 'PL-2', 'Fanuc', '2024-03-21', 1800000.0, 'Operational');

-- spare_part (12 rows)
INSERT INTO spare_part (part_name, unit_cost, stock_qty, reorder_level) VALUES
  ('Bearing 6205', 450, 40, 20),
  ('V-Drive Belt B-68', 380, 25, 10),
  ('Hydraulic Seal Kit', 2200, 12, 6),
  ('Coolant Pump 0.5HP', 5400, 4, 5),
  ('Proximity Sensor M18', 1250, 18, 8),
  ('Contactor 40A', 1800, 10, 6),
  ('Carbide Insert Set', 3200, 30, 15),
  ('Gear Pinion 22T', 4100, 6, 4),
  ('Solenoid Valve 24V', 2600, 9, 6),
  ('Air Filter Element', 950, 22, 10),
  ('Servo Motor Brush Set', 700, 3, 6),
  ('Thermocouple K-Type', 820, 15, 8);

-- maintenance_log (30 rows)
INSERT INTO maintenance_log (machine_id, technician_id, maint_type, maint_date, downtime_hours, labour_cost, description) VALUES
  (19, 28, 'Corrective', '2026-03-14', 1.3, 4200.0, 'Corrective service on machine 19'),
  (16, 28, 'Preventive', '2026-03-15', 1.6, 5700.0, 'Preventive service on machine 16'),
  (7, 23, 'Corrective', '2026-06-12', 9.5, 6000.0, 'Corrective service on machine 7'),
  (10, 25, 'Preventive', '2026-06-23', 5.3, 5900.0, 'Preventive service on machine 10'),
  (13, 25, 'Preventive', '2026-09-16', 12.6, 3100.0, 'Preventive service on machine 13'),
  (20, 24, 'Corrective', '2026-06-18', 2.3, 2800.0, 'Corrective service on machine 20'),
  (14, 24, 'Breakdown', '2026-07-03', 8.3, 5400.0, 'Breakdown service on machine 14'),
  (2, 23, 'Breakdown', '2026-07-10', 9.5, 5500.0, 'Breakdown service on machine 2'),
  (20, 28, 'Breakdown', '2026-01-22', 2.7, 2300.0, 'Breakdown service on machine 20'),
  (20, 24, 'Breakdown', '2026-07-13', 7.0, 5600.0, 'Breakdown service on machine 20'),
  (17, 25, 'Breakdown', '2026-06-14', 11.9, 2300.0, 'Breakdown service on machine 17'),
  (6, 26, 'Breakdown', '2026-03-06', 5.1, 2400.0, 'Breakdown service on machine 6'),
  (16, 24, 'Preventive', '2026-03-15', 15.4, 3600.0, 'Preventive service on machine 16'),
  (3, 28, 'Corrective', '2026-07-01', 12.9, 4300.0, 'Corrective service on machine 3'),
  (21, 24, 'Corrective', '2026-09-22', 4.4, 2000.0, 'Corrective service on machine 21'),
  (1, 25, 'Corrective', '2026-09-01', 14.6, 4300.0, 'Corrective service on machine 1'),
  (3, 25, 'Breakdown', '2026-09-07', 9.7, 5700.0, 'Breakdown service on machine 3'),
  (21, 23, 'Breakdown', '2026-08-31', 12.4, 3000.0, 'Breakdown service on machine 21'),
  (19, 24, 'Preventive', '2026-08-14', 9.3, 1100.0, 'Preventive service on machine 19'),
  (10, 24, 'Preventive', '2026-06-19', 16.9, 2200.0, 'Preventive service on machine 10'),
  (7, 28, 'Corrective', '2026-03-10', 11.4, 4700.0, 'Corrective service on machine 7'),
  (6, 27, 'Preventive', '2026-04-02', 12.0, 5400.0, 'Preventive service on machine 6'),
  (2, 28, 'Corrective', '2026-04-22', 13.9, 3400.0, 'Corrective service on machine 2'),
  (18, 25, 'Corrective', '2026-09-09', 4.8, 5900.0, 'Corrective service on machine 18'),
  (2, 24, 'Breakdown', '2026-08-05', 9.0, 2100.0, 'Breakdown service on machine 2'),
  (11, 28, 'Corrective', '2026-07-28', 2.8, 3100.0, 'Corrective service on machine 11'),
  (15, 23, 'Corrective', '2026-08-23', 4.8, 1400.0, 'Corrective service on machine 15'),
  (20, 26, 'Preventive', '2026-03-27', 14.7, 4400.0, 'Preventive service on machine 20'),
  (15, 26, 'Corrective', '2026-09-16', 0.9, 3300.0, 'Corrective service on machine 15'),
  (2, 23, 'Corrective', '2026-09-15', 11.0, 4900.0, 'Corrective service on machine 2');

-- maintenance_part_usage (25 rows)
INSERT INTO maintenance_part_usage (maint_id, part_id, qty_used) VALUES
  (1, 11, 3),
  (1, 3, 2),
  (2, 1, 1),
  (2, 5, 1),
  (3, 5, 3),
  (3, 1, 1),
  (4, 7, 3),
  (4, 12, 2),
  (5, 2, 1),
  (5, 7, 2),
  (6, 5, 3),
  (6, 8, 3),
  (7, 10, 2),
  (7, 7, 3),
  (8, 1, 1),
  (9, 6, 3),
  (9, 7, 1),
  (10, 10, 1),
  (11, 1, 3),
  (12, 4, 2),
  (12, 10, 3),
  (13, 12, 2),
  (14, 5, 3),
  (14, 3, 3),
  (15, 4, 2);

-- energy_consumption (25 rows)
INSERT INTO energy_consumption (plant_id, record_date, units_kwh, cost) VALUES
  (1, '2026-09-01', 13171.13, 111954.6),
  (1, '2026-09-02', 18589.47, 158010.49),
  (1, '2026-09-03', 16730.1, 142205.85),
  (1, '2026-09-04', 17166.28, 145913.38),
  (1, '2026-09-05', 12391.25, 105325.62),
  (1, '2026-09-06', 12806.98, 108859.33),
  (1, '2026-09-07', 14843.38, 126168.73),
  (1, '2026-09-08', 10335.86, 87854.81),
  (1, '2026-09-09', 14638.68, 124428.78),
  (1, '2026-09-10', 8882.38, 75500.23),
  (1, '2026-09-11', 14051.07, 119434.1),
  (1, '2026-09-12', 17172.85, 145969.22),
  (1, '2026-09-13', 11356.65, 96531.52),
  (1, '2026-09-14', 19869.09, 168887.27),
  (1, '2026-09-15', 16164.78, 137400.63),
  (1, '2026-09-16', 9425.73, 80118.71),
  (1, '2026-09-17', 19700.99, 167458.42),
  (1, '2026-09-18', 12726.84, 108178.14),
  (1, '2026-09-19', 17538.77, 149079.55),
  (1, '2026-09-20', 12069.02, 102586.67),
  (1, '2026-09-21', 19267.38, 163772.73),
  (1, '2026-09-22', 17059.58, 145006.43),
  (1, '2026-09-23', 10388.69, 88303.87),
  (1, '2026-09-24', 14109.47, 119930.49),
  (1, '2026-09-25', 14000.93, 119007.9);

-- safety_incident (10 rows)
INSERT INTO safety_incident (plant_id, emp_id, incident_date, severity, description, action_taken) VALUES
  (3, 18, '2026-09-14', 'Minor', 'Hand abrasion while handling steel sheet', 'First aid; gloves upgraded'),
  (1, 17, '2026-06-01', 'Minor', 'Slip on oil patch near press', 'Area cleaned; anti-skid mats placed'),
  (3, 19, '2026-05-05', 'Moderate', 'Hydraulic hose burst, no injury', 'Hose replaced; inspection schedule revised'),
  (2, 19, '2026-04-26', 'Moderate', 'Minor electrical short in panel', 'Panel isolated and repaired'),
  (3, 25, '2026-06-21', 'Major', 'Crane load swing hit barricade', 'Crane operator retrained; limit switch fixed'),
  (2, 21, '2026-05-30', 'Minor', 'Eye irritation from grinding dust', 'Face shields made mandatory'),
  (2, 4, '2026-07-23', 'Critical', 'Fire in paint booth exhaust duct', 'Plant drill held; duct fire-proofed'),
  (2, 21, '2026-03-25', 'Moderate', 'Forklift near-miss with pedestrian', 'Pedestrian lanes repainted'),
  (2, 2, '2026-06-06', 'Minor', 'Burn from hot workpiece', 'First aid; tongs provided'),
  (3, 3, '2026-07-06', 'Major', 'Fall from maintenance platform', 'Guard rails added; harness policy enforced');

-- supplier (10 rows)
INSERT INTO supplier (supplier_name, contact_person, phone, email, city, rating, gstin) VALUES
  ('Tata Steel Distributors', 'R. Banerjee', '9800010000', 'sales@tata.in', 'Kolkata', 4.6, '27AAAPL1000C1Z0'),
  ('JSW Metals Ltd', 'S. Pawar', '9800010001', 'sales@jsw.in', 'Mumbai', 4.3, '27AAAPL1001C1Z1'),
  ('Havells Wires & Cables', 'A. Khanna', '9800010002', 'sales@havells.in', 'Delhi', 4.5, '27AAAPL1002C1Z2'),
  ('Hindalco Aluminium Supply', 'M. Sinha', '9800010003', 'sales@hindalco.in', 'Ranchi', 4.1, '27AAAPL1003C1Z3'),
  ('Castrol Industrial Lubes', 'P. Rao', '9800010004', 'sales@castrol.in', 'Chennai', 4.4, '27AAAPL1004C1Z4'),
  ('Asian Paints Industrial', 'V. Gokhale', '9800010005', 'sales@asian.in', 'Pune', 4.2, '27AAAPL1005C1Z5'),
  ('SKF Bearings India', 'T. Iyer', '9800010006', 'sales@skf.in', 'Bengaluru', 4.8, '27AAAPL1006C1Z6'),
  ('Elcon Electronics', 'N. Gupta', '9800010007', 'sales@elcon.in', 'Noida', 3.9, '27AAAPL1007C1Z7'),
  ('Supreme Packaging', 'K. Shah', '9800010008', 'sales@supreme.in', 'Ahmedabad', 3.7, '27AAAPL1008C1Z8'),
  ('Fastener World', 'D. Malhotra', '9800010009', 'sales@fastener.in', 'Ludhiana', 4.0, '27AAAPL1009C1Z9');

-- material_category (5 rows)
INSERT INTO material_category (cat_name) VALUES
  ('Metals'),
  ('Electrical & Electronics'),
  ('Consumables'),
  ('Chemicals & Paints'),
  ('Packaging & Fasteners');

-- raw_material (15 rows)
INSERT INTO raw_material (material_name, cat_id, unit, unit_cost, reorder_level) VALUES
  ('Mild Steel Sheet 2mm', 1, 'kg', 68.0, 2000),
  ('Stainless Steel Rod 304', 1, 'kg', 245.0, 800),
  ('Aluminium Ingot', 1, 'kg', 210.0, 600),
  ('Cast Iron Billet', 1, 'kg', 58.0, 1500),
  ('Copper Winding Wire', 2, 'kg', 780.0, 400),
  ('Electronic PCB Controller', 2, 'piece', 1450.0, 60),
  ('Insulation Tape Roll', 2, 'piece', 85.0, 200),
  ('Rubber Gasket Set', 3, 'piece', 120.0, 300),
  ('Hydraulic Oil ISO 68', 3, 'litre', 190.0, 500),
  ('Lithium Grease', 3, 'kg', 320.0, 150),
  ('Welding Electrode E6013', 3, 'kg', 140.0, 400),
  ('Paint Primer', 4, 'litre', 260.0, 250),
  ('Bearing Steel Balls', 1, 'kg', 410.0, 200),
  ('Hex Bolts & Nuts Assorted', 5, 'kg', 95.0, 500),
  ('Corrugated Carton Box', 5, 'piece', 38.0, 1000);

-- supplier_material (41 rows)
INSERT INTO supplier_material (supplier_id, material_id, quoted_price, lead_time_days) VALUES
  (1, 15, 39.88, 6),
  (1, 8, 112.85, 11),
  (1, 11, 133.59, 11),
  (1, 5, 810.6, 5),
  (2, 2, 232.22, 3),
  (2, 10, 330.87, 11),
  (2, 12, 245.73, 6),
  (2, 14, 86.5, 9),
  (3, 6, 1502.55, 5),
  (3, 12, 234.27, 11),
  (3, 8, 131.47, 5),
  (3, 2, 240.44, 10),
  (4, 8, 115.71, 13),
  (4, 11, 127.66, 15),
  (4, 4, 53.24, 12),
  (4, 5, 738.23, 14),
  (5, 12, 255.73, 5),
  (5, 14, 103.24, 3),
  (5, 1, 74.76, 9),
  (5, 3, 222.09, 5),
  (6, 15, 34.27, 12),
  (6, 12, 265.34, 8),
  (6, 5, 746.39, 13),
  (6, 1, 68.59, 10),
  (7, 15, 36.27, 15),
  (7, 3, 193.73, 5),
  (7, 9, 198.78, 13),
  (7, 8, 114.17, 5),
  (8, 1, 68.92, 15),
  (8, 12, 244.04, 12),
  (8, 6, 1553.28, 9),
  (8, 5, 831.01, 11),
  (9, 6, 1332.66, 5),
  (9, 2, 243.9, 6),
  (9, 7, 76.62, 9),
  (9, 11, 132.59, 15),
  (10, 5, 814.77, 3),
  (10, 6, 1380.85, 8),
  (10, 15, 39.47, 3),
  (10, 10, 330.74, 10),
  (5, 13, 410.0, 7);

-- warehouse (4 rows)
INSERT INTO warehouse (plant_id, warehouse_name, capacity_units, manager_id) VALUES
  (1, 'Pune Main Store', 50000, 7),
  (1, 'Pune Annex Store', 20000, 33),
  (2, 'Jaipur Main Store', 40000, 7),
  (3, 'Chennai Main Store', 35000, 33);

-- inventory (60 rows)
INSERT INTO inventory (warehouse_id, material_id, quantity, last_updated) VALUES
  (1, 1, 1163.0, '2026-10-03'),
  (1, 2, 2078.0, '2026-09-20'),
  (1, 3, 87.0, '2026-09-19'),
  (1, 4, 1695.0, '2026-09-15'),
  (1, 5, 295.0, '2026-09-09'),
  (1, 6, 6.0, '2026-09-30'),
  (1, 7, 427.0, '2026-09-24'),
  (1, 8, 377.0, '2026-10-06'),
  (1, 9, 1285.0, '2026-10-05'),
  (1, 10, 25.0, '2026-09-14'),
  (1, 11, 869.0, '2026-09-06'),
  (1, 12, 29.0, '2026-10-04'),
  (1, 13, 404.0, '2026-09-05'),
  (1, 14, 1291.0, '2026-09-08'),
  (1, 15, 323.0, '2026-10-06'),
  (2, 1, 5087.0, '2026-09-13'),
  (2, 2, 1338.0, '2026-09-10'),
  (2, 3, 30.0, '2026-10-04'),
  (2, 4, 1990.0, '2026-09-14'),
  (2, 5, 821.0, '2026-10-02'),
  (2, 6, 2.0, '2026-10-03'),
  (2, 7, 267.0, '2026-09-04'),
  (2, 8, 407.0, '2026-10-03'),
  (2, 9, 628.0, '2026-10-07'),
  (2, 10, 5.0, '2026-09-30'),
  (2, 11, 774.0, '2026-09-20'),
  (2, 12, 38.0, '2026-09-26'),
  (2, 13, 166.0, '2026-09-01'),
  (2, 14, 1064.0, '2026-09-05'),
  (2, 15, 280.0, '2026-09-23'),
  (3, 1, 4018.0, '2026-10-05'),
  (3, 2, 2228.0, '2026-09-05'),
  (3, 3, 119.0, '2026-10-01'),
  (3, 4, 366.0, '2026-09-27'),
  (3, 5, 254.0, '2026-09-09'),
  (3, 6, 9.0, '2026-09-27'),
  (3, 7, 230.0, '2026-09-25'),
  (3, 8, 403.0, '2026-09-25'),
  (3, 9, 576.0, '2026-10-05'),
  (3, 10, 7.0, '2026-09-23'),
  (3, 11, 189.0, '2026-10-05'),
  (3, 12, 23.0, '2026-09-09'),
  (3, 13, 416.0, '2026-09-15'),
  (3, 14, 1182.0, '2026-09-02'),
  (3, 15, 2743.0, '2026-09-30'),
  (4, 1, 3876.0, '2026-10-05'),
  (4, 2, 1024.0, '2026-09-25'),
  (4, 3, 101.0, '2026-09-16'),
  (4, 4, 2064.0, '2026-09-10'),
  (4, 5, 355.0, '2026-09-08'),
  (4, 6, 2.0, '2026-09-27'),
  (4, 7, 357.0, '2026-09-02'),
  (4, 8, 239.0, '2026-09-05'),
  (4, 9, 213.0, '2026-09-03'),
  (4, 10, 15.0, '2026-09-04'),
  (4, 11, 322.0, '2026-09-03'),
  (4, 12, 23.0, '2026-09-15'),
  (4, 13, 317.0, '2026-09-04'),
  (4, 14, 265.0, '2026-09-19'),
  (4, 15, 778.0, '2026-10-07');

-- purchase_order (14 rows)
INSERT INTO purchase_order (supplier_id, created_by, order_date, expected_date, status) VALUES
  (6, 36, '2026-09-25', '2026-10-09', 'Placed'),
  (5, 36, '2026-07-10', '2026-07-15', 'Received'),
  (6, 8, '2026-08-15', '2026-08-26', 'Received'),
  (3, 36, '2026-08-27', '2026-09-01', 'Received'),
  (4, 8, '2026-07-24', '2026-08-08', 'Received'),
  (10, 8, '2026-10-02', '2026-10-07', 'Placed'),
  (5, 8, '2026-05-04', '2026-05-12', 'Cancelled'),
  (5, 36, '2026-08-06', '2026-08-18', 'Received'),
  (8, 35, '2026-07-21', '2026-07-29', 'Received'),
  (9, 36, '2026-09-27', '2026-10-11', 'Placed'),
  (10, 36, '2026-07-02', '2026-07-14', 'Received'),
  (3, 8, '2026-09-17', '2026-09-29', 'Placed'),
  (4, 8, '2026-05-18', '2026-05-25', 'Received'),
  (8, 36, '2026-05-05', '2026-05-10', 'Received');

-- purchase_order_item (25 rows)
INSERT INTO purchase_order_item (po_id, material_id, quantity, unit_price) VALUES
  (1, 12, 1750.0, 265.34),
  (1, 1, 800.0, 68.59),
  (1, 15, 1400.0, 34.27),
  (2, 14, 250.0, 103.24),
  (2, 3, 1200.0, 222.09),
  (2, 1, 1300.0, 74.76),
  (2, 13, 1750.0, 410.0),
  (3, 1, 850.0, 68.59),
  (3, 15, 800.0, 34.27),
  (3, 12, 1050.0, 265.34),
  (4, 2, 1300.0, 240.44),
  (4, 8, 850.0, 131.47),
  (4, 6, 900.0, 1502.55),
  (5, 4, 250.0, 53.24),
  (5, 8, 950.0, 115.71),
  (5, 5, 1300.0, 738.23),
  (6, 6, 750.0, 1380.85),
  (6, 10, 1150.0, 330.74),
  (7, 12, 1350.0, 255.73),
  (7, 3, 850.0, 222.09),
  (7, 13, 1850.0, 410.0),
  (7, 1, 1100.0, 74.76),
  (8, 1, 1700.0, 74.76),
  (8, 13, 1650.0, 410.0),
  (8, 14, 1550.0, 103.24);

-- product_category (4 rows)
INSERT INTO product_category (pcat_name) VALUES
  ('Power Transmission'),
  ('Hydraulics & Pneumatics'),
  ('Electrical Equipment'),
  ('Structural Components');

-- product (10 rows)
INSERT INTO product (product_name, sku, pcat_id, unit_price, standard_cost, stock_qty) VALUES
  ('Steel Gear Box GB-100', 'GB-100', 1, 45000.0, 31000.0, 467),
  ('Hydraulic Pump HP-200', 'HP-200', 2, 38000.0, 26500.0, 285),
  ('Industrial Gate Valve IV-50', 'IV-050', 2, 8500.0, 5600.0, 341),
  ('Electric Motor 5HP EM-05', 'EM-005', 3, 22000.0, 15200.0, 675),
  ('Bearing Set BS-10', 'BS-010', 1, 3200.0, 2000.0, 303),
  ('Conveyor Roller CR-30', 'CR-030', 4, 4500.0, 2900.0, 537),
  ('Pipe Fitting Kit PF-12', 'PF-012', 2, 1800.0, 1100.0, 415),
  ('Control Panel CP-09', 'CP-009', 3, 28000.0, 19500.0, 507),
  ('Air Compressor AC-07', 'AC-007', 2, 56000.0, 40000.0, 842),
  ('Welded Frame WF-04', 'WF-004', 4, 12500.0, 8200.0, 699);

-- bill_of_materials (40 rows)
INSERT INTO bill_of_materials (product_id, material_id, qty_required) VALUES
  (1, 4, 38.0),
  (1, 13, 0.8),
  (1, 9, 2.0),
  (1, 10, 0.5),
  (1, 14, 1.2),
  (2, 2, 12.0),
  (2, 8, 4.0),
  (2, 9, 3.5),
  (2, 3, 6.0),
  (3, 4, 5.0),
  (3, 8, 2.0),
  (3, 11, 0.3),
  (4, 5, 3.2),
  (4, 1, 10.0),
  (4, 7, 2.0),
  (4, 6, 1.0),
  (5, 13, 1.5),
  (5, 1, 0.6),
  (5, 10, 0.2),
  (6, 1, 6.0),
  (6, 13, 0.4),
  (6, 12, 0.3),
  (6, 14, 0.5),
  (7, 1, 1.2),
  (7, 8, 1.0),
  (7, 14, 0.2),
  (8, 1, 8.0),
  (8, 5, 2.5),
  (8, 6, 1.0),
  (8, 7, 3.0),
  (8, 12, 0.8),
  (9, 1, 55.0),
  (9, 3, 8.0),
  (9, 8, 6.0),
  (9, 9, 5.0),
  (9, 6, 1.0),
  (10, 1, 28.0),
  (10, 11, 1.5),
  (10, 12, 1.0),
  (10, 14, 1.5);

-- work_order (13 rows)
INSERT INTO work_order (product_id, line_id, supervisor_id, planned_qty, start_date, end_date, status) VALUES
  (1, 7, 14, 354, '2026-08-13', '2026-09-02', 'Completed'),
  (2, 3, 14, 398, '2026-05-13', '2026-05-31', 'Completed'),
  (3, 5, 15, 436, '2026-07-19', '2026-08-06', 'Completed'),
  (4, 2, 15, 791, '2026-08-10', '2026-08-26', 'Completed'),
  (5, 4, 16, 415, '2026-06-21', '2026-07-08', 'Completed'),
  (6, 2, 16, 739, '2026-06-08', '2026-06-28', 'Completed'),
  (7, 2, 15, 494, '2026-07-23', '2026-08-02', 'Completed'),
  (8, 8, 14, 626, '2026-06-18', '2026-07-06', 'Completed'),
  (9, 2, 15, 732, '2026-05-02', '2026-05-14', 'Completed'),
  (10, 6, 16, 800, '2026-06-25', '2026-07-15', 'Completed'),
  (1, 1, 14, 408, '2026-09-08', NULL, 'In Progress'),
  (9, 8, 15, 323, '2026-09-23', NULL, 'In Progress'),
  (4, 5, 16, 588, '2026-10-15', NULL, 'Planned');

-- production_batch (34 rows)
INSERT INTO production_batch (wo_id, shift_id, batch_no, batch_date, produced_qty, rejected_qty) VALUES
  (1, 1, 'B-2026-0001', '2026-08-19', 100, 1),
  (1, 1, 'B-2026-0002', '2026-08-20', 111, 4),
  (1, 3, 'B-2026-0003', '2026-08-17', 117, 2),
  (2, 2, 'B-2026-0004', '2026-05-22', 136, 5),
  (2, 3, 'B-2026-0005', '2026-05-16', 136, 6),
  (2, 1, 'B-2026-0006', '2026-05-19', 117, 4),
  (3, 2, 'B-2026-0007', '2026-07-21', 131, 4),
  (3, 2, 'B-2026-0008', '2026-07-26', 163, 7),
  (3, 2, 'B-2026-0009', '2026-07-27', 126, 5),
  (4, 2, 'B-2026-0010', '2026-08-17', 257, 7),
  (4, 1, 'B-2026-0011', '2026-08-17', 254, 11),
  (4, 3, 'B-2026-0012', '2026-08-14', 282, 6),
  (5, 2, 'B-2026-0013', '2026-06-24', 122, 1),
  (5, 2, 'B-2026-0014', '2026-06-28', 158, 2),
  (5, 2, 'B-2026-0015', '2026-06-22', 141, 2),
  (6, 1, 'B-2026-0016', '2026-06-13', 254, 7),
  (6, 2, 'B-2026-0017', '2026-06-15', 234, 3),
  (6, 1, 'B-2026-0018', '2026-06-15', 261, 9),
  (7, 1, 'B-2026-0019', '2026-08-01', 145, 2),
  (7, 2, 'B-2026-0020', '2026-07-29', 182, 7),
  (7, 1, 'B-2026-0021', '2026-07-30', 144, 3),
  (8, 3, 'B-2026-0022', '2026-06-22', 223, 12),
  (8, 1, 'B-2026-0023', '2026-06-25', 221, 4),
  (8, 2, 'B-2026-0024', '2026-06-23', 196, 4),
  (9, 2, 'B-2026-0025', '2026-05-09', 233, 3),
  (9, 2, 'B-2026-0026', '2026-05-04', 250, 6),
  (9, 3, 'B-2026-0027', '2026-05-10', 240, 13),
  (10, 1, 'B-2026-0028', '2026-06-27', 285, 5),
  (10, 2, 'B-2026-0029', '2026-07-02', 265, 2),
  (10, 2, 'B-2026-0030', '2026-07-01', 285, 8),
  (11, 2, 'B-2026-0031', '2026-09-11', 137, 4),
  (11, 2, 'B-2026-0032', '2026-09-12', 136, 5),
  (12, 2, 'B-2026-0033', '2026-09-30', 107, 2),
  (12, 3, 'B-2026-0034', '2026-10-01', 105, 4);

-- defect_type (6 rows)
INSERT INTO defect_type (defect_name, severity) VALUES
  ('Dimensional deviation', 'Medium'),
  ('Surface scratch', 'Low'),
  ('Weld porosity', 'High'),
  ('Paint defect', 'Low'),
  ('Leakage under pressure test', 'High'),
  ('Assembly misalignment', 'Medium');

-- quality_inspection (27 rows)
INSERT INTO quality_inspection (batch_id, inspector_id, insp_date, sample_size, defects_found, result, remarks) VALUES
  (1, 31, '2026-08-20', 40, 3, 'Rework', 'Needs corrective action'),
  (3, 32, '2026-08-18', 20, 1, 'Pass', 'Within tolerance'),
  (5, 31, '2026-05-17', 50, 0, 'Pass', 'Within tolerance'),
  (7, 29, '2026-07-22', 50, 2, 'Pass', 'Within tolerance'),
  (8, 32, '2026-07-27', 50, 5, 'Fail', 'Needs corrective action'),
  (9, 32, '2026-07-28', 20, 4, 'Rework', 'Needs corrective action'),
  (10, 29, '2026-08-18', 20, 3, 'Rework', 'Needs corrective action'),
  (11, 30, '2026-08-18', 50, 4, 'Rework', 'Needs corrective action'),
  (12, 32, '2026-08-15', 30, 5, 'Fail', 'Needs corrective action'),
  (13, 6, '2026-06-25', 30, 0, 'Pass', 'Within tolerance'),
  (14, 31, '2026-06-29', 30, 3, 'Rework', 'Needs corrective action'),
  (15, 31, '2026-06-23', 50, 0, 'Pass', 'Within tolerance'),
  (16, 6, '2026-06-14', 20, 2, 'Pass', 'Within tolerance'),
  (17, 32, '2026-06-16', 30, 2, 'Pass', 'Within tolerance'),
  (19, 29, '2026-08-02', 50, 1, 'Pass', 'Within tolerance'),
  (20, 31, '2026-07-30', 50, 2, 'Pass', 'Within tolerance'),
  (21, 30, '2026-07-31', 20, 2, 'Pass', 'Within tolerance'),
  (22, 6, '2026-06-23', 40, 5, 'Fail', 'Needs corrective action'),
  (23, 32, '2026-06-26', 20, 4, 'Rework', 'Needs corrective action'),
  (25, 32, '2026-05-10', 20, 2, 'Pass', 'Within tolerance'),
  (26, 30, '2026-05-05', 30, 1, 'Pass', 'Within tolerance'),
  (27, 31, '2026-05-11', 30, 4, 'Rework', 'Needs corrective action'),
  (28, 32, '2026-06-28', 30, 1, 'Pass', 'Within tolerance'),
  (30, 32, '2026-07-02', 20, 5, 'Fail', 'Needs corrective action'),
  (32, 32, '2026-09-13', 50, 6, 'Fail', 'Needs corrective action'),
  (33, 31, '2026-10-01', 30, 5, 'Fail', 'Needs corrective action'),
  (34, 31, '2026-10-02', 50, 1, 'Pass', 'Within tolerance');

-- defect_log (25 rows)
INSERT INTO defect_log (insp_id, defect_type_id, qty) VALUES
  (1, 3, 2),
  (1, 5, 1),
  (2, 6, 1),
  (4, 1, 2),
  (5, 5, 5),
  (6, 6, 3),
  (6, 1, 1),
  (7, 6, 3),
  (8, 1, 1),
  (8, 3, 1),
  (8, 5, 1),
  (8, 4, 1),
  (9, 1, 3),
  (9, 1, 2),
  (11, 4, 3),
  (13, 3, 1),
  (13, 6, 1),
  (14, 3, 1),
  (14, 4, 1),
  (15, 1, 1),
  (16, 1, 2),
  (17, 5, 1),
  (17, 3, 1),
  (18, 5, 4),
  (18, 5, 1);

-- customer (12 rows)
INSERT INTO customer (customer_name, contact_person, phone, email, city, state, credit_limit, gstin) VALUES
  ('Larsen Heavy Fabricators', 'Purchase Head', '9700020000', 'purchase@larsen.in', 'Mumbai', 'Maharashtra', 1500000.0, '29BBBCU2000D1Z0'),
  ('Bharat Infra Projects', 'Purchase Head', '9700020001', 'purchase@bharat.in', 'Delhi', 'Delhi', 5000000.0, '29BBBCU2001D1Z1'),
  ('Sundaram Textile Mills', 'Purchase Head', '9700020002', 'purchase@sundaram.in', 'Coimbatore', 'Tamil Nadu', 5000000.0, '29BBBCU2002D1Z2'),
  ('Rajputana Cement Works', 'Purchase Head', '9700020003', 'purchase@rajputana.in', 'Udaipur', 'Rajasthan', 1500000.0, '29BBBCU2003D1Z3'),
  ('Gujarat Alkalies Ltd', 'Purchase Head', '9700020004', 'purchase@gujarat.in', 'Vadodara', 'Gujarat', 8000000.0, '29BBBCU2004D1Z4'),
  ('Eastern Coal Handling', 'Purchase Head', '9700020005', 'purchase@eastern.in', 'Dhanbad', 'Jharkhand', 2500000.0, '29BBBCU2005D1Z5'),
  ('Kerala Agro Machinery', 'Purchase Head', '9700020006', 'purchase@kerala.in', 'Kochi', 'Kerala', 8000000.0, '29BBBCU2006D1Z6'),
  ('Punjab Sugar Mills', 'Purchase Head', '9700020007', 'purchase@punjab.in', 'Ludhiana', 'Punjab', 1500000.0, '29BBBCU2007D1Z7'),
  ('Hyderabad Pharma Plants', 'Purchase Head', '9700020008', 'purchase@hyderabad.in', 'Hyderabad', 'Telangana', 2500000.0, '29BBBCU2008D1Z8'),
  ('Karnataka Mining Corp', 'Purchase Head', '9700020009', 'purchase@karnataka.in', 'Bellary', 'Karnataka', 5000000.0, '29BBBCU2009D1Z9'),
  ('Odisha Steel Fabrication', 'Purchase Head', '9700020010', 'purchase@odisha.in', 'Rourkela', 'Odisha', 1500000.0, '29BBBCU2010D1Z0'),
  ('Bengal Jute Industries', 'Purchase Head', '9700020011', 'purchase@bengal.in', 'Kolkata', 'West Bengal', 8000000.0, '29BBBCU2011D1Z1');

-- vehicle (6 rows)
INSERT INTO vehicle (reg_no, vehicle_type, capacity_tons, driver_name, driver_phone, status) VALUES
  ('MH12AB1234', 'Truck 10T', 10.0, 'Ramesh Patil', '9822011111', 'Available'),
  ('RJ14CD5678', 'Truck 16T', 16.0, 'Bhanwar Lal', '9829022222', 'On Trip'),
  ('TN09EF9012', 'Container 20ft', 18.0, 'Murugan K', '9840033333', 'Available'),
  ('MH14GH3456', 'Mini Truck 3T', 3.0, 'Sandeep More', '9823044444', 'Available'),
  ('RJ20IJ7890', 'Truck 12T', 12.0, 'Hari Singh', '9828055555', 'Under Repair'),
  ('TN22KL2468', 'Truck 10T', 10.0, 'Selvam R', '9841066666', 'On Trip');

-- sales_order (26 rows)
INSERT INTO sales_order (customer_id, order_date, delivery_date, status, total_amount) VALUES
  (5, '2026-09-19', '2026-10-03', 'Placed', 263500.0),
  (1, '2026-09-09', '2026-09-29', 'Cancelled', 669000.0),
  (4, '2026-07-30', '2026-08-16', 'Delivered', 480300.0),
  (12, '2026-09-09', '2026-09-24', 'Shipped', 1664200.0),
  (7, '2026-07-07', '2026-07-24', 'Delivered', 671600.0),
  (3, '2026-09-15', '2026-10-02', 'Placed', 30600.0),
  (12, '2026-09-30', '2026-10-12', 'Placed', 685200.0),
  (6, '2026-07-01', '2026-07-11', 'Delivered', 364000.0),
  (12, '2026-09-11', '2026-09-22', 'Shipped', 1560500.0),
  (3, '2026-07-28', '2026-08-09', 'Delivered', 1030000.0),
  (11, '2026-09-15', '2026-09-28', 'Shipped', 494000.0),
  (6, '2026-09-14', '2026-09-29', 'Placed', 284000.0),
  (9, '2026-08-25', '2026-09-13', 'Delivered', 2202400.0),
  (10, '2026-09-03', '2026-09-21', 'Cancelled', 112500.0),
  (3, '2026-07-29', '2026-08-17', 'Delivered', 728700.0),
  (12, '2026-07-31', '2026-08-11', 'Delivered', 456800.0),
  (4, '2026-06-17', '2026-06-30', 'Delivered', 21600.0),
  (12, '2026-09-05', '2026-09-15', 'Cancelled', 1235000.0),
  (12, '2026-09-09', '2026-09-21', 'Placed', 1350200.0),
  (5, '2026-08-16', '2026-08-27', 'Delivered', 2615000.0),
  (9, '2026-06-10', '2026-06-20', 'Delivered', 154000.0),
  (3, '2026-06-14', '2026-07-02', 'Delivered', 1545500.0),
  (3, '2026-09-08', '2026-09-26', 'Shipped', 16200.0),
  (5, '2026-07-21', '2026-08-01', 'Delivered', 2156000.0),
  (7, '2026-06-13', '2026-06-26', 'Delivered', 1550500.0),
  (4, '2026-08-15', '2026-08-31', 'Delivered', 3210000.0);

-- sales_order_item (25 rows)
INSERT INTO sales_order_item (so_id, product_id, quantity, unit_price) VALUES
  (1, 3, 31, 8500.0),
  (2, 3, 14, 8500.0),
  (2, 4, 25, 22000.0),
  (3, 10, 31, 12500.0),
  (3, 5, 29, 3200.0),
  (4, 8, 10, 28000.0),
  (4, 6, 34, 4500.0),
  (4, 1, 27, 45000.0),
  (4, 7, 9, 1800.0),
  (5, 6, 40, 4500.0),
  (5, 5, 13, 3200.0),
  (5, 10, 36, 12500.0),
  (6, 7, 17, 1800.0),
  (7, 3, 28, 8500.0),
  (7, 5, 21, 3200.0),
  (7, 2, 10, 38000.0),
  (8, 5, 10, 3200.0),
  (8, 4, 11, 22000.0),
  (8, 7, 5, 1800.0),
  (8, 6, 18, 4500.0),
  (9, 3, 29, 8500.0),
  (9, 8, 10, 28000.0),
  (9, 2, 11, 38000.0),
  (9, 9, 11, 56000.0),
  (10, 2, 5, 38000.0);

-- shipment (18 rows)
INSERT INTO shipment (so_id, vehicle_id, ship_date, delivered_on, destination, status) VALUES
  (3, 6, '2026-08-05', '2026-08-08', 'Udaipur', 'Delivered'),
  (4, 4, '2026-09-16', NULL, 'Kolkata', 'In Transit'),
  (5, 1, '2026-07-12', '2026-07-24', 'Kochi', 'Delivered'),
  (8, 2, '2026-07-07', '2026-07-10', 'Dhanbad', 'Delivered'),
  (9, 3, '2026-09-16', NULL, 'Kolkata', 'In Transit'),
  (10, 6, '2026-08-03', '2026-08-10', 'Coimbatore', 'Delayed'),
  (11, 5, '2026-09-18', NULL, 'Rourkela', 'In Transit'),
  (13, 6, '2026-08-31', '2026-09-06', 'Hyderabad', 'Delivered'),
  (15, 2, '2026-08-01', '2026-08-07', 'Coimbatore', 'Delivered'),
  (16, 2, '2026-08-07', '2026-08-12', 'Kolkata', 'Delayed'),
  (17, 5, '2026-06-23', '2026-07-04', 'Udaipur', 'Delayed'),
  (20, 5, '2026-08-19', '2026-08-28', 'Vadodara', 'Delayed'),
  (21, 4, '2026-06-16', '2026-06-23', 'Hyderabad', 'Delayed'),
  (22, 6, '2026-06-17', '2026-06-27', 'Coimbatore', 'Delivered'),
  (23, 3, '2026-09-14', NULL, 'Coimbatore', 'In Transit'),
  (24, 2, '2026-07-28', '2026-08-06', 'Vadodara', 'Delayed'),
  (25, 2, '2026-06-17', '2026-06-27', 'Kochi', 'Delayed'),
  (26, 5, '2026-08-18', '2026-08-29', 'Udaipur', 'Delivered');

-- invoice (18 rows)
INSERT INTO invoice (so_id, invoice_date, due_date, amount, tax_amount, status) VALUES
  (3, '2026-08-05', '2026-09-04', 480300.0, 86454.0, 'Partial'),
  (4, '2026-09-16', '2026-10-16', 1664200.0, 299556.0, 'Unpaid'),
  (5, '2026-07-12', '2026-08-11', 671600.0, 120888.0, 'Paid'),
  (8, '2026-07-07', '2026-08-06', 364000.0, 65520.0, 'Paid'),
  (9, '2026-09-16', '2026-10-16', 1560500.0, 280890.0, 'Unpaid'),
  (10, '2026-08-03', '2026-09-02', 1030000.0, 185400.0, 'Paid'),
  (11, '2026-09-18', '2026-10-18', 494000.0, 88920.0, 'Unpaid'),
  (13, '2026-08-31', '2026-09-30', 2202400.0, 396432.0, 'Paid'),
  (15, '2026-08-01', '2026-08-31', 728700.0, 131166.0, 'Paid'),
  (16, '2026-08-07', '2026-09-06', 456800.0, 82224.0, 'Paid'),
  (17, '2026-06-23', '2026-07-23', 21600.0, 3888.0, 'Paid'),
  (20, '2026-08-19', '2026-09-18', 2615000.0, 470700.0, 'Paid'),
  (21, '2026-06-16', '2026-07-16', 154000.0, 27720.0, 'Unpaid'),
  (22, '2026-06-17', '2026-07-17', 1545500.0, 278190.0, 'Unpaid'),
  (23, '2026-09-14', '2026-10-14', 16200.0, 2916.0, 'Unpaid'),
  (24, '2026-07-28', '2026-08-27', 2156000.0, 388080.0, 'Partial'),
  (25, '2026-06-17', '2026-07-17', 1550500.0, 279090.0, 'Paid'),
  (26, '2026-08-18', '2026-09-17', 3210000.0, 577800.0, 'Paid');

-- payment (17 rows)
INSERT INTO payment (invoice_id, payment_date, amount, mode, ref_no) VALUES
  (1, '2026-09-03', 170026.2, 'Cheque', 'TXN540660'),
  (3, '2026-08-15', 792488.0, 'Bank Transfer', 'TXN130273'),
  (4, '2026-08-09', 429520.0, 'Bank Transfer', 'TXN008455'),
  (6, '2026-08-17', 1215400.0, 'UPI', 'TXN646003'),
  (8, '2026-10-06', 1039532.8, 'UPI', 'TXN118221'),
  (8, '2026-09-20', 1559299.2, 'UPI', 'TXN669078'),
  (9, '2026-08-31', 343946.4, 'Cheque', 'TXN300405'),
  (9, '2026-09-07', 515919.6, 'UPI', 'TXN444151'),
  (10, '2026-08-16', 269512.0, 'UPI', 'TXN732615'),
  (10, '2026-09-08', 269512.0, 'Cheque', 'TXN999742'),
  (11, '2026-07-09', 25488.0, 'Bank Transfer', 'TXN013281'),
  (12, '2026-09-09', 1234280.0, 'Bank Transfer', 'TXN937709'),
  (12, '2026-08-31', 1851420.0, 'Bank Transfer', 'TXN776765'),
  (16, '2026-08-19', 763224.0, 'Bank Transfer', 'TXN006688'),
  (17, '2026-07-01', 731836.0, 'Cheque', 'TXN293419'),
  (17, '2026-06-27', 1097754.0, 'Bank Transfer', 'TXN966027'),
  (18, '2026-08-31', 3787800.0, 'Bank Transfer', 'TXN305845');

-- =============================================================================
-- MODULE 6 : AUDIT LOG INITIAL SEED RECORDS
-- =============================================================================

INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value, created_at) VALUES
  ('AUD-BOOTSTRAP-01', 'INSERT', 'SYSTEM_INIT', 'system', 'Industrial ERP Database Schema and Core Seed Data initialized', 'None', 'Database initialized with 42 tables', CURRENT_TIMESTAMP),
  ('AUD-BOOTSTRAP-02', 'INSERT', 'RBAC_SECURITY', 'admin', 'Default seed user accounts created with BCrypt encrypted credentials', 'None', 'Created 8 role-based user accounts', CURRENT_TIMESTAMP),
  ('AUD-BOOTSTRAP-03', 'UPDATE', 'INVENTORY_BASE', 'system', 'Catalog pricing and reorder thresholds initialized', 'NULL', 'Baseline valuation and 18% GST active', CURRENT_TIMESTAMP);

-- =============================================================================
-- MODULE 7 : USER ACCOUNTS (PRE-CONFIGURED SEED USERS & REGISTRATION INSERTS)
-- Default Password for all seed users is 'password123'
-- =============================================================================

INSERT INTO user_account (username, password, role, full_name, email, department_id, employee_id, customer_id, contractor_id, assigned_machine_id, enabled, created_at) VALUES
  ('ceo', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'CEO', 'Suresh Pillai', 'ceo@indmfg.in', 1, 1, NULL, NULL, NULL, TRUE, CURRENT_TIMESTAMP),
  ('gm', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'GENERAL_MANAGER', 'Anil Singh', 'gm@indmfg.in', 1, 2, NULL, NULL, NULL, TRUE, CURRENT_TIMESTAMP),
  ('depthead_prod', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'DEPT_HEAD', 'Geeta Gupta', 'geeta.gupta4@indmfg.in', 1, 4, NULL, NULL, NULL, TRUE, CURRENT_TIMESTAMP),
  ('depthead_maint', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'DEPT_HEAD', 'Ashok Saxena', 'ashok.saxena5@indmfg.in', 2, 5, NULL, NULL, NULL, TRUE, CURRENT_TIMESTAMP),
  ('operator1', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'OPERATOR', 'Aarti Yadav', 'aarti.yadav17@indmfg.in', 1, 6, NULL, NULL, 1, TRUE, CURRENT_TIMESTAMP),
  ('employee1', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'EMPLOYEE', 'Nitin Mishra', 'nitin.mishra14@indmfg.in', 1, 5, NULL, NULL, NULL, TRUE, CURRENT_TIMESTAMP),
  ('customer1', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'CUSTOMER', 'Acme Engineering Solutions Ltd', 'purchase@acme.in', 1, NULL, 1, NULL, NULL, TRUE, CURRENT_TIMESTAMP),
  ('transporter1', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'TRANSPORTER', 'SafeGuard Express Logistics', 'logistics@safeguard.in', 1, NULL, NULL, 1, NULL, TRUE, CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------------------
-- DYNAMIC NEW USER REGISTRATION INSERTS (EXAMPLES & TEMPLATES FOR USER ADDITIONS)
-- -----------------------------------------------------------------------------

-- Example A: Insert new CUSTOMER user with linked customer domain profile
INSERT INTO customer (customer_name, contact_person, phone, email, city, state, credit_limit, gstin)
VALUES ('Apex Automotive Systems Pvt Ltd', 'Vikramaditya Rao', '+91 9876501234', 'procurement@apexauto.in', 'Pune', 'Maharashtra', 3500000.00, '27AABCA1234F1Z5');

INSERT INTO user_account (username, password, role, full_name, email, department_id, customer_id, enabled)
VALUES ('apex_buyer', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'CUSTOMER', 'Vikramaditya Rao', 'procurement@apexauto.in', 1, LAST_INSERT_ID(), TRUE);

INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value)
VALUES ('AUD-REG-001', 'INSERT', 'CUSTOMER', 'apex_buyer', 'Registered new commercial account: Apex Automotive Systems', 'None', 'Customer ID mapped, role=CUSTOMER');

-- Example B: Insert new PLANT OPERATOR with linked employee profile
INSERT INTO employee (first_name, last_name, gender, dob, hire_date, email, phone, dept_id, desig_id, plant_id, shift_id, status)
VALUES ('Sunil', 'Kulkarni', 'M', '1995-04-12', '2026-01-15', 'sunil.k@indmfg.in', '+91 9822099887', 1, 3, 1, 1, 'Active');

INSERT INTO user_account (username, password, role, full_name, email, department_id, employee_id, assigned_machine_id, enabled)
VALUES ('operator_sunil', '/xV93R8Uv6xRWe.d7L4yZ8L1hE1fH5z7qJ2qZ1m4q7y6m', 'OPERATOR', 'Sunil Kulkarni', 'sunil.k@indmfg.in', 1, LAST_INSERT_ID(), 2, TRUE);

INSERT INTO audit_log (audit_id, action_type, entity_name, username, payload_summary, previous_value, new_value)
VALUES ('AUD-REG-002', 'INSERT', 'EMPLOYEE', 'operator_sunil', 'Registered new operator: Sunil Kulkarni assigned to Machine #2', 'None', 'Employee ID mapped, role=OPERATOR');
