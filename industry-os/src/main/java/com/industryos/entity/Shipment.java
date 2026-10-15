package com.industryos.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Integer shipmentId;

    @Column(name = "so_id", nullable = false)
    private Integer soId;

    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;

    @Column(name = "ship_date", nullable = false)
    private LocalDate shipDate;

    @Column(name = "delivered_on")
    private LocalDate deliveredOn;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false, length = 20)
    private String status = "In Transit";

    public Shipment() {}

    public Integer getShipmentId() { return shipmentId; }
    public void setShipmentId(Integer shipmentId) { this.shipmentId = shipmentId; }

    public Integer getSoId() { return soId; }
    public void setSoId(Integer soId) { this.soId = soId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public LocalDate getShipDate() { return shipDate; }
    public void setShipDate(LocalDate shipDate) { this.shipDate = shipDate; }

    public LocalDate getDeliveredOn() { return deliveredOn; }
    public void setDeliveredOn(LocalDate deliveredOn) { this.deliveredOn = deliveredOn; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

