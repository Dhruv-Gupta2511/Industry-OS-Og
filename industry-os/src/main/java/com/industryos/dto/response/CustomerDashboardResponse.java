package com.industryos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CustomerDashboardResponse {

    private Integer customerId;
    private String customerName;
    private String contactPerson;
    private String email;
    private String phone;
    private String city;
    private String state;
    private String gstin;
    private BigDecimal creditLimit;
    private BigDecimal totalSalesHistory;
    private BigDecimal totalOutstandingBalance;

    private ActiveCartDTO activeCart;
    private List<OrderSummaryDTO> previousOrders;
    private DynamicInvoiceDTO latestOrderInvoice;

    public CustomerDashboardResponse() {}

    public static class ActiveCartDTO {
        private Integer activeOrderId;
        private String orderStatus;
        private LocalDate orderDate;
        private LocalDate expectedDeliveryDate;
        private int totalItemsCount;
        private BigDecimal subtotal;
        private BigDecimal estimatedTax;
        private BigDecimal totalAmount;
        private List<CartItemDTO> items;

        public ActiveCartDTO() {}

        public Integer getActiveOrderId() { return activeOrderId; }
        public void setActiveOrderId(Integer activeOrderId) { this.activeOrderId = activeOrderId; }

        public String getOrderStatus() { return orderStatus; }
        public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

        public LocalDate getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

        public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
        public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }

        public int getTotalItemsCount() { return totalItemsCount; }
        public void setTotalItemsCount(int totalItemsCount) { this.totalItemsCount = totalItemsCount; }

        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

        public BigDecimal getEstimatedTax() { return estimatedTax; }
        public void setEstimatedTax(BigDecimal estimatedTax) { this.estimatedTax = estimatedTax; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

        public List<CartItemDTO> getItems() { return items; }
        public void setItems(List<CartItemDTO> items) { this.items = items; }
    }

    public static class CartItemDTO {
        private Integer itemId;
        private Integer productId;
        private String productName;
        private String sku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;

        public CartItemDTO() {}

        public Integer getItemId() { return itemId; }
        public void setItemId(Integer itemId) { this.itemId = itemId; }

        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getLineTotal() { return lineTotal; }
        public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
    }

    public static class OrderSummaryDTO {
        private Integer orderId;
        private LocalDate orderDate;
        private LocalDate deliveryDate;
        private String status;
        private BigDecimal totalAmount;
        private int itemCount;
        private String shipmentStatus;
        private String paymentStatus;

        public OrderSummaryDTO() {}

        public Integer getOrderId() { return orderId; }
        public void setOrderId(Integer orderId) { this.orderId = orderId; }

        public LocalDate getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

        public LocalDate getDeliveryDate() { return deliveryDate; }
        public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

        public int getItemCount() { return itemCount; }
        public void setItemCount(int itemCount) { this.itemCount = itemCount; }

        public String getShipmentStatus() { return shipmentStatus; }
        public void setShipmentStatus(String shipmentStatus) { this.shipmentStatus = shipmentStatus; }

        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    }

    public static class DynamicInvoiceDTO {
        private Integer invoiceId;
        private Integer salesOrderId;
        private String invoiceReference;
        private LocalDate invoiceDate;
        private LocalDate dueDate;
        private BigDecimal subtotal;
        private BigDecimal taxRatePercent = BigDecimal.valueOf(18.0);
        private BigDecimal taxAmount;
        private BigDecimal totalPayable;
        private BigDecimal amountPaid;
        private BigDecimal outstandingBalance;
        private String paymentStatus;
        private List<CartItemDTO> lineItems;

        public DynamicInvoiceDTO() {}

        public Integer getInvoiceId() { return invoiceId; }
        public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }

        public Integer getSalesOrderId() { return salesOrderId; }
        public void setSalesOrderId(Integer salesOrderId) { this.salesOrderId = salesOrderId; }

        public String getInvoiceReference() { return invoiceReference; }
        public void setInvoiceReference(String invoiceReference) { this.invoiceReference = invoiceReference; }

        public LocalDate getInvoiceDate() { return invoiceDate; }
        public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

        public BigDecimal getTaxRatePercent() { return taxRatePercent; }
        public void setTaxRatePercent(BigDecimal taxRatePercent) { this.taxRatePercent = taxRatePercent; }

        public BigDecimal getTaxAmount() { return taxAmount; }
        public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

        public BigDecimal getTotalPayable() { return totalPayable; }
        public void setTotalPayable(BigDecimal totalPayable) { this.totalPayable = totalPayable; }

        public BigDecimal getAmountPaid() { return amountPaid; }
        public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

        public BigDecimal getOutstandingBalance() { return outstandingBalance; }
        public void setOutstandingBalance(BigDecimal outstandingBalance) { this.outstandingBalance = outstandingBalance; }

        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

        public List<CartItemDTO> getLineItems() { return lineItems; }
        public void setLineItems(List<CartItemDTO> lineItems) { this.lineItems = lineItems; }
    }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getGstin() { return gstin; }
    public void setGstin(String gstin) { this.gstin = gstin; }

    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }

    public BigDecimal getTotalSalesHistory() { return totalSalesHistory; }
    public void setTotalSalesHistory(BigDecimal totalSalesHistory) { this.totalSalesHistory = totalSalesHistory; }

    public BigDecimal getTotalOutstandingBalance() { return totalOutstandingBalance; }
    public void setTotalOutstandingBalance(BigDecimal totalOutstandingBalance) { this.totalOutstandingBalance = totalOutstandingBalance; }

    public ActiveCartDTO getActiveCart() { return activeCart; }
    public void setActiveCart(ActiveCartDTO activeCart) { this.activeCart = activeCart; }

    public List<OrderSummaryDTO> getPreviousOrders() { return previousOrders; }
    public void setPreviousOrders(List<OrderSummaryDTO> previousOrders) { this.previousOrders = previousOrders; }

    public DynamicInvoiceDTO getLatestOrderInvoice() { return latestOrderInvoice; }
    public void setLatestOrderInvoice(DynamicInvoiceDTO latestOrderInvoice) { this.latestOrderInvoice = latestOrderInvoice; }
}

