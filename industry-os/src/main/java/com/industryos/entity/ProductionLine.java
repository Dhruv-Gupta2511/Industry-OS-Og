package com.industryos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "production_line")
public class ProductionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Integer lineId;

    @Column(name = "plant_id", nullable = false)
    private Integer plantId;

    @Column(name = "line_name", nullable = false, length = 60)
    private String lineName;

    @Column(name = "hourly_output", nullable = false)
    private Integer hourlyOutput;

    @Column(nullable = false, length = 20)
    private String status = "Running";

    public ProductionLine() {}

    public ProductionLine(Integer lineId, Integer plantId, String lineName, Integer hourlyOutput, String status) {
        this.lineId = lineId;
        this.plantId = plantId;
        this.lineName = lineName;
        this.hourlyOutput = hourlyOutput;
        this.status = status;
    }

    public Integer getLineId() { return lineId; }
    public void setLineId(Integer lineId) { this.lineId = lineId; }

    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }

    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }

    public Integer getHourlyOutput() { return hourlyOutput; }
    public void setHourlyOutput(Integer hourlyOutput) { this.hourlyOutput = hourlyOutput; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

