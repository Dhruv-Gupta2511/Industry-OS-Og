package com.industryos.repository;

import com.industryos.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SparePartRepository extends JpaRepository<SparePart, Integer> {
    List<SparePart> findByPartIdIn(List<Integer> partIds);
    List<SparePart> findByStockQtyLessThanEqual(Integer reorderLevel);
}

