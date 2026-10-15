package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.ExecutiveDashboardResponse;
import com.industryos.service.ExecutiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/executive")
@PreAuthorize("hasAnyRole('CEO', 'GENERAL_MANAGER')")
public class ExecutiveController {

    private final ExecutiveService executiveService;

    public ExecutiveController(ExecutiveService executiveService) {
        this.executiveService = executiveService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<ExecutiveDashboardResponse>> getExecutiveDashboard(Authentication authentication) {
        ExecutiveDashboardResponse dashboard = executiveService.getExecutiveDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Global enterprise executive dashboard retrieved", dashboard));
    }

    @GetMapping("/financials")
    public ResponseEntity<ApiResponse<ExecutiveDashboardResponse.GlobalFinancialsDTO>> getGlobalFinancials(Authentication authentication) {
        ExecutiveDashboardResponse dashboard = executiveService.getExecutiveDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Global enterprise financials retrieved", dashboard.getGlobalFinancials()));
    }

    @GetMapping("/plants")
    public ResponseEntity<ApiResponse<?>> getPlantsOverview(Authentication authentication) {
        ExecutiveDashboardResponse dashboard = executiveService.getExecutiveDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Enterprise plants overview retrieved", dashboard.getPlants()));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<?>> getDepartmentsBreakdown(Authentication authentication) {
        ExecutiveDashboardResponse dashboard = executiveService.getExecutiveDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Enterprise departments breakdown retrieved", dashboard.getDepartments()));
    }
}

