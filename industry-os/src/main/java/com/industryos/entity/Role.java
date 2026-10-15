package com.industryos.entity;

public enum Role {
    CEO,
    GENERAL_MANAGER,
    DEPT_HEAD,
    OPERATOR,
    EMPLOYEE,
    CUSTOMER,
    TRANSPORTER;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}

