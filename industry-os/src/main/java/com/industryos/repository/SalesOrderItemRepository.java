package com.industryos.repository;

import com.industryos.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Integer> {
    List<SalesOrderItem> findBySoId(Integer soId);
    List<SalesOrderItem> findBySoIdIn(List<Integer> soIds);
}

