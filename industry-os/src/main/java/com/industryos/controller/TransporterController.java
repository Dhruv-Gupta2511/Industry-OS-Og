package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.TransporterDashboardResponse;
import com.industryos.service.TransporterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transporter")
@PreAuthorize("hasAnyRole('TRANSPORTER', 'CEO', 'GENERAL_MANAGER')")
public class TransporterController {

    private final TransporterService transporterService;

    public TransporterController(TransporterService transporterService) {
        this.transporterService = transporterService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<TransporterDashboardResponse>> getTransporterDashboard(Authentication authentication) {
        TransporterDashboardResponse dashboard = transporterService.getTransporterDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Transporter dashboard with vehicle fleet and mapped trip invoices retrieved", dashboard));
    }
}

