package com.industryos.repository;

import com.industryos.entity.SafetyIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafetyIncidentRepository extends JpaRepository<SafetyIncident, Integer> {
    List<SafetyIncident> findByPlantId(Integer plantId);
    long countByPlantId(Integer plantId);
}

