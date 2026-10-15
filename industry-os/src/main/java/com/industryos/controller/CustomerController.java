package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.CustomerDashboardResponse;
import com.industryos.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasAnyRole('CUSTOMER', 'CEO', 'GENERAL_MANAGER')")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<CustomerDashboardResponse>> getCustomerDashboard(Authentication authentication) {
        CustomerDashboardResponse dashboard = customerService.getCustomerDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Customer dashboard with active cart and dynamic invoice retrieved", dashboard));
    }
}

