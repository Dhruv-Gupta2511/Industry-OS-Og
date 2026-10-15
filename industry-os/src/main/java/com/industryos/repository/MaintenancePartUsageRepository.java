package com.industryos.repository;

import com.industryos.entity.MaintenancePartUsage;
import com.industryos.entity.MaintenancePartUsageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenancePartUsageRepository extends JpaRepository<MaintenancePartUsage, MaintenancePartUsageId> {
    List<MaintenancePartUsage> findByIdMaintId(Integer maintId);
    List<MaintenancePartUsage> findByIdMaintIdIn(List<Integer> maintIds);
    List<MaintenancePartUsage> findByIdPartId(Integer partId);
}

