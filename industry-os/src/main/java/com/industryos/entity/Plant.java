package com.industryos.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "plant")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Integer plantId;

    @Column(name = "plant_name", nullable = false, unique = true, length = 80)
    private String plantName;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String state;

    @Column(name = "daily_capacity", nullable = false)
    private Integer dailyCapacity;

    @Column(nullable = false)
    private LocalDate established;

    public Plant() {}

    public Plant(Integer plantId, String plantName, String city, String state, Integer dailyCapacity, LocalDate established) {
        this.plantId = plantId;
        this.plantName = plantName;
        this.city = city;
        this.state = state;
        this.dailyCapacity = dailyCapacity;
        this.established = established;
    }

    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Integer getDailyCapacity() { return dailyCapacity; }
    public void setDailyCapacity(Integer dailyCapacity) { this.dailyCapacity = dailyCapacity; }

    public LocalDate getEstablished() { return established; }
    public void setEstablished(LocalDate established) { this.established = established; }
}

