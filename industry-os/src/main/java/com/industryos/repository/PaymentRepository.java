package com.industryos.repository;

import com.industryos.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByInvoiceId(Integer invoiceId);
    List<Payment> findByInvoiceIdIn(List<Integer> invoiceIds);
}

