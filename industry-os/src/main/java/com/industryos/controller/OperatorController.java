package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.OperatorMachineInventoryResponse;
import com.industryos.service.OperatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operator")
@PreAuthorize("hasAnyRole('OPERATOR', 'CEO', 'GENERAL_MANAGER')")
public class OperatorController {

    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping("/machine/inventory")
    public ResponseEntity<ApiResponse<OperatorMachineInventoryResponse>> getAssignedMachineInventory(Authentication authentication) {
        OperatorMachineInventoryResponse response = operatorService.getOperatorMachineInventory(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Assigned machine health and live spare parts inventory retrieved", response));
    }
}

