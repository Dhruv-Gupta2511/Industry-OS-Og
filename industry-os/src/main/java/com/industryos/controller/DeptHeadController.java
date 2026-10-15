package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.DeptHeadAggregateResponse;
import com.industryos.service.DeptHeadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/depthead")
@PreAuthorize("hasAnyRole('DEPT_HEAD', 'CEO', 'GENERAL_MANAGER')")
public class DeptHeadController {

    private final DeptHeadService deptHeadService;

    public DeptHeadController(DeptHeadService deptHeadService) {
        this.deptHeadService = deptHeadService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DeptHeadAggregateResponse>> getDepartmentDashboard(
            Authentication authentication,
            @RequestParam(required = false) Integer departmentId) {
        DeptHeadAggregateResponse response = deptHeadService.getDepartmentAggregate(authentication.getName(), departmentId);
        return ResponseEntity.ok(ApiResponse.ok("Department-scoped aggregate data retrieved successfully", response));
    }

    @GetMapping("/aggregate")
    public ResponseEntity<ApiResponse<DeptHeadAggregateResponse>> getDepartmentAggregate(
            Authentication authentication,
            @RequestParam(required = false) Integer departmentId) {
        DeptHeadAggregateResponse response = deptHeadService.getDepartmentAggregate(authentication.getName(), departmentId);
        return ResponseEntity.ok(ApiResponse.ok("Department programmatic aggregate metrics retrieved", response));
    }
}

