package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.CustomerDashboardResponse;
import com.industryos.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasAnyRole('CUSTOMER', 'CEO', 'GENERAL_MANAGER', 'DEPT_HEAD')")
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

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Integer id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        customerService.deleteOrder(id, username);
        return ResponseEntity.ok(ApiResponse.ok("Sales order #" + id + " and related records deleted successfully from the database", null));
    }
}
