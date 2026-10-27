package com.industryos.service;

import com.industryos.dto.response.CustomerDashboardResponse;
import com.industryos.dto.response.CustomerDashboardResponse.*;
import com.industryos.entity.*;
import com.industryos.exception.ResourceNotFoundException;
import com.industryos.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class CustomerService {

    private final UserAccountRepository userAccountRepository;
    private final CustomerRepository customerRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final ShipmentRepository shipmentRepository;
    private final AuditLogRepository auditLogRepository;

    public CustomerService(UserAccountRepository userAccountRepository,
                           CustomerRepository customerRepository,
                           SalesOrderRepository salesOrderRepository,
                           SalesOrderItemRepository salesOrderItemRepository,
                           ProductRepository productRepository,
                           InvoiceRepository invoiceRepository,
                           PaymentRepository paymentRepository,
                           ShipmentRepository shipmentRepository,
                           AuditLogRepository auditLogRepository) {
        this.userAccountRepository = userAccountRepository;
        this.customerRepository = customerRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.shipmentRepository = shipmentRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public CustomerDashboardResponse getCustomerDashboard(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer user not found: " + username));

        Integer customerId = user.getCustomerId();
        if (customerId == null) {
            customerId = customerRepository.findAll().stream().findFirst().map(Customer::getCustomerId).orElse(1);
        }

        final Integer finalCustomerId = customerId;
        Customer customer = customerRepository.findById(finalCustomerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + finalCustomerId));

        CustomerDashboardResponse dashboard = new CustomerDashboardResponse();
        dashboard.setCustomerId(customer.getCustomerId());
        dashboard.setCustomerName(customer.getCustomerName());
        dashboard.setContactPerson(customer.getContactPerson());
        dashboard.setEmail(customer.getEmail());
        dashboard.setPhone(customer.getPhone());
        dashboard.setCity(customer.getCity());
        dashboard.setState(customer.getState());
        dashboard.setGstin(customer.getGstin());
        dashboard.setCreditLimit(customer.getCreditLimit());

        // Fetch all orders for this customer
        List<SalesOrder> orders = salesOrderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);

        BigDecimal totalSalesHistory = BigDecimal.ZERO;
        BigDecimal totalOutstanding = BigDecimal.ZERO;

        List<OrderSummaryDTO> previousOrders = new ArrayList<>();
        SalesOrder activeCartOrder = null;
        SalesOrder latestOrder = null;

        for (SalesOrder order : orders) {
            if (!"Cancelled".equalsIgnoreCase(order.getStatus())) {
                totalSalesHistory = totalSalesHistory.add(order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO);
            }

            // Identify active shopping cart (first order with status 'Placed')
            if ("Placed".equalsIgnoreCase(order.getStatus()) && activeCartOrder == null) {
                activeCartOrder = order;
            }

            if (latestOrder == null) {
                latestOrder = order;
            }

            OrderSummaryDTO orderDTO = new OrderSummaryDTO();
            orderDTO.setOrderId(order.getSoId());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setDeliveryDate(order.getDeliveryDate());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setTotalAmount(order.getTotalAmount());

            // Items count
            List<SalesOrderItem> items = salesOrderItemRepository.findBySoId(order.getSoId());
            orderDTO.setItemCount(items.size());

            // Shipment status
            shipmentRepository.findBySoId(order.getSoId())
                    .ifPresent(shipment -> orderDTO.setShipmentStatus(shipment.getStatus()));

            // Invoice & payment status
            invoiceRepository.findBySoId(order.getSoId()).ifPresent(invoice -> {
                orderDTO.setPaymentStatus(invoice.getStatus());
            });

            previousOrders.add(orderDTO);
        }

        dashboard.setPreviousOrders(previousOrders);
        dashboard.setTotalSalesHistory(totalSalesHistory);

        // 1. Build Active Shopping Cart
        ActiveCartDTO cartDTO = new ActiveCartDTO();
        if (activeCartOrder != null) {
            cartDTO.setActiveOrderId(activeCartOrder.getSoId());
            cartDTO.setOrderStatus(activeCartOrder.getStatus());
            cartDTO.setOrderDate(activeCartOrder.getOrderDate());
            cartDTO.setExpectedDeliveryDate(activeCartOrder.getDeliveryDate());

            List<SalesOrderItem> cartItems = salesOrderItemRepository.findBySoId(activeCartOrder.getSoId());
            List<CartItemDTO> itemDTOs = mapCartItems(cartItems);
            cartDTO.setItems(itemDTOs);
            cartDTO.setTotalItemsCount(itemDTOs.size());

            BigDecimal subtotal = activeCartOrder.getTotalAmount() != null ? activeCartOrder.getTotalAmount() : BigDecimal.ZERO;
            cartDTO.setSubtotal(subtotal);
            BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
            cartDTO.setEstimatedTax(tax);
            cartDTO.setTotalAmount(subtotal.add(tax));
        } else {
            cartDTO.setOrderStatus("Cart Ready (No active pending order)");
            cartDTO.setTotalItemsCount(0);
            cartDTO.setSubtotal(BigDecimal.ZERO);
            cartDTO.setEstimatedTax(BigDecimal.ZERO);
            cartDTO.setTotalAmount(BigDecimal.ZERO);
            cartDTO.setItems(Collections.emptyList());
        }
        dashboard.setActiveCart(cartDTO);

        // 2. Build Dynamically Generated Invoice for Current/Latest Order
        if (latestOrder != null) {
            final SalesOrder orderForInvoice = latestOrder;
            DynamicInvoiceDTO invoiceDTO = new DynamicInvoiceDTO();
            invoiceDTO.setSalesOrderId(orderForInvoice.getSoId());
            invoiceDTO.setInvoiceReference("INV-2026-" + String.format("%04d", orderForInvoice.getSoId()));

            List<SalesOrderItem> orderItems = salesOrderItemRepository.findBySoId(orderForInvoice.getSoId());
            List<CartItemDTO> invoiceLineItems = mapCartItems(orderItems);
            invoiceDTO.setLineItems(invoiceLineItems);

            BigDecimal subtotal = orderForInvoice.getTotalAmount() != null ? orderForInvoice.getTotalAmount() : BigDecimal.ZERO;
            invoiceDTO.setSubtotal(subtotal);

            Optional<Invoice> existingInvoiceOpt = invoiceRepository.findBySoId(orderForInvoice.getSoId());
            if (existingInvoiceOpt.isPresent()) {
                Invoice inv = existingInvoiceOpt.get();
                invoiceDTO.setInvoiceId(inv.getInvoiceId());
                invoiceDTO.setInvoiceDate(inv.getInvoiceDate());
                invoiceDTO.setDueDate(inv.getDueDate());
                invoiceDTO.setTaxAmount(inv.getTaxAmount());
                invoiceDTO.setTotalPayable(inv.getTotalAmount());

                List<Payment> payments = paymentRepository.findByInvoiceId(inv.getInvoiceId());
                BigDecimal paid = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                invoiceDTO.setAmountPaid(paid);
                BigDecimal balance = inv.getTotalAmount().subtract(paid);
                invoiceDTO.setOutstandingBalance(balance.compareTo(BigDecimal.ZERO) > 0 ? balance : BigDecimal.ZERO);
                invoiceDTO.setPaymentStatus(inv.getStatus());
            } else {
                // Dynamically generated invoice preview for placed/current order
                invoiceDTO.setInvoiceDate(orderForInvoice.getOrderDate());
                invoiceDTO.setDueDate(orderForInvoice.getOrderDate().plusDays(30));
                BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
                invoiceDTO.setTaxAmount(tax);
                invoiceDTO.setTotalPayable(subtotal.add(tax));
                invoiceDTO.setAmountPaid(BigDecimal.ZERO);
                invoiceDTO.setOutstandingBalance(subtotal.add(tax));
                invoiceDTO.setPaymentStatus("Unpaid");
            }
            dashboard.setLatestOrderInvoice(invoiceDTO);
        }

        // Calculate total outstanding balance across all invoices for this customer
        List<Integer> soIds = orders.stream().map(SalesOrder::getSoId).toList();
        if (!soIds.isEmpty()) {
            List<Invoice> customerInvoices = invoiceRepository.findBySoIdIn(soIds);
            for (Invoice inv : customerInvoices) {
                if (!"Paid".equalsIgnoreCase(inv.getStatus())) {
                    List<Payment> pmts = paymentRepository.findByInvoiceId(inv.getInvoiceId());
                    BigDecimal paid = pmts.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal bal = inv.getTotalAmount().subtract(paid);
                    if (bal.compareTo(BigDecimal.ZERO) > 0) {
                        totalOutstanding = totalOutstanding.add(bal);
                    }
                }
            }
        }
        dashboard.setTotalOutstandingBalance(totalOutstanding);

        return dashboard;
    }

    @Transactional
    public void deleteOrder(Integer orderId, String username) {
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with ID: " + orderId));

        BigDecimal orderAmt = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
        String prevVal = "Order ID: " + orderId + ", Customer ID: " + order.getCustomerId() + ", Amount: ₹" + orderAmt + ", Status: " + order.getStatus();

        // 1. Delete associated payments & invoice
        invoiceRepository.findBySoId(orderId).ifPresent(inv -> {
            List<Payment> payments = paymentRepository.findByInvoiceId(inv.getInvoiceId());
            paymentRepository.deleteAll(payments);
            invoiceRepository.delete(inv);
        });

        // 2. Delete associated shipment
        shipmentRepository.findBySoId(orderId).ifPresent(shipmentRepository::delete);

        // 3. Delete order line items
        List<SalesOrderItem> items = salesOrderItemRepository.findBySoId(orderId);
        salesOrderItemRepository.deleteAll(items);

        // 4. Delete the sales order record
        salesOrderRepository.delete(order);

        // 5. Record Audit Log
        String auditId = "AUD-" + (System.currentTimeMillis() % 100000);
        AuditLog auditLog = new AuditLog(
                auditId,
                "DELETE",
                "SALES_ORDER",
                username != null ? username : "system",
                "Permanently deleted Sales Order #" + orderId + " (Value: ₹" + orderAmt + ", Status: " + order.getStatus() + ")",
                prevVal,
                "ORDER_DELETED"
        );
        auditLogRepository.save(auditLog);
    }

    private List<CartItemDTO> mapCartItems(List<SalesOrderItem> items) {
        List<CartItemDTO> itemDTOs = new ArrayList<>();
        for (SalesOrderItem item : items) {
            CartItemDTO dto = new CartItemDTO();
            dto.setItemId(item.getSoItemId());
            dto.setProductId(item.getProductId());
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setLineTotal(item.getLineTotal());

            productRepository.findById(item.getProductId()).ifPresent(prod -> {
                dto.setProductName(prod.getProductName());
                dto.setSku(prod.getSku());
            });

            itemDTOs.add(dto);
        }
        return itemDTOs;
    }
}
