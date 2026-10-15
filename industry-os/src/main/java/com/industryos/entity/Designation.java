package com.industryos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "designation")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "desig_id")
    private Integer desigId;

    @Column(nullable = false, unique = true, length = 60)
    private String title;

    @Column(nullable = false, length = 2)
    private String grade;

    @Column(name = "base_salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseSalary;

    public Designation() {}

    public Designation(Integer desigId, String title, String grade, BigDecimal baseSalary) {
        this.desigId = desigId;
        this.title = title;
        this.grade = grade;
        this.baseSalary = baseSalary;
    }

    public Integer getDesigId() { return desigId; }
    public void setDesigId(Integer desigId) { this.desigId = desigId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
}

