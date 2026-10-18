package com.industryos.controller;

import com.industryos.dto.request.PriceStockUpdateRequest;
import com.industryos.dto.response.ApiResponse;
import com.industryos.entity.AuditLog;
import com.industryos.entity.Machine;
import com.industryos.entity.Product;
import com.industryos.entity.SparePart;
import com.industryos.exception.ResourceNotFoundException;
import com.industryos.repository.AuditLogRepository;
import com.industryos.repository.MachineRepository;
import com.industryos.repository.ProductRepository;
import com.industryos.repository.SparePartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryModificationController {

    private final SparePartRepository sparePartRepository;
    private final ProductRepository productRepository;
    private final MachineRepository machineRepository;
    private final AuditLogRepository auditLogRepository;

    public InventoryModificationController(SparePartRepository sparePartRepository,
                                           ProductRepository productRepository,
                                           MachineRepository machineRepository,
                                           AuditLogRepository auditLogRepository) {
        this.sparePartRepository = sparePartRepository;
        this.productRepository = productRepository;
        this.machineRepository = machineRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/spare-parts")
    public ResponseEntity<ApiResponse<List<SparePart>>> getAllSpareParts() {
        return ResponseEntity.ok(ApiResponse.ok("Spare parts inventory retrieved", sparePartRepository.findAll()));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.ok("Products inventory catalog retrieved", productRepository.findAll()));
    }

    @PutMapping("/spare-parts/{id}")
    @PreAuthorize("hasAnyRole('DEPT_HEAD', 'CEO', 'GENERAL_MANAGER', 'OPERATOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateSparePart(
            @PathVariable Integer id,
            @RequestBody PriceStockUpdateRequest request,
            Authentication authentication) {
        SparePart part = sparePartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spare part not found with ID: " + id));

        String username = authentication != null ? authentication.getName() : "system";
        String prevVal = "Price: ₹" + part.getUnitCost() + ", Stock: " + part.getStockQty() + ", Reorder: " + part.getReorderLevel();

        if (request.getUnitPrice() != null) {
            part.setUnitCost(request.getUnitPrice());
        }
        if (request.getStockQty() != null) {
            part.setStockQty(request.getStockQty());
        }
        if (request.getReorderLevel() != null) {
            part.setReorderLevel(request.getReorderLevel());
        }

        SparePart saved = sparePartRepository.save(part);
        String newVal = "Price: ₹" + saved.getUnitCost() + ", Stock: " + saved.getStockQty() + ", Reorder: " + saved.getReorderLevel();

        // Calculate dependent totals
        BigDecimal totalValuation = saved.getUnitCost().multiply(BigDecimal.valueOf(saved.getStockQty()));

        // Audit Trail
        String auditId = "AUD-" + (System.currentTimeMillis() % 100000);
        AuditLog auditLog = new AuditLog(
                auditId,
                "UPDATE",
                "SPARE_PART",
                username,
                "Updated " + saved.getPartName() + " (ID: " + id + "): " + newVal,
                prevVal,
                newVal
        );
        auditLogRepository.save(auditLog);

        Map<String, Object> result = new HashMap<>();
        result.put("sparePart", saved);
        result.put("totalValuation", totalValuation);
        result.put("auditId", auditId);

        return ResponseEntity.ok(ApiResponse.ok("Spare part price and stock count updated successfully", result));
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('DEPT_HEAD', 'CEO', 'GENERAL_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateProduct(
            @PathVariable Integer id,
            @RequestBody PriceStockUpdateRequest request,
            Authentication authentication) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        String username = authentication != null ? authentication.getName() : "system";
        String prevVal = "Unit Price: ₹" + product.getUnitPrice() + ", Stock: " + product.getStockQty();

        if (request.getUnitPrice() != null) {
            product.setUnitPrice(request.getUnitPrice());
        }
        if (request.getStockQty() != null) {
            product.setStockQty(request.getStockQty());
        }

        Product saved = productRepository.save(product);
        String newVal = "Unit Price: ₹" + saved.getUnitPrice() + ", Stock: " + saved.getStockQty();

        // Recalculate dependent inventory valuation
        BigDecimal totalValuation = saved.getUnitPrice().multiply(BigDecimal.valueOf(saved.getStockQty()));
        BigDecimal estimatedGst18 = totalValuation.multiply(new BigDecimal("0.18"));

        // Audit Trail
        String auditId = "AUD-" + (System.currentTimeMillis() % 100000);
        AuditLog auditLog = new AuditLog(
                auditId,
                "UPDATE",
                "PRODUCT_PRICE",
                username,
                "Modified commercial pricing for SKU " + saved.getSku() + " (" + saved.getProductName() + ")",
                prevVal,
                newVal
        );
        auditLogRepository.save(auditLog);

        Map<String, Object> result = new HashMap<>();
        result.put("product", saved);
        result.put("totalValuation", totalValuation);
        result.put("estimatedGst18", estimatedGst18);
        result.put("auditId", auditId);

        return ResponseEntity.ok(ApiResponse.ok("Product price and inventory record updated successfully", result));
    }

    @PutMapping("/machines/{id}/status")
    @PreAuthorize("hasAnyRole('OPERATOR', 'DEPT_HEAD', 'CEO', 'GENERAL_MANAGER')")
    public ResponseEntity<ApiResponse<Machine>> updateMachineStatus(
            @PathVariable Integer id,
            @RequestBody PriceStockUpdateRequest request,
            Authentication authentication) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine not found with ID: " + id));

        String username = authentication != null ? authentication.getName() : "system";
        String prevVal = "Status: " + machine.getStatus();

        if (request.getStatus() != null) {
            machine.setStatus(request.getStatus());
        }

        Machine saved = machineRepository.save(machine);
        String newVal = "Status: " + saved.getStatus();

        String auditId = "AUD-" + (System.currentTimeMillis() % 100000);
        AuditLog auditLog = new AuditLog(
                auditId,
                "UPDATE",
                "MACHINE_STATUS",
                username,
                "Hardware state change for " + saved.getMachineName() + " (Line ID: " + saved.getLineId() + ")",
                prevVal,
                newVal
        );
        auditLogRepository.save(auditLog);

        return ResponseEntity.ok(ApiResponse.ok("Machine hardware operational status updated", saved));
    }
}

