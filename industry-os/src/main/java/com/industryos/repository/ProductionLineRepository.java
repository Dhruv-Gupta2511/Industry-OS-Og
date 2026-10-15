package com.industryos.repository;

import com.industryos.entity.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, Integer> {
    List<ProductionLine> findByPlantId(Integer plantId);
    List<ProductionLine> findByPlantIdIn(List<Integer> plantIds);
}

