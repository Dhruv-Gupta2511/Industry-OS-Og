package com.industryos.repository;

import com.industryos.entity.ProductionBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, Integer> {
    List<ProductionBatch> findByWoId(Integer woId);
    List<ProductionBatch> findByWoIdIn(List<Integer> woIds);
}

