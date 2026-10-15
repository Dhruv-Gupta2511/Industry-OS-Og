package com.industryos.repository;

import com.industryos.entity.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Integer> {
    List<EnergyConsumption> findByPlantId(Integer plantId);
}

