package com.industryos.repository;

import com.industryos.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Integer> {
    List<WorkOrder> findByLineId(Integer lineId);
    List<WorkOrder> findByLineIdIn(List<Integer> lineIds);
    List<WorkOrder> findByStatus(String status);
}

