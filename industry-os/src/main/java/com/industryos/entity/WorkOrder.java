package com.industryos.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "work_order")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wo_id")
    private Integer woId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "line_id", nullable = false)
    private Integer lineId;

    @Column(name = "supervisor_id", nullable = false)
    private Integer supervisorId;

    @Column(name = "planned_qty", nullable = false)
    private Integer plannedQty;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, length = 20)
    private String status = "Planned";

    public WorkOrder() {}

    public Integer getWoId() { return woId; }
    public void setWoId(Integer woId) { this.woId = woId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getLineId() { return lineId; }
    public void setLineId(Integer lineId) { this.lineId = lineId; }

    public Integer getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Integer supervisorId) { this.supervisorId = supervisorId; }

    public Integer getPlannedQty() { return plannedQty; }
    public void setPlannedQty(Integer plannedQty) { this.plannedQty = plannedQty; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

