package com.industryos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "energy_consumption")
public class EnergyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "plant_id", nullable = false)
    private Integer plantId;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "units_kwh", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitsKwh;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    public EnergyConsumption() {}

    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public BigDecimal getUnitsKwh() { return unitsKwh; }
    public void setUnitsKwh(BigDecimal unitsKwh) { this.unitsKwh = unitsKwh; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
}

