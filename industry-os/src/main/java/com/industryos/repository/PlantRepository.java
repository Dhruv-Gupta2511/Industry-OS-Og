package com.industryos.repository;

import com.industryos.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
    Optional<Plant> findByPlantName(String plantName);
}

