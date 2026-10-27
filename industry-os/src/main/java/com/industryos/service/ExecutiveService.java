package com.industryos.service;

import com.industryos.dto.response.ExecutiveDashboardResponse;
import com.industryos.dto.response.ExecutiveDashboardResponse.*;
import com.industryos.entity.*;
import com.industryos.exception.ResourceNotFoundException;
import com.industryos.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExecutiveService {

    private final UserAccountRepository userAccountRepository;
    private final EmployeeRepository employeeRepository;
    private final PlantRepository plantRepository;
    private final DepartmentRepository departmentRepository;
    private final ProductionLineRepository productionLineRepository;
    private final MachineRepository machineRepository;
    private final CustomerRepository customerRepository;
    private final ContractorRepository contractorRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final VehicleRepository vehicleRepository;
    private final EnergyConsumptionRepository energyConsumptionRepository;
    private final SafetyIncidentRepository safetyIncidentRepository;
    private final ProductRepository productRepository;

    public ExecutiveService(UserAccountRepository userAccountRepository,
                            EmployeeRepository employeeRepository,
                            PlantRepository plantRepository,
                            DepartmentRepository departmentRepository,
                            ProductionLineRepository productionLineRepository,
                            MachineRepository machineRepository,
                            CustomerRepository customerRepository,
                            ContractorRepository contractorRepository,
                            SalesOrderRepository salesOrderRepository,
                            InvoiceRepository invoiceRepository,
                            PaymentRepository paymentRepository,
                            MaintenanceLogRepository maintenanceLogRepository,
                            VehicleRepository vehicleRepository,
                            EnergyConsumptionRepository energyConsumptionRepository,
                            SafetyIncidentRepository safetyIncidentRepository,
                            ProductRepository productRepository) {
        this.userAccountRepository = userAccountRepository;
        this.employeeRepository = employeeRepository;
        this.plantRepository = plantRepository;
        this.departmentRepository = departmentRepository;
        this.productionLineRepository = productionLineRepository;
        this.machineRepository = machineRepository;
        this.customerRepository = customerRepository;
        this.contractorRepository = contractorRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.vehicleRepository = vehicleRepository;
        this.energyConsumptionRepository = energyConsumptionRepository;
        this.safetyIncidentRepository = safetyIncidentRepository;
        this.productRepository = productRepository;
    }

    public ExecutiveDashboardResponse getExecutiveDashboard(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Executive user not found: " + username));

        ExecutiveDashboardResponse response = new ExecutiveDashboardResponse();
        response.setExecutiveName(user.getFullName());
        response.setRole(user.getRole());

        // 1. Company Overview
        CompanyOverviewDTO overview = new CompanyOverviewDTO();
        overview.setTotalEmployees(employeeRepository.count());
        overview.setTotalPlants((int) plantRepository.count());
        overview.setTotalProductionLines((int) productionLineRepository.count());
        overview.setTotalMachines((int) machineRepository.count());
        overview.setOperationalMachines(machineRepository.countByStatus("Operational"));
        overview.setUnderMaintenanceMachines(machineRepository.countByStatus("Under Maintenance"));
        overview.setTotalActiveCustomers((int) customerRepository.count());
        overview.setTotalContractors((int) contractorRepository.count());
        response.setCompanyOverview(overview);

        // 2. Global Financials
        GlobalFinancialsDTO financials = new GlobalFinancialsDTO();
        BigDecimal totalSales = salesOrderRepository.findAll().stream()
                .filter(so -> !"Cancelled".equalsIgnoreCase(so.getStatus()))
                .map(SalesOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        financials.setTotalSalesRevenue(totalSales);

        BigDecimal totalInvoiced = invoiceRepository.findAll().stream()
                .map(Invoice::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        financials.setTotalInvoicedAmount(totalInvoiced);

        BigDecimal totalCollected = paymentRepository.findAll().stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        financials.setTotalCollectedRevenue(totalCollected);

        BigDecimal receivables = totalInvoiced.subtract(totalCollected);
        financials.setTotalAccountsReceivable(receivables.compareTo(BigDecimal.ZERO) > 0 ? receivables : BigDecimal.ZERO);

        BigDecimal totalBudgets = departmentRepository.findAll().stream()
                .map(d -> d.getBudget() != null ? d.getBudget() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        financials.setTotalDepartmentBudgets(totalBudgets);

        BigDecimal totalMaintenanceLabour = maintenanceLogRepository.findAll().stream()
                .map(m -> m.getLabourCost() != null ? m.getLabourCost() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        financials.setTotalMaintenanceCost(totalMaintenanceLabour);

        BigDecimal totalFleetCapacity = vehicleRepository.findAll().stream()
                .map(v -> v.getCapacityTons() != null ? v.getCapacityTons() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        financials.setTotalFleetCapacityTons(totalFleetCapacity);

        response.setGlobalFinancials(financials);

        // 3. Plants Overview
        List<Plant> plants = plantRepository.findAll();
        List<PlantOverviewDTO> plantDTOs = new ArrayList<>();

        for (Plant plant : plants) {
            PlantOverviewDTO pDTO = new PlantOverviewDTO();
            pDTO.setPlantId(plant.getPlantId());
            pDTO.setPlantName(plant.getPlantName());
            pDTO.setCity(plant.getCity());
            pDTO.setState(plant.getState());
            pDTO.setDailyCapacity(plant.getDailyCapacity());

            pDTO.setEmployeeCount(employeeRepository.findByPlantId(plant.getPlantId()).size());

            List<ProductionLine> lines = productionLineRepository.findByPlantId(plant.getPlantId());
            pDTO.setProductionLinesCount(lines.size());

            List<Integer> lineIds = lines.stream().map(ProductionLine::getLineId).toList();
            if (!lineIds.isEmpty()) {
                pDTO.setMachinesCount(machineRepository.findByLineIdIn(lineIds).size());
            } else {
                pDTO.setMachinesCount(0);
            }

            List<EnergyConsumption> energyList = energyConsumptionRepository.findByPlantId(plant.getPlantId());
            BigDecimal kwh = energyList.stream().map(e -> e.getUnitsKwh() != null ? e.getUnitsKwh() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal cost = energyList.stream().map(e -> e.getCost() != null ? e.getCost() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            pDTO.setTotalEnergyKwh(kwh);
            pDTO.setTotalEnergyCost(cost);

            pDTO.setSafetyIncidentCount(safetyIncidentRepository.countByPlantId(plant.getPlantId()));

            plantDTOs.add(pDTO);
        }
        response.setPlants(plantDTOs);

        // 4. Departments Breakdown
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentSummaryDTO> deptDTOs = new ArrayList<>();
        for (Department d : departments) {
            DepartmentSummaryDTO dDTO = new DepartmentSummaryDTO();
            dDTO.setDeptId(d.getDeptId());
            dDTO.setDeptName(d.getDeptName());
            dDTO.setLocation(d.getLocation());
            dDTO.setBudget(d.getBudget());
            dDTO.setHeadcount(employeeRepository.countByDeptId(d.getDeptId()));
            deptDTOs.add(dDTO);
        }
        response.setDepartments(deptDTOs);

        // 5. Product Profitability Margins
        List<Product> products = productRepository.findAll();
        List<ProductProfitabilityDTO> marginDTOs = new ArrayList<>();
        for (Product p : products) {
            ProductProfitabilityDTO mDTO = new ProductProfitabilityDTO();
            mDTO.setProductId(p.getProductId());
            mDTO.setProductName(p.getProductName());
            mDTO.setSku(p.getSku());
            mDTO.setUnitPrice(p.getUnitPrice());
            mDTO.setStandardCost(p.getStandardCost());
            mDTO.setStockQty(p.getStockQty());

            if (p.getUnitPrice() != null && p.getStandardCost() != null) {
                BigDecimal margin = p.getUnitPrice().subtract(p.getStandardCost());
                mDTO.setMarginPerUnit(margin);
                if (p.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                    double pct = margin.multiply(BigDecimal.valueOf(100))
                            .divide(p.getUnitPrice(), 2, RoundingMode.HALF_UP)
                            .doubleValue();
                    mDTO.setMarginPercent(pct);
                }
            }

            marginDTOs.add(mDTO);
        }
        response.setProductMargins(marginDTOs);

        return response;
    }
}

