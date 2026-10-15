package com.industryos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industryos.dto.request.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IndustryOsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String obtainAccessToken(String username, String password, String userType) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password, userType);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("data").get("token").asText();
    }

    @Test
    @DisplayName("Context Loads Successfully")
    void contextLoads() {
    }

    @Test
    @DisplayName("1. Authentication - Login as Operator, Customer, Transporter, DeptHead, CEO")
    void testAuthenticationAllRoles() throws Exception {
        obtainAccessToken("operator1", "password123", "OPERATOR");
        obtainAccessToken("customer1", "password123", "CUSTOMER");
        obtainAccessToken("transporter1", "password123", "TRANSPORTER");
        obtainAccessToken("depthead_prod", "password123", "DEPT_HEAD");
        obtainAccessToken("ceo", "password123", "CEO");
    }

    @Test
    @DisplayName("2. GET /api/users/me - Returns full details, role, department & machine")
    void testGetCurrentUser() throws Exception {
        String token = obtainAccessToken("operator1", "password123", "OPERATOR");

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("operator1"))
                .andExpect(jsonPath("$.data.role").value("OPERATOR"))
                .andExpect(jsonPath("$.data.departmentName").value("Production"))
                .andExpect(jsonPath("$.data.assignedMachineId").value(1));
    }

    @Test
    @DisplayName("3. OPERATOR - GET /api/operator/machine/inventory returns assigned machine, health and spare parts")
    void testOperatorMachineInventory() throws Exception {
        String token = obtainAccessToken("operator1", "password123", "OPERATOR");

        mockMvc.perform(get("/api/operator/machine/inventory")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.machine.machineName").value("CNC Lathe"))
                .andExpect(jsonPath("$.data.health.healthScore").isNumber())
                .andExpect(jsonPath("$.data.health.healthStatus").isNotEmpty())
                .andExpect(jsonPath("$.data.sparePartsInventory", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.sparePartsInventory[0].partName").isNotEmpty())
                .andExpect(jsonPath("$.data.sparePartsInventory[0].stockQty").isNumber());
    }

    @Test
    @DisplayName("4. CUSTOMER - GET /api/customer/dashboard returns active cart, previous orders, and dynamic invoice")
    void testCustomerDashboard() throws Exception {
        String token = obtainAccessToken("customer1", "password123", "CUSTOMER");

        mockMvc.perform(get("/api/customer/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.customerName").value("Acme Engineering Solutions Ltd"))
                .andExpect(jsonPath("$.data.activeCart").exists())
                .andExpect(jsonPath("$.data.previousOrders", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data.latestOrderInvoice").exists())
                .andExpect(jsonPath("$.data.latestOrderInvoice.subtotal").isNumber())
                .andExpect(jsonPath("$.data.latestOrderInvoice.taxAmount").isNumber())
                .andExpect(jsonPath("$.data.latestOrderInvoice.totalPayable").isNumber());
    }

    @Test
    @DisplayName("5. TRANSPORTER - GET /api/transporter/dashboard returns fleet, active contract, and mapped trip invoices")
    void testTransporterDashboard() throws Exception {
        String token = obtainAccessToken("transporter1", "password123", "TRANSPORTER");

        mockMvc.perform(get("/api/transporter/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyName").value("SafeGuard Express Logistics & Transport"))
                .andExpect(jsonPath("$.data.activeContractTerms.contractStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.data.vehicles", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.vehicles[0].regNo").isNotEmpty())
                .andExpect(jsonPath("$.data.logisticsFinancials.totalFleetCount").isNumber());
    }

    @Test
    @DisplayName("6. DEPT_HEAD - Programmatic access to aggregate data for specific department only")
    void testDeptHeadAggregate() throws Exception {
        String token = obtainAccessToken("depthead_prod", "password123", "DEPT_HEAD");

        mockMvc.perform(get("/api/depthead/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.departmentId").value(1))
                .andExpect(jsonPath("$.data.departmentName").value("Production"))
                .andExpect(jsonPath("$.data.staffOverview.totalEmployees").isNumber())
                .andExpect(jsonPath("$.data.employees", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.machines", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.workOrders", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("7. DEPT_HEAD - Rejection when attempting to access another department's data")
    void testDeptHeadCrossDepartmentUnauthorized() throws Exception {
        String token = obtainAccessToken("depthead_prod", "password123", "DEPT_HEAD");

        // depthead_prod is department 1, attempting to request department 2 should fail
        mockMvc.perform(get("/api/depthead/dashboard?departmentId=2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("only authorized to view data for your own department")));
    }

    @Test
    @DisplayName("8. CEO & GENERAL_MANAGER - Global unrestricted access to executive metrics & financials")
    void testExecutiveGlobalAccess() throws Exception {
        String token = obtainAccessToken("ceo", "password123", "CEO");

        mockMvc.perform(get("/api/executive/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.executiveName").value("Suresh Pillai"))
                .andExpect(jsonPath("$.data.role").value("CEO"))
                .andExpect(jsonPath("$.data.companyOverview.totalEmployees").isNumber())
                .andExpect(jsonPath("$.data.globalFinancials.totalSalesRevenue").isNumber())
                .andExpect(jsonPath("$.data.plants", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.departments", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.productMargins", hasSize(greaterThan(0))));

        // CEO can also access any department's data without restriction
        mockMvc.perform(get("/api/depthead/dashboard?departmentId=2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.departmentId").value(2));
    }

    @Test
    @DisplayName("9. RBAC Security - Operator denied access to executive endpoint")
    void testRbacForbidden() throws Exception {
        String token = obtainAccessToken("operator1", "password123", "OPERATOR");

        mockMvc.perform(get("/api/executive/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }
}

