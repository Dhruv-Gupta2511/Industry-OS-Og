package com.industryos.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "po_id")
    private Integer poId;

    @Column(name = "supplier_id", nullable = false)
    private Integer supplierId;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expected_date", nullable = false)
    private LocalDate expectedDate;

    @Column(nullable = false, length = 20)
    private String status = "Placed";

    public PurchaseOrder() {}

    public Integer getPoId() { return poId; }
    public void setPoId(Integer poId) { this.poId = poId; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalDate getExpectedDate() { return expectedDate; }
    public void setExpectedDate(LocalDate expectedDate) { this.expectedDate = expectedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

