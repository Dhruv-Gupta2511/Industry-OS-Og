package com.industryos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "reg_no", nullable = false, unique = true, length = 15)
    private String regNo;

    @Column(name = "vehicle_type", nullable = false, length = 30)
    private String vehicleType;

    @Column(name = "capacity_tons", nullable = false, precision = 5, scale = 1)
    private BigDecimal capacityTons;

    @Column(name = "driver_name", nullable = false, length = 60)
    private String driverName;

    @Column(name = "driver_phone", length = 15)
    private String driverPhone;

    @Column(nullable = false, length = 20)
    private String status = "Available";

    public Vehicle() {}

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public BigDecimal getCapacityTons() { return capacityTons; }
    public void setCapacityTons(BigDecimal capacityTons) { this.capacityTons = capacityTons; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

