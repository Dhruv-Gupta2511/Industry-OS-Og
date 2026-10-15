package com.industryos.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "production_batch")
public class ProductionBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Integer batchId;

    @Column(name = "wo_id", nullable = false)
    private Integer woId;

    @Column(name = "shift_id", nullable = false)
    private Integer shiftId;

    @Column(name = "batch_no", nullable = false, unique = true, length = 20)
    private String batchNo;

    @Column(name = "batch_date", nullable = false)
    private LocalDate batchDate;

    @Column(name = "produced_qty", nullable = false)
    private Integer producedQty = 0;

    @Column(name = "rejected_qty", nullable = false)
    private Integer rejectedQty = 0;

    public ProductionBatch() {}

    public Integer getBatchId() { return batchId; }
    public void setBatchId(Integer batchId) { this.batchId = batchId; }

    public Integer getWoId() { return woId; }
    public void setWoId(Integer woId) { this.woId = woId; }

    public Integer getShiftId() { return shiftId; }
    public void setShiftId(Integer shiftId) { this.shiftId = shiftId; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public LocalDate getBatchDate() { return batchDate; }
    public void setBatchDate(LocalDate batchDate) { this.batchDate = batchDate; }

    public Integer getProducedQty() { return producedQty; }
    public void setProducedQty(Integer producedQty) { this.producedQty = producedQty; }

    public Integer getRejectedQty() { return rejectedQty; }
    public void setRejectedQty(Integer rejectedQty) { this.rejectedQty = rejectedQty; }

    public Integer getGoodQty() {
        return Math.max(0, (producedQty != null ? producedQty : 0) - (rejectedQty != null ? rejectedQty : 0));
    }
}

