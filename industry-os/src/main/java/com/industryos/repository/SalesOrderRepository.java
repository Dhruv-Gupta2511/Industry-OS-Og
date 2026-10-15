package com.industryos.repository;

import com.industryos.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Integer> {
    List<SalesOrder> findByCustomerId(Integer customerId);
    List<SalesOrder> findByCustomerIdOrderByOrderDateDesc(Integer customerId);
    List<SalesOrder> findByCustomerIdAndStatus(Integer customerId, String status);
    List<SalesOrder> findByStatus(String status);
}

