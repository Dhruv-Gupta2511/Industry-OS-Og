package com.industryos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransporterDashboardResponse {

    private Integer contractorId;
    private String companyName;
    private String serviceType;
    private String phone;
    private ActiveContractDTO activeContractTerms;
    private List<VehicleDetailDTO> vehicles;
    private LogisticsFinancialsDTO logisticsFinancials;

    public TransporterDashboardResponse() {}

    public static class ActiveContractDTO {
        private Integer contractId;
        private String companyName;
        private String serviceType;
        private String plantName;
        private LocalDate contractStart;
        private LocalDate contractEnd;
        private BigDecimal contractValue;
        private long remainingDays;
        private String contractStatus;

        public ActiveContractDTO() {}

        public Integer getContractId() { return contractId; }
        public void setContractId(Integer contractId) { this.contractId = contractId; }

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public String getServiceType() { return serviceType; }
        public void setServiceType(String serviceType) { this.serviceType = serviceType; }

        public String getPlantName() { return plantName; }
        public void setPlantName(String plantName) { this.plantName = plantName; }

        public LocalDate getContractStart() { return contractStart; }
        public void setContractStart(LocalDate contractStart) { this.contractStart = contractStart; }

        public LocalDate getContractEnd() { return contractEnd; }
        public void setContractEnd(LocalDate contractEnd) { this.contractEnd = contractEnd; }

        public BigDecimal getContractValue() { return contractValue; }
        public void setContractValue(BigDecimal contractValue) { this.contractValue = contractValue; }

        public long getRemainingDays() { return remainingDays; }
        public void setRemainingDays(long remainingDays) { this.remainingDays = remainingDays; }

        public String getContractStatus() { return contractStatus; }
        public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    }

    public static class VehicleDetailDTO {
        private Integer vehicleId;
        private String regNo;
        private String vehicleType;
        private BigDecimal capacityTons;
        private String driverName;
        private String driverPhone;
        private String status; // Available, On Trip, Under Repair
        private int totalTrips;
        private BigDecimal totalCargoValueHandled;
        private List<TruckTripInvoiceDTO> mappedShipmentsAndInvoices;

        public VehicleDetailDTO() {}

        public Integer getVehicleId() { return vehicleId; }
        public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

        public String getRegNo() { return regNo; }
        public void setRegNo(String regNo) { this.regNo = regNo; }

        public String getVehicleType() { return vehicleType; }
        public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

        public BigDecimal getCapacityTons() { return capacityTons; }
        public void setCapacityTons(BigDecimal capacityTons) { this.capacityTons = capacityTons; }

        public String getDriverName() { return driverName; }
        public void setDriverName(String driverName) { this.driverName = driverName; }

        public String getDriverPhone() { return driverPhone; }
        public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public int getTotalTrips() { return totalTrips; }
        public void setTotalTrips(int totalTrips) { this.totalTrips = totalTrips; }

        public BigDecimal getTotalCargoValueHandled() { return totalCargoValueHandled; }
        public void setTotalCargoValueHandled(BigDecimal totalCargoValueHandled) { this.totalCargoValueHandled = totalCargoValueHandled; }

        public List<TruckTripInvoiceDTO> getMappedShipmentsAndInvoices() { return mappedShipmentsAndInvoices; }
        public void setMappedShipmentsAndInvoices(List<TruckTripInvoiceDTO> mappedShipmentsAndInvoices) { this.mappedShipmentsAndInvoices = mappedShipmentsAndInvoices; }
    }

    public static class TruckTripInvoiceDTO {
        private Integer shipmentId;
        private Integer salesOrderId;
        private LocalDate shipDate;
        private LocalDate deliveredOn;
        private String destination;
        private String shipmentStatus;
        private Integer invoiceId;
        private BigDecimal invoiceAmount;
        private BigDecimal taxAmount;
        private BigDecimal totalInvoiceAmount;
        private BigDecimal paymentReceived;
        private String invoiceStatus;

        public TruckTripInvoiceDTO() {}

        public Integer getShipmentId() { return shipmentId; }
        public void setShipmentId(Integer shipmentId) { this.shipmentId = shipmentId; }

        public Integer getSalesOrderId() { return salesOrderId; }
        public void setSalesOrderId(Integer salesOrderId) { this.salesOrderId = salesOrderId; }

        public LocalDate getShipDate() { return shipDate; }
        public void setShipDate(LocalDate shipDate) { this.shipDate = shipDate; }

        public LocalDate getDeliveredOn() { return deliveredOn; }
        public void setDeliveredOn(LocalDate deliveredOn) { this.deliveredOn = deliveredOn; }

        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }

        public String getShipmentStatus() { return shipmentStatus; }
        public void setShipmentStatus(String shipmentStatus) { this.shipmentStatus = shipmentStatus; }

        public Integer getInvoiceId() { return invoiceId; }
        public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }

        public BigDecimal getInvoiceAmount() { return invoiceAmount; }
        public void setInvoiceAmount(BigDecimal invoiceAmount) { this.invoiceAmount = invoiceAmount; }

        public BigDecimal getTaxAmount() { return taxAmount; }
        public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

        public BigDecimal getTotalInvoiceAmount() { return totalInvoiceAmount; }
        public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) { this.totalInvoiceAmount = totalInvoiceAmount; }

        public BigDecimal getPaymentReceived() { return paymentReceived; }
        public void setPaymentReceived(BigDecimal paymentReceived) { this.paymentReceived = paymentReceived; }

        public String getInvoiceStatus() { return invoiceStatus; }
        public void setInvoiceStatus(String invoiceStatus) { this.invoiceStatus = invoiceStatus; }
    }

    public static class LogisticsFinancialsDTO {
        private int totalFleetCount;
        private int activeTrips;
        private int availableTrucks;
        private int underRepairTrucks;
        private BigDecimal totalTransportInvoiced;
        private BigDecimal totalPaymentsCleared;

        public LogisticsFinancialsDTO() {}

        public int getTotalFleetCount() { return totalFleetCount; }
        public void setTotalFleetCount(int totalFleetCount) { this.totalFleetCount = totalFleetCount; }

        public int getActiveTrips() { return activeTrips; }
        public void setActiveTrips(int activeTrips) { this.activeTrips = activeTrips; }

        public int getAvailableTrucks() { return availableTrucks; }
        public void setAvailableTrucks(int availableTrucks) { this.availableTrucks = availableTrucks; }

        public int getUnderRepairTrucks() { return underRepairTrucks; }
        public void setUnderRepairTrucks(int underRepairTrucks) { this.underRepairTrucks = underRepairTrucks; }

        public BigDecimal getTotalTransportInvoiced() { return totalTransportInvoiced; }
        public void setTotalTransportInvoiced(BigDecimal totalTransportInvoiced) { this.totalTransportInvoiced = totalTransportInvoiced; }

        public BigDecimal getTotalPaymentsCleared() { return totalPaymentsCleared; }
        public void setTotalPaymentsCleared(BigDecimal totalPaymentsCleared) { this.totalPaymentsCleared = totalPaymentsCleared; }
    }

    public Integer getContractorId() { return contractorId; }
    public void setContractorId(Integer contractorId) { this.contractorId = contractorId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public ActiveContractDTO getActiveContractTerms() { return activeContractTerms; }
    public void setActiveContractTerms(ActiveContractDTO activeContractTerms) { this.activeContractTerms = activeContractTerms; }

    public List<VehicleDetailDTO> getVehicles() { return vehicles; }
    public void setVehicles(List<VehicleDetailDTO> vehicles) { this.vehicles = vehicles; }

    public LogisticsFinancialsDTO getLogisticsFinancials() { return logisticsFinancials; }
    public void setLogisticsFinancials(LogisticsFinancialsDTO logisticsFinancials) { this.logisticsFinancials = logisticsFinancials; }
}

