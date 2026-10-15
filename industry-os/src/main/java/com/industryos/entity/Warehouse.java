package com.industryos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "plant_id", nullable = false)
    private Integer plantId;

    @Column(name = "warehouse_name", nullable = false, length = 60)
    private String warehouseName;

    @Column(name = "capacity_units", nullable = false)
    private Integer capacityUnits;

    @Column(name = "manager_id")
    private Integer managerId;

    public Warehouse() {}

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public Integer getCapacityUnits() { return capacityUnits; }
    public void setCapacityUnits(Integer capacityUnits) { this.capacityUnits = capacityUnits; }

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }
}

