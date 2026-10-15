package com.industryos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "maintenance_part_usage")
public class MaintenancePartUsage {

    @EmbeddedId
    private MaintenancePartUsageId id;

    @Column(name = "qty_used", nullable = false)
    private Integer qtyUsed;

    public MaintenancePartUsage() {}

    public MaintenancePartUsage(Integer maintId, Integer partId, Integer qtyUsed) {
        this.id = new MaintenancePartUsageId(maintId, partId);
        this.qtyUsed = qtyUsed;
    }

    public MaintenancePartUsageId getId() { return id; }
    public void setId(MaintenancePartUsageId id) { this.id = id; }

    public Integer getQtyUsed() { return qtyUsed; }
    public void setQtyUsed(Integer qtyUsed) { this.qtyUsed = qtyUsed; }
}

