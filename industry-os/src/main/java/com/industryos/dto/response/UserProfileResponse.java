package com.industryos.dto.response;

import com.industryos.entity.Role;

public class UserProfileResponse {

    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private Role role;
    private Integer departmentId;
    private String departmentName;
    private String projectOrPlant;
    private String designation;
    private Integer employeeId;
    private Integer customerId;
    private Integer contractorId;
    private Integer assignedMachineId;
    private String assignedMachineName;

    public UserProfileResponse() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getProjectOrPlant() { return projectOrPlant; }
    public void setProjectOrPlant(String projectOrPlant) { this.projectOrPlant = projectOrPlant; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getContractorId() { return contractorId; }
    public void setContractorId(Integer contractorId) { this.contractorId = contractorId; }

    public Integer getAssignedMachineId() { return assignedMachineId; }
    public void setAssignedMachineId(Integer assignedMachineId) { this.assignedMachineId = assignedMachineId; }

    public String getAssignedMachineName() { return assignedMachineName; }
    public void setAssignedMachineName(String assignedMachineName) { this.assignedMachineName = assignedMachineName; }
}

