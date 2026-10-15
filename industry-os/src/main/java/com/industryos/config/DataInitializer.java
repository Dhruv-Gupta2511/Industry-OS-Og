package com.industryos.config;

import com.industryos.entity.*;
import com.industryos.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PlantRepository plantRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductionLineRepository productionLineRepository;
    private final MachineRepository machineRepository;
    private final SparePartRepository sparePartRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final MaintenancePartUsageRepository maintenancePartUsageRepository;
    private final ContractorRepository contractorRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final ShipmentRepository shipmentRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ProductionBatchRepository productionBatchRepository;
    private final EnergyConsumptionRepository energyConsumptionRepository;
    private final SafetyIncidentRepository safetyIncidentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserAccountRepository userAccountRepository,
                           PlantRepository plantRepository,
                           DepartmentRepository departmentRepository,
                           DesignationRepository designationRepository,
                           ShiftRepository shiftRepository,
                           EmployeeRepository employeeRepository,
                           ProductionLineRepository productionLineRepository,
                           MachineRepository machineRepository,
                           SparePartRepository sparePartRepository,
                           MaintenanceLogRepository maintenanceLogRepository,
                           MaintenancePartUsageRepository maintenancePartUsageRepository,
                           ContractorRepository contractorRepository,
                           VehicleRepository vehicleRepository,
                           CustomerRepository customerRepository,
                           SalesOrderRepository salesOrderRepository,
                           SalesOrderItemRepository salesOrderItemRepository,
                           InvoiceRepository invoiceRepository,
                           PaymentRepository paymentRepository,
                           ShipmentRepository shipmentRepository,
                           ProductCategoryRepository productCategoryRepository,
                           ProductRepository productRepository,
                           WorkOrderRepository workOrderRepository,
                           ProductionBatchRepository productionBatchRepository,
                           EnergyConsumptionRepository energyConsumptionRepository,
                           SafetyIncidentRepository safetyIncidentRepository,
                           PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.plantRepository = plantRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.productionLineRepository = productionLineRepository;
        this.machineRepository = machineRepository;
        this.sparePartRepository = sparePartRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.maintenancePartUsageRepository = maintenancePartUsageRepository;
        this.contractorRepository = contractorRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.shipmentRepository = shipmentRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.workOrderRepository = workOrderRepository;
        this.productionBatchRepository = productionBatchRepository;
        this.energyConsumptionRepository = energyConsumptionRepository;
        this.safetyIncidentRepository = safetyIncidentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedBusinessData();
        seedUserAccounts();
    }

    private void seedBusinessData() {
        if (plantRepository.count() > 0) {
            return; // Data already seeded
        }

        // 1. Plants
        Plant p1 = plantRepository.save(new Plant(1, "Pune Works", "Pune", "Maharashtra", 1200, LocalDate.of(2008, 4, 12)));
        Plant p2 = plantRepository.save(new Plant(2, "Jaipur Works", "Jaipur", "Rajasthan", 900, LocalDate.of(2012, 9, 3)));
        Plant p3 = plantRepository.save(new Plant(3, "Chennai Works", "Chennai", "Tamil Nadu", 1000, LocalDate.of(2016, 1, 20)));

        // 2. Departments
        Department d1 = departmentRepository.save(new Department(1, "Production", "Shop Floor", new BigDecimal("9500000.00")));
        Department d2 = departmentRepository.save(new Department(2, "Maintenance", "Workshop", new BigDecimal("4200000.00")));
        Department d3 = departmentRepository.save(new Department(3, "Quality Control", "QC Lab", new BigDecimal("3100000.00")));
        Department d4 = departmentRepository.save(new Department(4, "Stores & Inventory", "Warehouse", new BigDecimal("2800000.00")));
        Department d5 = departmentRepository.save(new Department(5, "Procurement", "Admin Block", new BigDecimal("2500000.00")));
        Department d6 = departmentRepository.save(new Department(6, "Sales & Marketing", "Admin Block", new BigDecimal("5200000.00")));
        Department d7 = departmentRepository.save(new Department(7, "Human Resources", "Admin Block", new BigDecimal("1800000.00")));
        Department d8 = departmentRepository.save(new Department(8, "Finance & Accounts", "Admin Block", new BigDecimal("2200000.00")));
        Department d9 = departmentRepository.save(new Department(9, "Safety & EHS", "Admin Block", new BigDecimal("1500000.00")));
        Department d10 = departmentRepository.save(new Department(10, "Logistics", "Dispatch Yard", new BigDecimal("3300000.00")));

        // 3. Designations
        designationRepository.save(new Designation(1, "Plant Manager", "G1", new BigDecimal("120000.00")));
        designationRepository.save(new Designation(2, "Department Manager", "G2", new BigDecimal("85000.00")));
        designationRepository.save(new Designation(3, "Engineer", "G3", new BigDecimal("55000.00")));
        designationRepository.save(new Designation(4, "Supervisor", "G3", new BigDecimal("48000.00")));
        designationRepository.save(new Designation(5, "Senior Technician", "G4", new BigDecimal("38000.00")));
        designationRepository.save(new Designation(6, "Technician", "G5", new BigDecimal("28000.00")));
        designationRepository.save(new Designation(7, "Operator", "G5", new BigDecimal("24000.00")));
        designationRepository.save(new Designation(8, "Executive", "G4", new BigDecimal("40000.00")));
        designationRepository.save(new Designation(9, "Accountant", "G4", new BigDecimal("42000.00")));
        designationRepository.save(new Designation(10, "Logistics Helper", "G6", new BigDecimal("20000.00")));

        // 4. Shifts
        shiftRepository.save(new Shift(1, "Morning", LocalTime.of(6, 0), LocalTime.of(14, 0)));
        shiftRepository.save(new Shift(2, "General", LocalTime.of(9, 0), LocalTime.of(17, 0)));
        shiftRepository.save(new Shift(3, "Night", LocalTime.of(22, 0), LocalTime.of(6, 0)));

        // 5. Employees
        Employee e1 = new Employee();
        e1.setFirstName("Suresh"); e1.setLastName("Pillai"); e1.setGender("M"); e1.setDob(LocalDate.of(1972, 7, 12));
        e1.setHireDate(LocalDate.of(2022, 7, 11)); e1.setEmail("suresh.pillai1@indmfg.in"); e1.setPhone("9398471886");
        e1.setDeptId(1); e1.setDesigId(1); e1.setPlantId(1); e1.setShiftId(1); e1.setStatus("Active");
        employeeRepository.save(e1);

        Employee e2 = new Employee();
        e2.setFirstName("Anil"); e2.setLastName("Singh"); e2.setGender("M"); e2.setDob(LocalDate.of(1988, 5, 3));
        e2.setHireDate(LocalDate.of(2022, 3, 8)); e2.setEmail("anil.singh2@indmfg.in"); e2.setPhone("9274648506");
        e2.setDeptId(1); e2.setDesigId(1); e2.setPlantId(2); e2.setShiftId(2); e2.setStatus("Active");
        employeeRepository.save(e2);

        Employee e4 = new Employee();
        e4.setFirstName("Geeta"); e4.setLastName("Gupta"); e4.setGender("F"); e4.setDob(LocalDate.of(1998, 1, 23));
        e4.setHireDate(LocalDate.of(2019, 12, 28)); e4.setEmail("geeta.gupta4@indmfg.in"); e4.setPhone("9853573823");
        e4.setDeptId(1); e4.setDesigId(2); e4.setPlantId(1); e4.setShiftId(1); e4.setStatus("Active");
        employeeRepository.save(e4);

        Employee e5 = new Employee();
        e5.setFirstName("Ashok"); e5.setLastName("Saxena"); e5.setGender("M"); e5.setDob(LocalDate.of(1998, 8, 30));
        e5.setHireDate(LocalDate.of(2015, 8, 8)); e5.setEmail("ashok.saxena5@indmfg.in"); e5.setPhone("9754049436");
        e5.setDeptId(2); e5.setDesigId(2); e5.setPlantId(2); e5.setShiftId(2); e5.setStatus("Active");
        employeeRepository.save(e5);

        Employee e14 = new Employee();
        e14.setFirstName("Nitin"); e14.setLastName("Mishra"); e14.setGender("M"); e14.setDob(LocalDate.of(1997, 5, 26));
        e14.setHireDate(LocalDate.of(2025, 3, 13)); e14.setEmail("nitin.mishra14@indmfg.in"); e14.setPhone("9524806516");
        e14.setDeptId(1); e14.setDesigId(4); e14.setPlantId(2); e14.setShiftId(2); e14.setStatus("Active");
        employeeRepository.save(e14);

        Employee e17 = new Employee();
        e17.setFirstName("Aarti"); e17.setLastName("Yadav"); e17.setGender("F"); e17.setDob(LocalDate.of(2001, 6, 1));
        e17.setHireDate(LocalDate.of(2019, 11, 23)); e17.setEmail("aarti.yadav17@indmfg.in"); e17.setPhone("9902099969");
        e17.setDeptId(1); e17.setDesigId(7); e17.setPlantId(1); e17.setShiftId(2); e17.setStatus("Active");
        employeeRepository.save(e17);

        // 6. Production Lines
        productionLineRepository.save(new ProductionLine(1, 1, "Assembly Line A", 40, "Running"));
        productionLineRepository.save(new ProductionLine(2, 1, "Machining Line B", 30, "Running"));
        productionLineRepository.save(new ProductionLine(3, 1, "Fabrication Line C", 25, "Idle"));
        productionLineRepository.save(new ProductionLine(4, 2, "Assembly Line A", 35, "Running"));
        productionLineRepository.save(new ProductionLine(5, 2, "Casting Line B", 20, "Running"));
        productionLineRepository.save(new ProductionLine(6, 2, "Welding Line C", 28, "Running"));
        productionLineRepository.save(new ProductionLine(7, 3, "Assembly Line A", 45, "Running"));
        productionLineRepository.save(new ProductionLine(8, 3, "Finishing Line B", 38, "Shutdown"));

        // 7. Machines
        Machine m1 = new Machine();
        m1.setLineId(1); m1.setMachineName("CNC Lathe"); m1.setModelNo("QT-200"); m1.setManufacturer("Mazak");
        m1.setPurchaseDate(LocalDate.of(2016, 9, 14)); m1.setPurchaseCost(new BigDecimal("1900000.00"));
        m1.setStatus("Operational"); m1.setLastService(LocalDate.of(2026, 9, 1));
        machineRepository.save(m1);

        Machine m2 = new Machine();
        m2.setLineId(2); m2.setMachineName("Hydraulic Press 200T"); m2.setModelNo("HP-200"); m2.setManufacturer("Hindustan Hydraulics");
        m2.setPurchaseDate(LocalDate.of(2021, 8, 9)); m2.setPurchaseCost(new BigDecimal("7300000.00"));
        m2.setStatus("Operational"); m2.setLastService(LocalDate.of(2026, 9, 15));
        machineRepository.save(m2);

        Machine m4 = new Machine();
        m4.setLineId(4); m4.setMachineName("Vertical Milling Machine"); m4.setModelNo("VM-4"); m4.setManufacturer("HMT");
        m4.setPurchaseDate(LocalDate.of(2022, 6, 13)); m4.setPurchaseCost(new BigDecimal("3500000.00"));
        m4.setStatus("Under Maintenance"); m4.setLastService(LocalDate.of(2026, 6, 23));
        machineRepository.save(m4);

        // 8. Spare Parts
        SparePart sp1 = new SparePart();
        sp1.setPartName("Bearing 6205"); sp1.setUnitCost(new BigDecimal("450.00")); sp1.setStockQty(40); sp1.setReorderLevel(20);
        sparePartRepository.save(sp1);

        SparePart sp2 = new SparePart();
        sp2.setPartName("V-Drive Belt B-68"); sp2.setUnitCost(new BigDecimal("380.00")); sp2.setStockQty(25); sp2.setReorderLevel(10);
        sparePartRepository.save(sp2);

        SparePart sp3 = new SparePart();
        sp3.setPartName("Hydraulic Seal Kit"); sp3.setUnitCost(new BigDecimal("2200.00")); sp3.setStockQty(12); sp3.setReorderLevel(6);
        sparePartRepository.save(sp3);

        SparePart sp4 = new SparePart();
        sp4.setPartName("Coolant Pump 0.5HP"); sp4.setUnitCost(new BigDecimal("5400.00")); sp4.setStockQty(4); sp4.setReorderLevel(5);
        sparePartRepository.save(sp4);

        SparePart sp5 = new SparePart();
        sp5.setPartName("Proximity Sensor M18"); sp5.setUnitCost(new BigDecimal("1250.00")); sp5.setStockQty(18); sp5.setReorderLevel(8);
        sparePartRepository.save(sp5);

        // 9. Maintenance Logs & Parts Usage
        MaintenanceLog ml1 = new MaintenanceLog();
        ml1.setMachineId(1); ml1.setTechnicianId(1); ml1.setMaintType("Preventive");
        ml1.setMaintDate(LocalDate.of(2026, 9, 1)); ml1.setDowntimeHours(new BigDecimal("4.5"));
        ml1.setLabourCost(new BigDecimal("4300.00")); ml1.setDescription("Spindle bearing replacement & calibration");
        maintenanceLogRepository.save(ml1);

        maintenancePartUsageRepository.save(new MaintenancePartUsage(ml1.getMaintId(), sp1.getPartId(), 2));
        maintenancePartUsageRepository.save(new MaintenancePartUsage(ml1.getMaintId(), sp5.getPartId(), 1));

        MaintenanceLog ml2 = new MaintenanceLog();
        ml2.setMachineId(2); ml2.setTechnicianId(1); ml2.setMaintType("Corrective");
        ml2.setMaintDate(LocalDate.of(2026, 9, 15)); ml2.setDowntimeHours(new BigDecimal("11.0"));
        ml2.setLabourCost(new BigDecimal("4900.00")); ml2.setDescription("Hydraulic seal leakage repaired");
        maintenanceLogRepository.save(ml2);

        maintenancePartUsageRepository.save(new MaintenancePartUsage(ml2.getMaintId(), sp3.getPartId(), 2));

        // 10. Contractors
        Contractor c1 = new Contractor();
        c1.setCompanyName("SafeGuard Express Logistics & Transport"); c1.setServiceType("Logistics & Fleet Transport");
        c1.setPhone("9811100001"); c1.setPlantId(1); c1.setContractStart(LocalDate.of(2025, 4, 1));
        c1.setContractEnd(LocalDate.of(2027, 3, 31)); c1.setContractValue(new BigDecimal("2400000.00"));
        contractorRepository.save(c1);

        // 11. Vehicles
        Vehicle v1 = new Vehicle();
        v1.setRegNo("MH12AB1234"); v1.setVehicleType("Truck 10T"); v1.setCapacityTons(new BigDecimal("10.0"));
        v1.setDriverName("Ramesh Patil"); v1.setDriverPhone("9822011111"); v1.setStatus("Available");
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle();
        v2.setRegNo("RJ14CD5678"); v2.setVehicleType("Truck 16T"); v2.setCapacityTons(new BigDecimal("16.0"));
        v2.setDriverName("Bhanwar Lal"); v2.setDriverPhone("9829022222"); v2.setStatus("On Trip");
        vehicleRepository.save(v2);

        Vehicle v4 = new Vehicle();
        v4.setRegNo("MH14GH3456"); v4.setVehicleType("Mini Truck 3T"); v4.setCapacityTons(new BigDecimal("3.0"));
        v4.setDriverName("Sandeep More"); v4.setDriverPhone("9823044444"); v4.setStatus("On Trip");
        vehicleRepository.save(v4);

        // 12. Products & Categories
        ProductCategory cat1 = productCategoryRepository.save(new ProductCategory(1, "Automotive Components"));
        ProductCategory cat2 = productCategoryRepository.save(new ProductCategory(2, "Industrial Fasteners"));

        Product pr1 = new Product();
        pr1.setProductName("Flange Shaft Assembly"); pr1.setSku("PRD-AUTO-001"); pr1.setPcatId(cat1.getPcatId());
        pr1.setUnitPrice(new BigDecimal("8500.00")); pr1.setStandardCost(new BigDecimal("5200.00")); pr1.setStockQty(150);
        productRepository.save(pr1);

        Product pr2 = new Product();
        pr2.setProductName("Heavy Transmission Gear"); pr2.setSku("PRD-AUTO-002"); pr2.setPcatId(cat1.getPcatId());
        pr2.setUnitPrice(new BigDecimal("22000.00")); pr2.setStandardCost(new BigDecimal("14500.00")); pr2.setStockQty(80);
        productRepository.save(pr2);

        Product pr3 = new Product();
        pr3.setProductName("High-Tensile Hex Bolt M16"); pr3.setSku("PRD-FST-003"); pr3.setPcatId(cat2.getPcatId());
        pr3.setUnitPrice(new BigDecimal("3200.00")); pr3.setStandardCost(new BigDecimal("1850.00")); pr3.setStockQty(450);
        productRepository.save(pr3);

        // 13. Customers
        Customer cust5 = new Customer();
        cust5.setCustomerName("Acme Engineering Solutions Ltd"); cust5.setContactPerson("R. K. Malhotra");
        cust5.setPhone("9876543210"); cust5.setEmail("purchase@acme.in"); cust5.setCity("Pune");
        cust5.setState("Maharashtra"); cust5.setCreditLimit(new BigDecimal("5000000.00")); cust5.setGstin("27AAAPL5555C1Z1");
        customerRepository.save(cust5);

        // 14. Sales Orders, Items, Invoices, Shipments, Payments
        SalesOrder so1 = new SalesOrder();
        so1.setCustomerId(cust5.getCustomerId()); so1.setOrderDate(LocalDate.of(2026, 9, 19));
        so1.setDeliveryDate(LocalDate.of(2026, 10, 3)); so1.setStatus("Placed"); so1.setTotalAmount(new BigDecimal("263500.00"));
        salesOrderRepository.save(so1);

        salesOrderItemRepository.save(new SalesOrderItem(so1.getSoId(), pr1.getProductId(), 31, new BigDecimal("8500.00")));

        SalesOrder so2 = new SalesOrder();
        so2.setCustomerId(cust5.getCustomerId()); so2.setOrderDate(LocalDate.of(2026, 8, 16));
        so2.setDeliveryDate(LocalDate.of(2026, 8, 27)); so2.setStatus("Delivered"); so2.setTotalAmount(new BigDecimal("2615000.00"));
        salesOrderRepository.save(so2);

        salesOrderItemRepository.save(new SalesOrderItem(so2.getSoId(), pr2.getProductId(), 100, new BigDecimal("22000.00")));
        salesOrderItemRepository.save(new SalesOrderItem(so2.getSoId(), pr3.getProductId(), 130, new BigDecimal("3200.00")));

        Invoice inv2 = new Invoice();
        inv2.setSoId(so2.getSoId()); inv2.setInvoiceDate(LocalDate.of(2026, 8, 19)); inv2.setDueDate(LocalDate.of(2026, 9, 18));
        inv2.setAmount(new BigDecimal("2615000.00")); inv2.setTaxAmount(new BigDecimal("470700.00")); inv2.setStatus("Paid");
        invoiceRepository.save(inv2);

        Payment pmt2 = new Payment();
        pmt2.setInvoiceId(inv2.getInvoiceId()); pmt2.setPaymentDate(LocalDate.of(2026, 8, 31));
        pmt2.setAmount(new BigDecimal("3085700.00")); pmt2.setMode("Bank Transfer"); pmt2.setRefNo("TXN889922");
        paymentRepository.save(pmt2);

        Shipment shp2 = new Shipment();
        shp2.setSoId(so2.getSoId()); shp2.setVehicleId(v2.getVehicleId());
        shp2.setShipDate(LocalDate.of(2026, 8, 19)); shp2.setDeliveredOn(LocalDate.of(2026, 8, 27));
        shp2.setDestination("Pune Industrial Estate"); shp2.setStatus("Delivered");
        shipmentRepository.save(shp2);

        // 15. Work Orders & Production Batches
        WorkOrder wo1 = new WorkOrder();
        wo1.setProductId(pr1.getProductId()); wo1.setLineId(1); wo1.setSupervisorId(e14.getEmpId());
        wo1.setPlannedQty(400); wo1.setStartDate(LocalDate.of(2026, 8, 13)); wo1.setEndDate(LocalDate.of(2026, 9, 2));
        wo1.setStatus("Completed");
        workOrderRepository.save(wo1);

        ProductionBatch pb1 = new ProductionBatch();
        pb1.setWoId(wo1.getWoId()); pb1.setShiftId(1); pb1.setBatchNo("BAT-2026-0813-A");
        pb1.setBatchDate(LocalDate.of(2026, 8, 20)); pb1.setProducedQty(400); pb1.setRejectedQty(12);
        productionBatchRepository.save(pb1);

        // 16. Energy Consumption & Safety Incidents
        EnergyConsumption ec1 = new EnergyConsumption();
        ec1.setPlantId(1); ec1.setRecordDate(LocalDate.of(2026, 9, 1));
        ec1.setUnitsKwh(new BigDecimal("13171.13")); ec1.setCost(new BigDecimal("111954.60"));
        energyConsumptionRepository.save(ec1);

        SafetyIncident si1 = new SafetyIncident();
        si1.setPlantId(1); si1.setEmpId(e17.getEmpId()); si1.setIncidentDate(LocalDate.of(2026, 6, 1));
        si1.setSeverity("Minor"); si1.setDescription("Slip on oil patch near press"); si1.setActionTaken("Anti-skid mats installed");
        safetyIncidentRepository.save(si1);
    }

    private void seedUserAccounts() {
        if (userAccountRepository.count() > 0) {
            return;
        }

        String defaultEncodedPassword = passwordEncoder.encode("password123");

        // 1. CEO (Global unrestricted access)
        UserAccount ceo = new UserAccount("ceo", defaultEncodedPassword, Role.CEO, "Suresh Pillai", "ceo@indmfg.in");
        ceo.setDepartmentId(1);
        ceo.setEmployeeId(1);
        userAccountRepository.save(ceo);

        // 2. GENERAL_MANAGER (Global unrestricted access)
        UserAccount gm = new UserAccount("gm", defaultEncodedPassword, Role.GENERAL_MANAGER, "Anil Singh", "gm@indmfg.in");
        gm.setDepartmentId(1);
        gm.setEmployeeId(2);
        userAccountRepository.save(gm);

        // 3. DEPT_HEAD (Production - Head of Dept 1)
        UserAccount deptHeadProd = new UserAccount("depthead_prod", defaultEncodedPassword, Role.DEPT_HEAD, "Geeta Gupta", "geeta.gupta4@indmfg.in");
        deptHeadProd.setDepartmentId(1);
        deptHeadProd.setEmployeeId(4);
        userAccountRepository.save(deptHeadProd);

        // 4. DEPT_HEAD (Maintenance - Head of Dept 2)
        UserAccount deptHeadMaint = new UserAccount("depthead_maint", defaultEncodedPassword, Role.DEPT_HEAD, "Ashok Saxena", "ashok.saxena5@indmfg.in");
        deptHeadMaint.setDepartmentId(2);
        deptHeadMaint.setEmployeeId(5);
        userAccountRepository.save(deptHeadMaint);

        // 5. OPERATOR (Assigned to CNC Lathe Machine 1)
        UserAccount operator = new UserAccount("operator1", defaultEncodedPassword, Role.OPERATOR, "Aarti Yadav", "aarti.yadav17@indmfg.in");
        operator.setDepartmentId(1);
        operator.setEmployeeId(6);
        operator.setAssignedMachineId(1);
        userAccountRepository.save(operator);

        // 6. EMPLOYEE (General Employee)
        UserAccount employee = new UserAccount("employee1", defaultEncodedPassword, Role.EMPLOYEE, "Nitin Mishra", "nitin.mishra14@indmfg.in");
        employee.setDepartmentId(1);
        employee.setEmployeeId(5);
        userAccountRepository.save(employee);

        // 7. CUSTOMER (Retailer / Buyer)
        UserAccount customer = new UserAccount("customer1", defaultEncodedPassword, Role.CUSTOMER, "Acme Engineering Solutions", "purchase@acme.in");
        customer.setCustomerId(1);
        userAccountRepository.save(customer);

        // 8. TRANSPORTER (Contractor / Logistics)
        UserAccount transporter = new UserAccount("transporter1", defaultEncodedPassword, Role.TRANSPORTER, "SafeGuard Express Logistics", "logistics@safeguard.in");
        transporter.setContractorId(1);
        userAccountRepository.save(transporter);
    }
}

