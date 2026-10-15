package com.industryos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sales_order_item")
public class SalesOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_item_id")
    private Integer soItemId;

    @Column(name = "so_id", nullable = false)
    private Integer soId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", precision = 14, scale = 2)
    private BigDecimal lineTotal;

    public SalesOrderItem() {}

    public SalesOrderItem(Integer soId, Integer productId, Integer quantity, BigDecimal unitPrice) {
        this.soId = soId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        if (unitPrice != null && quantity != null) {
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public Integer getSoItemId() { return soItemId; }
    public void setSoItemId(Integer soItemId) { this.soItemId = soItemId; }

    public Integer getSoId() { return soId; }
    public void setSoId(Integer soId) { this.soId = soId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        if (this.unitPrice != null && quantity != null) {
            this.lineTotal = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        if (unitPrice != null && this.quantity != null) {
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    public BigDecimal getLineTotal() {
        if (lineTotal == null && unitPrice != null && quantity != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}

