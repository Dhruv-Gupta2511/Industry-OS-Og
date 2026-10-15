package com.industryos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OperatorMachineInventoryResponse {

    private String operatorUsername;
    private String operatorFullName;
    private MachineDetailDTO machine;
    private MachineHealthDTO health;
    private List<MachineSparePartDTO> sparePartsInventory;
    private List<MaintenanceSummaryDTO> recentMaintenanceHistory;

    public OperatorMachineInventoryResponse() {}

    public static class MachineDetailDTO {
        private Integer machineId;
        private String machineName;
        private String modelNo;
        private String manufacturer;
        private String lineName;
        private String plantName;
        private LocalDate purchaseDate;
        private BigDecimal purchaseCost;
        private LocalDate lastService;
        private String operationalStatus;

        public MachineDetailDTO() {}

        public Integer getMachineId() { return machineId; }
        public void setMachineId(Integer machineId) { this.machineId = machineId; }

        public String getMachineName() { return machineName; }
        public void setMachineName(String machineName) { this.machineName = machineName; }

        public String getModelNo() { return modelNo; }
        public void setModelNo(String modelNo) { this.modelNo = modelNo; }

        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

        public String getLineName() { return lineName; }
        public void setLineName(String lineName) { this.lineName = lineName; }

        public String getPlantName() { return plantName; }
        public void setPlantName(String plantName) { this.plantName = plantName; }

        public LocalDate getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

        public BigDecimal getPurchaseCost() { return purchaseCost; }
        public void setPurchaseCost(BigDecimal purchaseCost) { this.purchaseCost = purchaseCost; }

        public LocalDate getLastService() { return lastService; }
        public void setLastService(LocalDate lastService) { this.lastService = lastService; }

        public String getOperationalStatus() { return operationalStatus; }
        public void setOperationalStatus(String operationalStatus) { this.operationalStatus = operationalStatus; }
    }

    public static class MachineHealthDTO {
        private int healthScore; // 0-100
        private String healthStatus; // EXCELLENT, GOOD, ATTENTION_REQUIRED, CRITICAL
        private long totalMaintenanceJobs;
        private BigDecimal totalDowntimeHours;
        private BigDecimal totalLabourCost;
        private BigDecimal totalPartsCost;
        private long daysSinceLastService;
        private String recommendation;

        public MachineHealthDTO() {}

        public int getHealthScore() { return healthScore; }
        public void setHealthScore(int healthScore) { this.healthScore = healthScore; }

        public String getHealthStatus() { return healthStatus; }
        public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

        public long getTotalMaintenanceJobs() { return totalMaintenanceJobs; }
        public void setTotalMaintenanceJobs(long totalMaintenanceJobs) { this.totalMaintenanceJobs = totalMaintenanceJobs; }

        public BigDecimal getTotalDowntimeHours() { return totalDowntimeHours; }
        public void setTotalDowntimeHours(BigDecimal totalDowntimeHours) { this.totalDowntimeHours = totalDowntimeHours; }

        public BigDecimal getTotalLabourCost() { return totalLabourCost; }
        public void setTotalLabourCost(BigDecimal totalLabourCost) { this.totalLabourCost = totalLabourCost; }

        public BigDecimal getTotalPartsCost() { return totalPartsCost; }
        public void setTotalPartsCost(BigDecimal totalPartsCost) { this.totalPartsCost = totalPartsCost; }

        public long getDaysSinceLastService() { return daysSinceLastService; }
        public void setDaysSinceLastService(long daysSinceLastService) { this.daysSinceLastService = daysSinceLastService; }

        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    }

    public static class MachineSparePartDTO {
        private Integer partId;
        private String partName;
        private BigDecimal unitCost;
        private Integer stockQty;
        private Integer reorderLevel;
        private String stockStatus; // ADEQUATE, LOW_STOCK, OUT_OF_STOCK
        private Integer totalUsedOnThisMachine;
        private boolean reorderRecommended;

        public MachineSparePartDTO() {}

        public Integer getPartId() { return partId; }
        public void setPartId(Integer partId) { this.partId = partId; }

        public String getPartName() { return partName; }
        public void setPartName(String partName) { this.partName = partName; }

        public BigDecimal getUnitCost() { return unitCost; }
        public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

        public Integer getStockQty() { return stockQty; }
        public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }

        public Integer getReorderLevel() { return reorderLevel; }
        public void setReorderLevel(Integer reorderLevel) { this.reorderLevel = reorderLevel; }

        public String getStockStatus() { return stockStatus; }
        public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }

        public Integer getTotalUsedOnThisMachine() { return totalUsedOnThisMachine; }
        public void setTotalUsedOnThisMachine(Integer totalUsedOnThisMachine) { this.totalUsedOnThisMachine = totalUsedOnThisMachine; }

        public boolean isReorderRecommended() { return reorderRecommended; }
        public void setReorderRecommended(boolean reorderRecommended) { this.reorderRecommended = reorderRecommended; }
    }

    public static class MaintenanceSummaryDTO {
        private Integer maintId;
        private String maintType;
        private LocalDate maintDate;
        private BigDecimal downtimeHours;
        private BigDecimal labourCost;
        private String description;

        public MaintenanceSummaryDTO() {}

        public Integer getMaintId() { return maintId; }
        public void setMaintId(Integer maintId) { this.maintId = maintId; }

        public String getMaintType() { return maintType; }
        public void setMaintType(String maintType) { this.maintType = maintType; }

        public LocalDate getMaintDate() { return maintDate; }
        public void setMaintDate(LocalDate maintDate) { this.maintDate = maintDate; }

        public BigDecimal getDowntimeHours() { return downtimeHours; }
        public void setDowntimeHours(BigDecimal downtimeHours) { this.downtimeHours = downtimeHours; }

        public BigDecimal getLabourCost() { return labourCost; }
        public void setLabourCost(BigDecimal labourCost) { this.labourCost = labourCost; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public String getOperatorUsername() { return operatorUsername; }
    public void setOperatorUsername(String operatorUsername) { this.operatorUsername = operatorUsername; }

    public String getOperatorFullName() { return operatorFullName; }
    public void setOperatorFullName(String operatorFullName) { this.operatorFullName = operatorFullName; }

    public MachineDetailDTO getMachine() { return machine; }
    public void setMachine(MachineDetailDTO machine) { this.machine = machine; }

    public MachineHealthDTO getHealth() { return health; }
    public void setHealth(MachineHealthDTO health) { this.health = health; }

    public List<MachineSparePartDTO> getSparePartsInventory() { return sparePartsInventory; }
    public void setSparePartsInventory(List<MachineSparePartDTO> sparePartsInventory) { this.sparePartsInventory = sparePartsInventory; }

    public List<MaintenanceSummaryDTO> getRecentMaintenanceHistory() { return recentMaintenanceHistory; }
    public void setRecentMaintenanceHistory(List<MaintenanceSummaryDTO> recentMaintenanceHistory) { this.recentMaintenanceHistory = recentMaintenanceHistory; }
}

