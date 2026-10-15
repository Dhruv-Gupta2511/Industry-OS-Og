package com.industryos.repository;

import com.industryos.entity.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Integer> {
    List<MaintenanceLog> findByMachineId(Integer machineId);
    List<MaintenanceLog> findByMachineIdIn(List<Integer> machineIds);
    List<MaintenanceLog> findByMachineIdOrderByMaintDateDesc(Integer machineId);
}

