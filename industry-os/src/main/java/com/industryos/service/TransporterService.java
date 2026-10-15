package com.industryos.service;

import com.industryos.dto.response.TransporterDashboardResponse;
import com.industryos.dto.response.TransporterDashboardResponse.*;
import com.industryos.entity.*;
import com.industryos.exception.ResourceNotFoundException;
import com.industryos.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TransporterService {

    private final UserAccountRepository userAccountRepository;
    private final ContractorRepository contractorRepository;
    private final PlantRepository plantRepository;
    private final VehicleRepository vehicleRepository;
    private final ShipmentRepository shipmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public TransporterService(UserAccountRepository userAccountRepository,
                              ContractorRepository contractorRepository,
                              PlantRepository plantRepository,
                              VehicleRepository vehicleRepository,
                              ShipmentRepository shipmentRepository,
                              InvoiceRepository invoiceRepository,
                              PaymentRepository paymentRepository) {
        this.userAccountRepository = userAccountRepository;
        this.contractorRepository = contractorRepository;
        this.plantRepository = plantRepository;
        this.vehicleRepository = vehicleRepository;
        this.shipmentRepository = shipmentRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    public TransporterDashboardResponse getTransporterDashboard(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Transporter user not found: " + username));

        Integer contractorId = user.getContractorId();
        if (contractorId == null) {
            contractorId = contractorRepository.findAll().stream().findFirst().map(Contractor::getContractorId).orElse(1);
        }

        final Integer finalContractorId = contractorId;
        Contractor contractor = contractorRepository.findById(finalContractorId)
                .orElse(new Contractor());

        TransporterDashboardResponse dashboard = new TransporterDashboardResponse();
        dashboard.setContractorId(contractor.getContractorId() != null ? contractor.getContractorId() : finalContractorId);
        dashboard.setCompanyName(contractor.getCompanyName() != null ? contractor.getCompanyName() : "SafeGuard Express Logistics & Transport");
        dashboard.setServiceType(contractor.getServiceType() != null ? contractor.getServiceType() : "Logistics & Fleet Transport");
        dashboard.setPhone(contractor.getPhone() != null ? contractor.getPhone() : "9822011111");

        // 1. Active Contract Terms
        ActiveContractDTO contractDTO = new ActiveContractDTO();
        contractDTO.setContractId(contractor.getContractorId() != null ? contractor.getContractorId() : 1);
        contractDTO.setCompanyName(dashboard.getCompanyName());
        contractDTO.setServiceType(dashboard.getServiceType());
        contractDTO.setContractStart(contractor.getContractStart() != null ? contractor.getContractStart() : LocalDate.of(2025, 4, 1));
        contractDTO.setContractEnd(contractor.getContractEnd() != null ? contractor.getContractEnd() : LocalDate.of(2027, 3, 31));
        contractDTO.setContractValue(contractor.getContractValue() != null ? contractor.getContractValue() : BigDecimal.valueOf(2400000));

        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), contractDTO.getContractEnd());
        contractDTO.setRemainingDays(Math.max(0, remainingDays));
        contractDTO.setContractStatus(remainingDays > 0 ? "ACTIVE" : "EXPIRED");

        if (contractor.getPlantId() != null) {
            plantRepository.findById(contractor.getPlantId()).ifPresent(plant -> {
                contractDTO.setPlantName(plant.getPlantName() + " (" + plant.getCity() + ")");
            });
        } else {
            contractDTO.setPlantName("Pune Works Central Hub");
        }
        dashboard.setActiveContractTerms(contractDTO);

        // 2. Vehicles / Trucks Provided & Mapped Trip Invoices
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<VehicleDetailDTO> vehicleDTOs = new ArrayList<>();

        int activeTripsCount = 0;
        int availableTrucksCount = 0;
        int underRepairCount = 0;
        BigDecimal totalTransportInvoiced = BigDecimal.ZERO;
        BigDecimal totalPaymentsCleared = BigDecimal.ZERO;

        for (Vehicle v : allVehicles) {
            VehicleDetailDTO vDTO = new VehicleDetailDTO();
            vDTO.setVehicleId(v.getVehicleId());
            vDTO.setRegNo(v.getRegNo());
            vDTO.setVehicleType(v.getVehicleType());
            vDTO.setCapacityTons(v.getCapacityTons());
            vDTO.setDriverName(v.getDriverName());
            vDTO.setDriverPhone(v.getDriverPhone());
            vDTO.setStatus(v.getStatus());

            if ("On Trip".equalsIgnoreCase(v.getStatus())) {
                activeTripsCount++;
            } else if ("Available".equalsIgnoreCase(v.getStatus())) {
                availableTrucksCount++;
            } else if ("Under Repair".equalsIgnoreCase(v.getStatus())) {
                underRepairCount++;
            }

            // Shipments mapped to this specific vehicle
            List<Shipment> vehicleShipments = shipmentRepository.findByVehicleId(v.getVehicleId());
            vDTO.setTotalTrips(vehicleShipments.size());

            BigDecimal vehicleCargoValue = BigDecimal.ZERO;
            List<TruckTripInvoiceDTO> tripInvoiceDTOs = new ArrayList<>();

            for (Shipment s : vehicleShipments) {
                TruckTripInvoiceDTO tripDTO = new TruckTripInvoiceDTO();
                tripDTO.setShipmentId(s.getShipmentId());
                tripDTO.setSalesOrderId(s.getSoId());
                tripDTO.setShipDate(s.getShipDate());
                tripDTO.setDeliveredOn(s.getDeliveredOn());
                tripDTO.setDestination(s.getDestination());
                tripDTO.setShipmentStatus(s.getStatus());

                // Mapped Invoice & Payments
                Optional<Invoice> invOpt = invoiceRepository.findBySoId(s.getSoId());
                if (invOpt.isPresent()) {
                    Invoice inv = invOpt.get();
                    tripDTO.setInvoiceId(inv.getInvoiceId());
                    tripDTO.setInvoiceAmount(inv.getAmount());
                    tripDTO.setTaxAmount(inv.getTaxAmount());
                    tripDTO.setTotalInvoiceAmount(inv.getTotalAmount());
                    tripDTO.setInvoiceStatus(inv.getStatus());

                    vehicleCargoValue = vehicleCargoValue.add(inv.getTotalAmount());
                    totalTransportInvoiced = totalTransportInvoiced.add(inv.getTotalAmount());

                    List<Payment> payments = paymentRepository.findByInvoiceId(inv.getInvoiceId());
                    BigDecimal paid = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    tripDTO.setPaymentReceived(paid);
                    totalPaymentsCleared = totalPaymentsCleared.add(paid);
                } else {
                    tripDTO.setInvoiceStatus("PENDING_BILLING");
                    tripDTO.setInvoiceAmount(BigDecimal.ZERO);
                    tripDTO.setTaxAmount(BigDecimal.ZERO);
                    tripDTO.setTotalInvoiceAmount(BigDecimal.ZERO);
                    tripDTO.setPaymentReceived(BigDecimal.ZERO);
                }

                tripInvoiceDTOs.add(tripDTO);
            }

            vDTO.setTotalCargoValueHandled(vehicleCargoValue);
            vDTO.setMappedShipmentsAndInvoices(tripInvoiceDTOs);
            vehicleDTOs.add(vDTO);
        }

        dashboard.setVehicles(vehicleDTOs);

        // 3. Logistics Financials
        LogisticsFinancialsDTO financials = new LogisticsFinancialsDTO();
        financials.setTotalFleetCount(allVehicles.size());
        financials.setActiveTrips(activeTripsCount);
        financials.setAvailableTrucks(availableTrucksCount);
        financials.setUnderRepairTrucks(underRepairCount);
        financials.setTotalTransportInvoiced(totalTransportInvoiced);
        financials.setTotalPaymentsCleared(totalPaymentsCleared);
        dashboard.setLogisticsFinancials(financials);

        return dashboard;
    }
}

