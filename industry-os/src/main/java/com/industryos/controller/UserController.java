package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.dto.response.UserProfileResponse;
import com.industryos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteCurrentAccount(Authentication authentication) {
        userService.deleteUserAccount(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("User account deleted successfully from the database", null));
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyRole('CEO', 'GENERAL_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteUserAccount(@PathVariable String username) {
        userService.deleteUserAccount(username);
        return ResponseEntity.ok(ApiResponse.ok("User account '" + username + "' deleted successfully from the database", null));
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('CEO', 'GENERAL_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteUserAccountById(@PathVariable Long id) {
        userService.deleteUserAccount(id);
        return ResponseEntity.ok(ApiResponse.ok("User account ID '" + id + "' deleted successfully from the database", null));
    }
}
