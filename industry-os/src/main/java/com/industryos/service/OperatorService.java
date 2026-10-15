package com.industryos.service;

import com.industryos.dto.response.OperatorMachineInventoryResponse;
import com.industryos.dto.response.OperatorMachineInventoryResponse.*;
import com.industryos.entity.*;
import com.industryos.exception.ResourceNotFoundException;
import com.industryos.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class OperatorService {

    private final UserAccountRepository userAccountRepository;
    private final MachineRepository machineRepository;
    private final ProductionLineRepository productionLineRepository;
    private final PlantRepository plantRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final MaintenancePartUsageRepository maintenancePartUsageRepository;
    private final SparePartRepository sparePartRepository;

    public OperatorService(UserAccountRepository userAccountRepository,
                           MachineRepository machineRepository,
                           ProductionLineRepository productionLineRepository,
                           PlantRepository plantRepository,
                           MaintenanceLogRepository maintenanceLogRepository,
                           MaintenancePartUsageRepository maintenancePartUsageRepository,
                           SparePartRepository sparePartRepository) {
        this.userAccountRepository = userAccountRepository;
        this.machineRepository = machineRepository;
        this.productionLineRepository = productionLineRepository;
        this.plantRepository = plantRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.maintenancePartUsageRepository = maintenancePartUsageRepository;
        this.sparePartRepository = sparePartRepository;
    }

    public OperatorMachineInventoryResponse getOperatorMachineInventory(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Operator user not found: " + username));

        Integer machineId = user.getAssignedMachineId();
        if (machineId == null) {
            // Default to machine 1 for CEO/GM or unassigned operator demo
            machineId = 1;
        }

        final Integer finalMachineId = machineId;
        Machine machine = machineRepository.findById(finalMachineId)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned machine not found with ID: " + finalMachineId));

        OperatorMachineInventoryResponse response = new OperatorMachineInventoryResponse();
        response.setOperatorUsername(user.getUsername());
        response.setOperatorFullName(user.getFullName());

        // 1. Machine Details
        MachineDetailDTO machineDetail = new MachineDetailDTO();
        machineDetail.setMachineId(machine.getMachineId());
        machineDetail.setMachineName(machine.getMachineName());
        machineDetail.setModelNo(machine.getModelNo());
        machineDetail.setManufacturer(machine.getManufacturer());
        machineDetail.setPurchaseDate(machine.getPurchaseDate());
        machineDetail.setPurchaseCost(machine.getPurchaseCost());
        machineDetail.setLastService(machine.getLastService());
        machineDetail.setOperationalStatus(machine.getStatus());

        if (machine.getLineId() != null) {
            productionLineRepository.findById(machine.getLineId()).ifPresent(line -> {
                machineDetail.setLineName(line.getLineName());
                if (line.getPlantId() != null) {
                    plantRepository.findById(line.getPlantId()).ifPresent(plant -> {
                        machineDetail.setPlantName(plant.getPlantName() + " (" + plant.getCity() + ")");
                    });
                }
            });
        }
        response.setMachine(machineDetail);

        // 2. Maintenance Logs & Machine Health
        List<MaintenanceLog> maintLogs = maintenanceLogRepository.findByMachineIdOrderByMaintDateDesc(machineId);

        BigDecimal totalDowntime = BigDecimal.ZERO;
        BigDecimal totalLabour = BigDecimal.ZERO;
        BigDecimal totalPartsCost = BigDecimal.ZERO;

        List<Integer> maintIds = new ArrayList<>();
        List<MaintenanceSummaryDTO> recentHistory = new ArrayList<>();

        for (int i = 0; i < maintLogs.size(); i++) {
            MaintenanceLog log = maintLogs.get(i);
            maintIds.add(log.getMaintId());
            if (log.getDowntimeHours() != null) {
                totalDowntime = totalDowntime.add(log.getDowntimeHours());
            }
            if (log.getLabourCost() != null) {
                totalLabour = totalLabour.add(log.getLabourCost());
            }

            if (i < 5) {
                MaintenanceSummaryDTO logDTO = new MaintenanceSummaryDTO();
                logDTO.setMaintId(log.getMaintId());
                logDTO.setMaintType(log.getMaintType());
                logDTO.setMaintDate(log.getMaintDate());
                logDTO.setDowntimeHours(log.getDowntimeHours());
                logDTO.setLabourCost(log.getLabourCost());
                logDTO.setDescription(log.getDescription());
                recentHistory.add(logDTO);
            }
        }
        response.setRecentMaintenanceHistory(recentHistory);

        // 3. Spare Parts Used on this Machine
        Map<Integer, Integer> partUsageMap = new HashMap<>();
        if (!maintIds.isEmpty()) {
            List<MaintenancePartUsage> usages = maintenancePartUsageRepository.findByIdMaintIdIn(maintIds);
            for (MaintenancePartUsage usage : usages) {
                Integer pId = usage.getId().getPartId();
                partUsageMap.put(pId, partUsageMap.getOrDefault(pId, 0) + usage.getQtyUsed());
            }
        }

        // Fetch all spare parts or those used on this machine
        List<SparePart> relevantParts;
        if (!partUsageMap.isEmpty()) {
            relevantParts = sparePartRepository.findAllById(partUsageMap.keySet());
        } else {
            // If no prior part usage logged, provide top critical spare parts as baseline
            relevantParts = sparePartRepository.findAll().stream().limit(5).toList();
        }

        List<MachineSparePartDTO> sparePartDTOs = new ArrayList<>();
        for (SparePart part : relevantParts) {
            MachineSparePartDTO partDTO = new MachineSparePartDTO();
            partDTO.setPartId(part.getPartId());
            partDTO.setPartName(part.getPartName());
            partDTO.setUnitCost(part.getUnitCost());
            partDTO.setStockQty(part.getStockQty());
            partDTO.setReorderLevel(part.getReorderLevel());

            int used = partUsageMap.getOrDefault(part.getPartId(), 0);
            partDTO.setTotalUsedOnThisMachine(used);

            if (part.getStockQty() <= 0) {
                partDTO.setStockStatus("OUT_OF_STOCK");
                partDTO.setReorderRecommended(true);
            } else if (part.getStockQty() <= part.getReorderLevel()) {
                partDTO.setStockStatus("LOW_STOCK");
                partDTO.setReorderRecommended(true);
            } else {
                partDTO.setStockStatus("ADEQUATE");
                partDTO.setReorderRecommended(false);
            }

            if (part.getUnitCost() != null && used > 0) {
                totalPartsCost = totalPartsCost.add(part.getUnitCost().multiply(BigDecimal.valueOf(used)));
            }

            sparePartDTOs.add(partDTO);
        }
        response.setSparePartsInventory(sparePartDTOs);

        // 4. Calculate Health Score
        MachineHealthDTO health = new MachineHealthDTO();
        health.setTotalMaintenanceJobs(maintLogs.size());
        health.setTotalDowntimeHours(totalDowntime);
        health.setTotalLabourCost(totalLabour);
        health.setTotalPartsCost(totalPartsCost);

        long daysSinceService = 0;
        if (machine.getLastService() != null) {
            daysSinceService = ChronoUnit.DAYS.between(machine.getLastService(), LocalDate.now());
            if (daysSinceService < 0) daysSinceService = 0;
        }
        health.setDaysSinceLastService(daysSinceService);

        int score = 100;
        if ("Under Maintenance".equalsIgnoreCase(machine.getStatus())) {
            score -= 35;
        } else if ("Decommissioned".equalsIgnoreCase(machine.getStatus())) {
            score = 0;
        }

        // Penalize for heavy downtime
        score -= Math.min(30, totalDowntime.intValue() * 2);

        // Penalize if service is overdue (> 90 days)
        if (daysSinceService > 90) {
            score -= Math.min(20, (int) ((daysSinceService - 90) / 10));
        }

        score = Math.max(10, Math.min(100, score));
        health.setHealthScore(score);

        if (score >= 85) {
            health.setHealthStatus("EXCELLENT");
            health.setRecommendation("Machine is operating smoothly. All mechanical tolerances within normal specifications.");
        } else if (score >= 70) {
            health.setHealthStatus("GOOD");
            health.setRecommendation("Regular operating health. Ensure coolant level check at shift handoff.");
        } else if (score >= 45) {
            health.setHealthStatus("ATTENTION_REQUIRED");
            health.setRecommendation("Preventive inspection recommended due to elevated accumulated downtime.");
        } else {
            health.setHealthStatus("CRITICAL");
            health.setRecommendation("Urgent maintenance required. Check bearing vibration and hydraulic seals immediately.");
        }

        response.setHealth(health);

        return response;
    }
}

