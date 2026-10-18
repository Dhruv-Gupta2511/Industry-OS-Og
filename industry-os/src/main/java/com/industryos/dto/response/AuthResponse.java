package com.industryos.dto.response;

import com.industryos.entity.Role;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String fullName;
    private Role role;
    private String department;
    private String mappedTable;

    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String username, String fullName, Role role, String department) {
        this.token = token;
        this.tokenType = "Bearer";
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.department = department;
    }

    public AuthResponse(String token, Long userId, String username, String fullName, Role role, String department, String mappedTable) {
        this(token, userId, username, fullName, role, department);
        this.mappedTable = mappedTable;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getMappedTable() { return mappedTable; }
    public void setMappedTable(String mappedTable) { this.mappedTable = mappedTable; }
}

