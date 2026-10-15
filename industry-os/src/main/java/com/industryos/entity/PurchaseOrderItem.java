package com.industryos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_order_item")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "po_item_id")
    private Integer poItemId;

    @Column(name = "po_id", nullable = false)
    private Integer poId;

    @Column(name = "material_id", nullable = false)
    private Integer materialId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", precision = 14, scale = 2)
    private BigDecimal lineTotal;

    public PurchaseOrderItem() {}

    public PurchaseOrderItem(Integer poId, Integer materialId, BigDecimal quantity, BigDecimal unitPrice) {
        this.poId = poId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        if (unitPrice != null && quantity != null) {
            this.lineTotal = unitPrice.multiply(quantity);
        }
    }

    public Integer getPoItemId() { return poItemId; }
    public void setPoItemId(Integer poItemId) { this.poItemId = poItemId; }

    public Integer getPoId() { return poId; }
    public void setPoId(Integer poId) { this.poId = poId; }

    public Integer getMaterialId() { return materialId; }
    public void setMaterialId(Integer materialId) { this.materialId = materialId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        if (this.unitPrice != null && quantity != null) {
            this.lineTotal = this.unitPrice.multiply(quantity);
        }
    }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        if (unitPrice != null && this.quantity != null) {
            this.lineTotal = unitPrice.multiply(this.quantity);
        }
    }

    public BigDecimal getLineTotal() {
        if (lineTotal == null && unitPrice != null && quantity != null) {
            return unitPrice.multiply(quantity);
        }
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}

