package com.industryos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MaintenancePartUsageId implements Serializable {

    @Column(name = "maint_id")
    private Integer maintId;

    @Column(name = "part_id")
    private Integer partId;

    public MaintenancePartUsageId() {}

    public MaintenancePartUsageId(Integer maintId, Integer partId) {
        this.maintId = maintId;
        this.partId = partId;
    }

    public Integer getMaintId() { return maintId; }
    public void setMaintId(Integer maintId) { this.maintId = maintId; }

    public Integer getPartId() { return partId; }
    public void setPartId(Integer partId) { this.partId = partId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenancePartUsageId that = (MaintenancePartUsageId) o;
        return Objects.equals(maintId, that.maintId) && Objects.equals(partId, that.partId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maintId, partId);
    }
}

