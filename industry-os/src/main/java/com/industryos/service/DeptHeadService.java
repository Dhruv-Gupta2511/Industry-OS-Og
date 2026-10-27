package com.industryos.service;

import com.industryos.dto.response.DeptHeadAggregateResponse;
import com.industryos.dto.response.DeptHeadAggregateResponse.*;
import com.industryos.entity.*;
import com.industryos.exception.ResourceNotFoundException;
import com.industryos.exception.UnauthorizedException;
import com.industryos.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DeptHeadService {

    private final UserAccountRepository userAccountRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;
    private final ShiftRepository shiftRepository;
    private final MachineRepository machineRepository;
    private final ProductionLineRepository productionLineRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ProductionBatchRepository productionBatchRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final ProductRepository productRepository;

    public DeptHeadService(UserAccountRepository userAccountRepository,
                           DepartmentRepository departmentRepository,
                           EmployeeRepository employeeRepository,
                           DesignationRepository designationRepository,
                           ShiftRepository shiftRepository,
                           MachineRepository machineRepository,
                           ProductionLineRepository productionLineRepository,
                           WorkOrderRepository workOrderRepository,
                           ProductionBatchRepository productionBatchRepository,
                           MaintenanceLogRepository maintenanceLogRepository,
                           ProductRepository productRepository) {
        this.userAccountRepository = userAccountRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.designationRepository = designationRepository;
        this.shiftRepository = shiftRepository;
        this.machineRepository = machineRepository;
        this.productionLineRepository = productionLineRepository;
        this.workOrderRepository = workOrderRepository;
        this.productionBatchRepository = productionBatchRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.productRepository = productRepository;
    }

    public DeptHeadAggregateResponse getDepartmentAggregate(String username, Integer requestedDeptId) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Integer targetDeptId;

        if (user.getRole() == Role.DEPT_HEAD) {
            // Strict role restriction: DEPT_HEAD can ONLY view their own assigned department
            if (user.getDepartmentId() == null) {
                throw new UnauthorizedException("Department Head user has no assigned department.");
            }
            if (requestedDeptId != null && !requestedDeptId.equals(user.getDepartmentId())) {
                throw new UnauthorizedException("Access Denied: You are only authorized to view data for your own department (ID: " + user.getDepartmentId() + ")");
            }
            targetDeptId = user.getDepartmentId();
        } else if (user.getRole() == Role.CEO || user.getRole() == Role.GENERAL_MANAGER) {
            // Unrestricted executive access: can view any requested department or default to 1 (Production)
            targetDeptId = (requestedDeptId != null) ? requestedDeptId : (user.getDepartmentId() != null ? user.getDepartmentId() : 1);
        } else {
            throw new UnauthorizedException("Insufficient privileges to access department aggregate data.");
        }

        Department dept = departmentRepository.findById(targetDeptId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + targetDeptId));

        DeptHeadAggregateResponse response = new DeptHeadAggregateResponse();
        response.setDepartmentId(dept.getDeptId());
        response.setDepartmentName(dept.getDeptName());
        response.setLocation(dept.getLocation());
        response.setBudget(dept.getBudget());
        response.setDepartmentHeadName(user.getFullName());

        // 1. Employees in this department
        List<Employee> deptEmployees = employeeRepository.findByDeptId(targetDeptId);

        long activeStaff = 0;
        long onLeaveStaff = 0;
        long resignedStaff = 0;

        List<DepartmentEmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee emp : deptEmployees) {
            if ("Active".equalsIgnoreCase(emp.getStatus())) activeStaff++;
            else if ("On Leave".equalsIgnoreCase(emp.getStatus())) onLeaveStaff++;
            else if ("Resigned".equalsIgnoreCase(emp.getStatus())) resignedStaff++;

            DepartmentEmployeeDTO empDTO = new DepartmentEmployeeDTO();
            empDTO.setEmpId(emp.getEmpId());
            empDTO.setFullName(emp.getFullName());
            empDTO.setEmail(emp.getEmail());
            empDTO.setPhone(emp.getPhone());
            empDTO.setHireDate(emp.getHireDate());
            empDTO.setStatus(emp.getStatus());

            if (emp.getDesigId() != null) {
                designationRepository.findById(emp.getDesigId()).ifPresent(d -> {
                    empDTO.setDesignation(d.getTitle());
                    empDTO.setGrade(d.getGrade());
                });
            }

            if (emp.getShiftId() != null) {
                shiftRepository.findById(emp.getShiftId()).ifPresent(s -> {
                    empDTO.setShiftName(s.getShiftName());
                });
            }

            employeeDTOs.add(empDTO);
        }

        response.setStaffOverview(new StaffOverviewDTO(deptEmployees.size(), activeStaff, onLeaveStaff, resignedStaff));
        response.setEmployees(employeeDTOs);

        // 2. Machines tied to this department / relevant production lines
        List<Machine> relevantMachines;
        List<DepartmentMachineDTO> machineDTOs = new ArrayList<>();
        List<ProductionLine> lines = productionLineRepository.findAll();
        Map<Integer, String> lineNames = new HashMap<>();
        for (ProductionLine l : lines) {
            lineNames.put(l.getLineId(), l.getLineName());
        }

        if (targetDeptId == 1 || targetDeptId == 2) {
            // Production or Maintenance has access to the manufacturing machinery
            relevantMachines = machineRepository.findAll();
        } else {
            // Scoped machinery or sample subset
            relevantMachines = machineRepository.findAll().stream().limit(5).toList();
        }

        for (Machine m : relevantMachines) {
            DepartmentMachineDTO mDTO = new DepartmentMachineDTO();
            mDTO.setMachineId(m.getMachineId());
            mDTO.setMachineName(m.getMachineName());
            mDTO.setModelNo(m.getModelNo());
            mDTO.setManufacturer(m.getManufacturer());
            mDTO.setStatus(m.getStatus());
            mDTO.setLastService(m.getLastService());
            mDTO.setLineName(lineNames.getOrDefault(m.getLineId(), "Line " + m.getLineId()));
            machineDTOs.add(mDTO);
        }
        response.setMachines(machineDTOs);

        // 3. Work Orders / Orders tied to this department
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        List<DepartmentWorkOrderDTO> woDTOs = new ArrayList<>();

        for (WorkOrder wo : workOrders) {
            DepartmentWorkOrderDTO woDTO = new DepartmentWorkOrderDTO();
            woDTO.setWoId(wo.getWoId());
            woDTO.setProductId(wo.getProductId());
            woDTO.setPlannedQty(wo.getPlannedQty());
            woDTO.setStartDate(wo.getStartDate());
            woDTO.setEndDate(wo.getEndDate());
            woDTO.setStatus(wo.getStatus());

            productRepository.findById(wo.getProductId()).ifPresent(p -> {
                woDTO.setProductName(p.getProductName());
            });

            // Calculate batches output
            List<ProductionBatch> batches = productionBatchRepository.findByWoId(wo.getWoId());
            int good = 0;
            int rejected = 0;
            for (ProductionBatch b : batches) {
                good += (b.getGoodQty() != null ? b.getGoodQty() : 0);
                rejected += (b.getRejectedQty() != null ? b.getRejectedQty() : 0);
            }
            woDTO.setGoodProducedQty(good);
            woDTO.setRejectedQty(rejected);

            if (wo.getPlannedQty() != null && wo.getPlannedQty() > 0) {
                double pct = (good * 100.0) / wo.getPlannedQty();
                woDTO.setProgressPercent(Math.round(pct * 10.0) / 10.0);
            } else {
                woDTO.setProgressPercent(0.0);
            }

            woDTOs.add(woDTO);
        }
        response.setWorkOrders(woDTOs);

        // 4. Maintenance logs for department
        List<MaintenanceLog> maintLogs = maintenanceLogRepository.findAll().stream().limit(10).toList();
        List<DepartmentMaintenanceDTO> maintDTOs = new ArrayList<>();
        Map<Integer, String> machineNames = new HashMap<>();
        for (Machine m : relevantMachines) {
            machineNames.put(m.getMachineId(), m.getMachineName());
        }

        for (MaintenanceLog ml : maintLogs) {
            DepartmentMaintenanceDTO logDTO = new DepartmentMaintenanceDTO();
            logDTO.setMaintId(ml.getMaintId());
            logDTO.setMachineId(ml.getMachineId());
            logDTO.setMachineName(machineNames.getOrDefault(ml.getMachineId(), "Machine " + ml.getMachineId()));
            logDTO.setMaintType(ml.getMaintType());
            logDTO.setMaintDate(ml.getMaintDate());
            logDTO.setDowntimeHours(ml.getDowntimeHours());
            logDTO.setLabourCost(ml.getLabourCost());
            logDTO.setDescription(ml.getDescription());
            maintDTOs.add(logDTO);
        }
        response.setMaintenanceLogs(maintDTOs);

        // 5. Aggregate Department Metrics
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("allocatedBudget", dept.getBudget());
        metrics.put("totalEmployees", deptEmployees.size());
        if (!deptEmployees.isEmpty() && dept.getBudget() != null) {
            metrics.put("budgetPerEmployee", dept.getBudget().divide(BigDecimal.valueOf(deptEmployees.size()), 2, RoundingMode.HALF_UP));
        }
        metrics.put("activeStaffRatio", deptEmployees.isEmpty() ? "0%" : Math.round((activeStaff * 100.0) / deptEmployees.size()) + "%");
        metrics.put("departmentRoleScope", dept.getDeptName().toUpperCase() + "_AGGREGATE_ONLY");

        response.setAggregateMetrics(metrics);

        return response;
    }
}

