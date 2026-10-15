package com.industryos.dto.response;

import com.industryos.entity.Role;
import java.math.BigDecimal;
import java.util.List;

public class ExecutiveDashboardResponse {

    private String executiveName;
    private Role role;
    private CompanyOverviewDTO companyOverview;
    private GlobalFinancialsDTO globalFinancials;
    private List<PlantOverviewDTO> plants;
    private List<DepartmentSummaryDTO> departments;
    private List<ProductProfitabilityDTO> productMargins;

    public ExecutiveDashboardResponse() {}

    public static class CompanyOverviewDTO {
        private long totalEmployees;
        private int totalPlants;
        private int totalProductionLines;
        private int totalMachines;
        private long operationalMachines;
        private long underMaintenanceMachines;
        private int totalActiveCustomers;
        private int totalContractors;

        public CompanyOverviewDTO() {}

        public long getTotalEmployees() { return totalEmployees; }
        public void setTotalEmployees(long totalEmployees) { this.totalEmployees = totalEmployees; }

        public int getTotalPlants() { return totalPlants; }
        public void setTotalPlants(int totalPlants) { this.totalPlants = totalPlants; }

        public int getTotalProductionLines() { return totalProductionLines; }
        public void setTotalProductionLines(int totalProductionLines) { this.totalProductionLines = totalProductionLines; }

        public int getTotalMachines() { return totalMachines; }
        public void setTotalMachines(int totalMachines) { this.totalMachines = totalMachines; }

        public long getOperationalMachines() { return operationalMachines; }
        public void setOperationalMachines(long operationalMachines) { this.operationalMachines = operationalMachines; }

        public long getUnderMaintenanceMachines() { return underMaintenanceMachines; }
        public void setUnderMaintenanceMachines(long underMaintenanceMachines) { this.underMaintenanceMachines = underMaintenanceMachines; }

        public int getTotalActiveCustomers() { return totalActiveCustomers; }
        public void setTotalActiveCustomers(int totalActiveCustomers) { this.totalActiveCustomers = totalActiveCustomers; }

        public int getTotalContractors() { return totalContractors; }
        public void setTotalContractors(int totalContractors) { this.totalContractors = totalContractors; }
    }

    public static class GlobalFinancialsDTO {
        private BigDecimal totalSalesRevenue;
        private BigDecimal totalInvoicedAmount;
        private BigDecimal totalCollectedRevenue;
        private BigDecimal totalAccountsReceivable;
        private BigDecimal totalDepartmentBudgets;
        private BigDecimal totalMaintenanceCost;
        private BigDecimal totalFleetCapacityTons;

        public GlobalFinancialsDTO() {}

        public BigDecimal getTotalSalesRevenue() { return totalSalesRevenue; }
        public void setTotalSalesRevenue(BigDecimal totalSalesRevenue) { this.totalSalesRevenue = totalSalesRevenue; }

        public BigDecimal getTotalInvoicedAmount() { return totalInvoicedAmount; }
        public void setTotalInvoicedAmount(BigDecimal totalInvoicedAmount) { this.totalInvoicedAmount = totalInvoicedAmount; }

        public BigDecimal getTotalCollectedRevenue() { return totalCollectedRevenue; }
        public void setTotalCollectedRevenue(BigDecimal totalCollectedRevenue) { this.totalCollectedRevenue = totalCollectedRevenue; }

        public BigDecimal getTotalAccountsReceivable() { return totalAccountsReceivable; }
        public void setTotalAccountsReceivable(BigDecimal totalAccountsReceivable) { this.totalAccountsReceivable = totalAccountsReceivable; }

        public BigDecimal getTotalDepartmentBudgets() { return totalDepartmentBudgets; }
        public void setTotalDepartmentBudgets(BigDecimal totalDepartmentBudgets) { this.totalDepartmentBudgets = totalDepartmentBudgets; }

        public BigDecimal getTotalMaintenanceCost() { return totalMaintenanceCost; }
        public void setTotalMaintenanceCost(BigDecimal totalMaintenanceCost) { this.totalMaintenanceCost = totalMaintenanceCost; }

        public BigDecimal getTotalFleetCapacityTons() { return totalFleetCapacityTons; }
        public void setTotalFleetCapacityTons(BigDecimal totalFleetCapacityTons) { this.totalFleetCapacityTons = totalFleetCapacityTons; }
    }

    public static class PlantOverviewDTO {
        private Integer plantId;
        private String plantName;
        private String city;
        private String state;
        private Integer dailyCapacity;
        private long employeeCount;
        private int productionLinesCount;
        private int machinesCount;
        private BigDecimal totalEnergyKwh;
        private BigDecimal totalEnergyCost;
        private long safetyIncidentCount;

        public PlantOverviewDTO() {}

        public Integer getPlantId() { return plantId; }
        public void setPlantId(Integer plantId) { this.plantId = plantId; }

        public String getPlantName() { return plantName; }
        public void setPlantName(String plantName) { this.plantName = plantName; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public Integer getDailyCapacity() { return dailyCapacity; }
        public void setDailyCapacity(Integer dailyCapacity) { this.dailyCapacity = dailyCapacity; }

        public long getEmployeeCount() { return employeeCount; }
        public void setEmployeeCount(long employeeCount) { this.employeeCount = employeeCount; }

        public int getProductionLinesCount() { return productionLinesCount; }
        public void setProductionLinesCount(int productionLinesCount) { this.productionLinesCount = productionLinesCount; }

        public int getMachinesCount() { return machinesCount; }
        public void setMachinesCount(int machinesCount) { this.machinesCount = machinesCount; }

        public BigDecimal getTotalEnergyKwh() { return totalEnergyKwh; }
        public void setTotalEnergyKwh(BigDecimal totalEnergyKwh) { this.totalEnergyKwh = totalEnergyKwh; }

        public BigDecimal getTotalEnergyCost() { return totalEnergyCost; }
        public void setTotalEnergyCost(BigDecimal totalEnergyCost) { this.totalEnergyCost = totalEnergyCost; }

        public long getSafetyIncidentCount() { return safetyIncidentCount; }
        public void setSafetyIncidentCount(long safetyIncidentCount) { this.safetyIncidentCount = safetyIncidentCount; }
    }

    public static class DepartmentSummaryDTO {
        private Integer deptId;
        private String deptName;
        private String location;
        private BigDecimal budget;
        private long headcount;

        public DepartmentSummaryDTO() {}

        public Integer getDeptId() { return deptId; }
        public void setDeptId(Integer deptId) { this.deptId = deptId; }

        public String getDeptName() { return deptName; }
        public void setDeptName(String deptName) { this.deptName = deptName; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public BigDecimal getBudget() { return budget; }
        public void setBudget(BigDecimal budget) { this.budget = budget; }

        public long getHeadcount() { return headcount; }
        public void setHeadcount(long headcount) { this.headcount = headcount; }
    }

    public static class ProductProfitabilityDTO {
        private Integer productId;
        private String productName;
        private String sku;
        private BigDecimal unitPrice;
        private BigDecimal standardCost;
        private BigDecimal marginPerUnit;
        private double marginPercent;
        private Integer stockQty;

        public ProductProfitabilityDTO() {}

        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getStandardCost() { return standardCost; }
        public void setStandardCost(BigDecimal standardCost) { this.standardCost = standardCost; }

        public BigDecimal getMarginPerUnit() { return marginPerUnit; }
        public void setMarginPerUnit(BigDecimal marginPerUnit) { this.marginPerUnit = marginPerUnit; }

        public double getMarginPercent() { return marginPercent; }
        public void setMarginPercent(double marginPercent) { this.marginPercent = marginPercent; }

        public Integer getStockQty() { return stockQty; }
        public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    }

    public String getExecutiveName() { return executiveName; }
    public void setExecutiveName(String executiveName) { this.executiveName = executiveName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public CompanyOverviewDTO getCompanyOverview() { return companyOverview; }
    public void setCompanyOverview(CompanyOverviewDTO companyOverview) { this.companyOverview = companyOverview; }

    public GlobalFinancialsDTO getGlobalFinancials() { return globalFinancials; }
    public void setGlobalFinancials(GlobalFinancialsDTO globalFinancials) { this.globalFinancials = globalFinancials; }

    public List<PlantOverviewDTO> getPlants() { return plants; }
    public void setPlants(List<PlantOverviewDTO> plants) { this.plants = plants; }

    public List<DepartmentSummaryDTO> getDepartments() { return departments; }
    public void setDepartments(List<DepartmentSummaryDTO> departments) { this.departments = departments; }

    public List<ProductProfitabilityDTO> getProductMargins() { return productMargins; }
    public void setProductMargins(List<ProductProfitabilityDTO> productMargins) { this.productMargins = productMargins; }
}

