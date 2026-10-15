package com.industryos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pcat_id")
    private Integer pcatId;

    @Column(name = "pcat_name", nullable = false, unique = true, length = 50)
    private String pcatName;

    public ProductCategory() {}

    public ProductCategory(Integer pcatId, String pcatName) {
        this.pcatId = pcatId;
        this.pcatName = pcatName;
    }

    public Integer getPcatId() { return pcatId; }
    public void setPcatId(Integer pcatId) { this.pcatId = pcatId; }

    public String getPcatName() { return pcatName; }
    public void setPcatName(String pcatName) { this.pcatName = pcatName; }
}

