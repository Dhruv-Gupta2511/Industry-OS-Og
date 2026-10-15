package com.industryos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_log")
public class MaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maint_id")
    private Integer maintId;

    @Column(name = "machine_id", nullable = false)
    private Integer machineId;

    @Column(name = "technician_id", nullable = false)
    private Integer technicianId;

    @Column(name = "maint_type", nullable = false, length = 20)
    private String maintType;

    @Column(name = "maint_date", nullable = false)
    private LocalDate maintDate;

    @Column(name = "downtime_hours", nullable = false, precision = 5, scale = 1)
    private BigDecimal downtimeHours = BigDecimal.ZERO;

    @Column(name = "labour_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal labourCost = BigDecimal.ZERO;

    @Column(length = 255)
    private String description;

    public MaintenanceLog() {}

    public Integer getMaintId() { return maintId; }
    public void setMaintId(Integer maintId) { this.maintId = maintId; }

    public Integer getMachineId() { return machineId; }
    public void setMachineId(Integer machineId) { this.machineId = machineId; }

    public Integer getTechnicianId() { return technicianId; }
    public void setTechnicianId(Integer technicianId) { this.technicianId = technicianId; }

    public String getMaintType() { return maintType; }
    public void setMaintType(String maintType) { this.maintType = maintType; }

    public LocalDate getMaintDate() { return maintDate; }
    public void setMaintDate(LocalDate maintDate) { this.maintDate = maintDate; }

    public BigDecimal getDowntimeHours() { return downtimeHours; }
    public void setDowntimeHours(BigDecimal downtimeHours) { this.downtimeHours = downtimeHours; }

    public BigDecimal getLabourCost() { return labourCost; }
    public void setLabourCost(BigDecimal labourCost) { this.labourCost = labourCost; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

