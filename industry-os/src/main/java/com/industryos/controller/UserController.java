package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.UserProfileResponse;
import com.industryos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser(Authentication authentication) {
        UserProfileResponse profile = userService.getCurrentUserProfile(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("User profile retrieved successfully", profile));
    }
}

