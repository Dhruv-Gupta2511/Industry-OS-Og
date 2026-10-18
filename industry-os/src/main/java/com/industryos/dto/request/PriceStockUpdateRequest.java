package com.industryos.dto.request;

import java.math.BigDecimal;

public class PriceStockUpdateRequest {
    private BigDecimal unitPrice;
    private Integer stockQty;
    private Integer reorderLevel;
    private String status;

    public PriceStockUpdateRequest() {}

    public PriceStockUpdateRequest(BigDecimal unitPrice, Integer stockQty, Integer reorderLevel, String status) {
        this.unitPrice = unitPrice;
        this.stockQty = stockQty;
        this.reorderLevel = reorderLevel;
        this.status = status;
    }



    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }

    public Integer getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(Integer reorderLevel) { this.reorderLevel = reorderLevel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

