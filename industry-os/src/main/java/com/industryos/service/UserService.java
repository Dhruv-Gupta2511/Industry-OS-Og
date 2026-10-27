package com.industryos.service;

import com.industryos.dto.response.UserProfileResponse;
import com.industryos.entity.*;
import com.industryos.exception.ResourceNotFoundException;
import com.industryos.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;
    private final PlantRepository plantRepository;
    private final MachineRepository machineRepository;
    private final AuditLogRepository auditLogRepository;

    public UserService(UserAccountRepository userAccountRepository,
                       DepartmentRepository departmentRepository,
                       EmployeeRepository employeeRepository,
                       DesignationRepository designationRepository,
                       PlantRepository plantRepository,
                       MachineRepository machineRepository,
                       AuditLogRepository auditLogRepository) {
        this.userAccountRepository = userAccountRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.designationRepository = designationRepository;
        this.plantRepository = plantRepository;
        this.machineRepository = machineRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public UserProfileResponse getCurrentUserProfile(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        UserProfileResponse profile = new UserProfileResponse();
        profile.setUserId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setFullName(user.getFullName());
        profile.setEmail(user.getEmail());
        profile.setRole(user.getRole());
        profile.setDepartmentId(user.getDepartmentId());
        profile.setEmployeeId(user.getEmployeeId());
        profile.setCustomerId(user.getCustomerId());
        profile.setContractorId(user.getContractorId());
        profile.setAssignedMachineId(user.getAssignedMachineId());

        // Resolve Department details
        if (user.getDepartmentId() != null) {
            departmentRepository.findById(user.getDepartmentId()).ifPresent(dept -> {
                profile.setDepartmentName(dept.getDeptName());
                profile.setProjectOrPlant(dept.getLocation());
            });
        } else {
            profile.setDepartmentName("Executive Suite / Headquarters");
            profile.setProjectOrPlant("Corporate Head Office");
        }

        // Resolve Employee details if linked
        if (user.getEmployeeId() != null) {
            employeeRepository.findById(user.getEmployeeId()).ifPresent(emp -> {
                if (emp.getDesigId() != null) {
                    designationRepository.findById(emp.getDesigId()).ifPresent(desig -> {
                        profile.setDesignation(desig.getTitle() + " (" + desig.getGrade() + ")");
                    });
                }
                if (emp.getPlantId() != null) {
                    plantRepository.findById(emp.getPlantId()).ifPresent(plant -> {
                        profile.setProjectOrPlant(plant.getPlantName() + " (" + plant.getCity() + ")");
                    });
                }
            });
        }

        // Resolve Machine details if assigned
        if (user.getAssignedMachineId() != null) {
            machineRepository.findById(user.getAssignedMachineId()).ifPresent(machine -> {
                profile.setAssignedMachineName(machine.getMachineName() + " [" + machine.getModelNo() + "]");
            });
        }

        return profile;
    }

    @Transactional
    public void deleteUserAccount(String identifier) {
        UserAccount user = null;
        try {
            Long id = Long.parseLong(identifier);
            user = userAccountRepository.findById(id).orElse(null);
        } catch (NumberFormatException ignored) {}

        if (user == null) {
            user = userAccountRepository.findByUsername(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + identifier));
        }

        String prevVal = "User ID: " + user.getId() + ", Username: " + user.getUsername() + ", Role: " + user.getRole() + ", FullName: " + user.getFullName();

        // Audit Log Entry
        String auditId = "AUD-" + (System.currentTimeMillis() % 100000);
        AuditLog auditLog = new AuditLog(
                auditId,
                "DELETE",
                "USER_ACCOUNT",
                user.getUsername(),
                "Permanently deleted user account '" + user.getUsername() + "' (" + user.getRole() + ")",
                prevVal,
                "ACCOUNT_DELETED"
        );
        auditLogRepository.save(auditLog);

        userAccountRepository.delete(user);
    }
}
