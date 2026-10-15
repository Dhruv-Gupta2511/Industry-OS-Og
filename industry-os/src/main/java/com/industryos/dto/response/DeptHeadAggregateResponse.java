package com.industryos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DeptHeadAggregateResponse {

    private Integer departmentId;
    private String departmentName;
    private String location;
    private BigDecimal budget;
    private String departmentHeadName;

    private StaffOverviewDTO staffOverview;
    private List<DepartmentEmployeeDTO> employees;
    private List<DepartmentMachineDTO> machines;
    private List<DepartmentWorkOrderDTO> workOrders;
    private List<DepartmentMaintenanceDTO> maintenanceLogs;
    private Map<String, Object> aggregateMetrics;

    public DeptHeadAggregateResponse() {}

    public static class StaffOverviewDTO {
        private long totalEmployees;
        private long activeStaff;
        private long onLeaveStaff;
        private long resignedStaff;

        public StaffOverviewDTO() {}

        public StaffOverviewDTO(long totalEmployees, long activeStaff, long onLeaveStaff, long resignedStaff) {
            this.totalEmployees = totalEmployees;
            this.activeStaff = activeStaff;
            this.onLeaveStaff = onLeaveStaff;
            this.resignedStaff = resignedStaff;
        }

        public long getTotalEmployees() { return totalEmployees; }
        public void setTotalEmployees(long totalEmployees) { this.totalEmployees = totalEmployees; }

        public long getActiveStaff() { return activeStaff; }
        public void setActiveStaff(long activeStaff) { this.activeStaff = activeStaff; }

        public long getOnLeaveStaff() { return onLeaveStaff; }
        public void setOnLeaveStaff(long onLeaveStaff) { this.onLeaveStaff = onLeaveStaff; }

        public long getResignedStaff() { return resignedStaff; }
        public void setResignedStaff(long resignedStaff) { this.resignedStaff = resignedStaff; }
    }

    public static class DepartmentEmployeeDTO {
        private Integer empId;
        private String fullName;
        private String email;
        private String phone;
        private String designation;
        private String grade;
        private String shiftName;
        private LocalDate hireDate;
        private String status;

        public DepartmentEmployeeDTO() {}

        public Integer getEmpId() { return empId; }
        public void setEmpId(Integer empId) { this.empId = empId; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }

        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }

        public String getShiftName() { return shiftName; }
        public void setShiftName(String shiftName) { this.shiftName = shiftName; }

        public LocalDate getHireDate() { return hireDate; }
        public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class DepartmentMachineDTO {
        private Integer machineId;
        private String machineName;
        private String lineName;
        private String modelNo;
        private String manufacturer;
        private String status;
        private LocalDate lastService;

        public DepartmentMachineDTO() {}

        public Integer getMachineId() { return machineId; }
        public void setMachineId(Integer machineId) { this.machineId = machineId; }

        public String getMachineName() { return machineName; }
        public void setMachineName(String machineName) { this.machineName = machineName; }

        public String getLineName() { return lineName; }
        public void setLineName(String lineName) { this.lineName = lineName; }

        public String getModelNo() { return modelNo; }
        public void setModelNo(String modelNo) { this.modelNo = modelNo; }

        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDate getLastService() { return lastService; }
        public void setLastService(LocalDate lastService) { this.lastService = lastService; }
    }

    public static class DepartmentWorkOrderDTO {
        private Integer woId;
        private Integer productId;
        private String productName;
        private Integer plannedQty;
        private Integer goodProducedQty;
        private Integer rejectedQty;
        private double progressPercent;
        private LocalDate startDate;
        private LocalDate endDate;
        private String status;

        public DepartmentWorkOrderDTO() {}

        public Integer getWoId() { return woId; }
        public void setWoId(Integer woId) { this.woId = woId; }

        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Integer getPlannedQty() { return plannedQty; }
        public void setPlannedQty(Integer plannedQty) { this.plannedQty = plannedQty; }

        public Integer getGoodProducedQty() { return goodProducedQty; }
        public void setGoodProducedQty(Integer goodProducedQty) { this.goodProducedQty = goodProducedQty; }

        public Integer getRejectedQty() { return rejectedQty; }
        public void setRejectedQty(Integer rejectedQty) { this.rejectedQty = rejectedQty; }

        public double getProgressPercent() { return progressPercent; }
        public void setProgressPercent(double progressPercent) { this.progressPercent = progressPercent; }

        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class DepartmentMaintenanceDTO {
        private Integer maintId;
        private Integer machineId;
        private String machineName;
        private String maintType;
        private LocalDate maintDate;
        private BigDecimal downtimeHours;
        private BigDecimal labourCost;
        private String description;

        public DepartmentMaintenanceDTO() {}

        public Integer getMaintId() { return maintId; }
        public void setMaintId(Integer maintId) { this.maintId = maintId; }

        public Integer getMachineId() { return machineId; }
        public void setMachineId(Integer machineId) { this.machineId = machineId; }

        public String getMachineName() { return machineName; }
        public void setMachineName(String machineName) { this.machineName = machineName; }

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

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    public String getDepartmentHeadName() { return departmentHeadName; }
    public void setDepartmentHeadName(String departmentHeadName) { this.departmentHeadName = departmentHeadName; }

    public StaffOverviewDTO getStaffOverview() { return staffOverview; }
    public void setStaffOverview(StaffOverviewDTO staffOverview) { this.staffOverview = staffOverview; }

    public List<DepartmentEmployeeDTO> getEmployees() { return employees; }
    public void setEmployees(List<DepartmentEmployeeDTO> employees) { this.employees = employees; }

    public List<DepartmentMachineDTO> getMachines() { return machines; }
    public void setMachines(List<DepartmentMachineDTO> machines) { this.machines = machines; }

    public List<DepartmentWorkOrderDTO> getWorkOrders() { return workOrders; }
    public void setWorkOrders(List<DepartmentWorkOrderDTO> workOrders) { this.workOrders = workOrders; }

    public List<DepartmentMaintenanceDTO> getMaintenanceLogs() { return maintenanceLogs; }
    public void setMaintenanceLogs(List<DepartmentMaintenanceDTO> maintenanceLogs) { this.maintenanceLogs = maintenanceLogs; }

    public Map<String, Object> getAggregateMetrics() { return aggregateMetrics; }
    public void setAggregateMetrics(Map<String, Object> aggregateMetrics) { this.aggregateMetrics = aggregateMetrics; }
}

