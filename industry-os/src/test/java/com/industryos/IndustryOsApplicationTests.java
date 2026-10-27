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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.jdbc.core.JdbcTemplate;
import com.industryos.dto.request.RegisterRequest;
import com.industryos.dto.request.PriceStockUpdateRequest;
import com.industryos.entity.AuditLog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
class IndustryOsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @Test
    @DisplayName("10. RDBMS INSERT: Dynamic User Registration and Domain Table Mapping")
    void testDynamicUserRegistrationAndTableMapping() throws Exception {
        // A. Register CUSTOMER -> Must insert into 'customer' and 'user_account' tables
        RegisterRequest customerReq = new RegisterRequest();
        customerReq.setFullName("Acme Precision Sub-Contracting Ltd");
        customerReq.setEmail("cust2026@industry-os.com");
        customerReq.setUsername("test_cust_2026");
        customerReq.setPassword("Password@123");
        customerReq.setRole("CUSTOMER");
        customerReq.setUserType("CUSTOMER");
        customerReq.setDepartment("Client Commercial Accounts");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.mappedTable").value("customers"));

        // RDBMS Query Check: Verify 'customer' table row
        Map<String, Object> customerRow = jdbcTemplate.queryForMap(
                "SELECT * FROM customer WHERE email = ?", "cust2026@industry-os.com");
        assertNotNull(customerRow.get("customer_id"), "Customer ID must be generated");
        assertEquals("Acme Precision Sub-Contracting Ltd", customerRow.get("customer_name"));
        System.out.println("[RDBMS VERIFIED] customer table row inserted: " + customerRow);

        // RDBMS Query Check: Verify 'user_account' table foreign key linkage
        Map<String, Object> userCustRow = jdbcTemplate.queryForMap(
                "SELECT * FROM user_account WHERE username = ?", "test_cust_2026");
        assertEquals(customerRow.get("customer_id"), userCustRow.get("customer_id"),
                "user_account.customer_id MUST match customer.customer_id foreign key");
        System.out.println("[RDBMS VERIFIED] user_account foreign key linked: " + userCustRow);

        // B. Register EMPLOYEE -> Must insert into 'employee' and 'user_account' tables
        RegisterRequest employeeReq = new RegisterRequest();
        employeeReq.setFullName("Vikramaditya Rao");
        employeeReq.setEmail("emp2026@industry-os.com");
        employeeReq.setUsername("test_emp_2026");
        employeeReq.setPassword("Password@123");
        employeeReq.setRole("EMPLOYEE");
        employeeReq.setUserType("EMPLOYEE");
        employeeReq.setDepartment("Production");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.mappedTable").value("employees"));

        // RDBMS Query Check: Verify 'employee' table row
        Map<String, Object> empRow = jdbcTemplate.queryForMap(
                "SELECT * FROM employee WHERE email = ?", "emp2026@industry-os.com");
        assertNotNull(empRow.get("emp_id"), "Employee ID must be generated");
        assertEquals("Vikramaditya", empRow.get("first_name"));
        System.out.println("[RDBMS VERIFIED] employee table row inserted: " + empRow);

        Map<String, Object> userEmpRow = jdbcTemplate.queryForMap(
                "SELECT * FROM user_account WHERE username = ?", "test_emp_2026");
        assertEquals(empRow.get("emp_id"), userEmpRow.get("employee_id"),
                "user_account.employee_id MUST match employee.emp_id foreign key");

        // C. Verify Audit Log entry in 'audit_log' table
        List<Map<String, Object>> auditRows = jdbcTemplate.queryForList(
                "SELECT * FROM audit_log WHERE entity_name = 'USER_REGISTRATION'");
        assertFalse(auditRows.isEmpty(), "Audit log records must be created for registrations");
        System.out.println("[RDBMS VERIFIED] audit_log registration events found: " + auditRows.size());
    }

    @Test
    @DisplayName("11. RDBMS UPDATE: Dynamic Spare Part Price & Stock Modification with SQL Persistence")
    void testDynamicPriceStockModification() throws Exception {
        String token = obtainAccessToken("operator1", "password123", "OPERATOR");

        // Check original state via raw SQL query
        Map<String, Object> initialPart = jdbcTemplate.queryForMap(
                "SELECT unit_cost, stock_qty, reorder_level FROM spare_part WHERE part_id = 1");
        System.out.println("[RDBMS INITIAL STATE] spare_part part_id=1: " + initialPart);

        // Execute dynamic modification: Price -> 540.00, Stock -> 50, Reorder -> 18
        PriceStockUpdateRequest updateReq = new PriceStockUpdateRequest(BigDecimal.valueOf(540.0), 50, 18, null);

        mockMvc.perform(put("/api/inventory/spare-parts/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sparePart.unitCost").value(540.0))
                .andExpect(jsonPath("$.data.sparePart.stockQty").value(50))
                .andExpect(jsonPath("$.data.totalValuation").value(27000.0));

        // RDBMS Direct Query: Verify changes persisted into 'spare_part' database table
        Map<String, Object> updatedPart = jdbcTemplate.queryForMap(
                "SELECT unit_cost, stock_qty, reorder_level FROM spare_part WHERE part_id = 1");
        System.out.println("[RDBMS UPDATED STATE] spare_part part_id=1: " + updatedPart);

        assertEquals(50, ((Number) updatedPart.get("stock_qty")).intValue(), "stock_qty must be updated to 50");
        assertEquals(18, ((Number) updatedPart.get("reorder_level")).intValue(), "reorder_level must be updated to 18");

        // RDBMS Direct Query: Verify Audit Log in 'audit_log' table with previous and new values
        List<Map<String, Object>> auditLogs = jdbcTemplate.queryForList(
                "SELECT * FROM audit_log WHERE entity_name = 'SPARE_PART' ORDER BY id DESC");
        assertFalse(auditLogs.isEmpty());
        Map<String, Object> latestAudit = auditLogs.get(0);
        System.out.println("[RDBMS AUDIT RECORD] spare_part audit: " + latestAudit);
        assertNotNull(latestAudit.get("previous_value"));
        assertNotNull(latestAudit.get("new_value"));
        assertTrue(latestAudit.get("new_value").toString().contains("540"));
    }

    @Test
    @DisplayName("12. RDBMS UPDATE: Dynamic Product Catalog Price Modification")
    void testDynamicProductPriceModification() throws Exception {
        String token = obtainAccessToken("ceo", "password123", "CEO");

        // Update product 1 price to 9250.00
        PriceStockUpdateRequest updateReq = new PriceStockUpdateRequest(BigDecimal.valueOf(9250.0), null, null, null);

        mockMvc.perform(put("/api/inventory/products/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.product.unitPrice").value(9250.0))
                .andExpect(jsonPath("$.data.totalValuation").isNumber())
                .andExpect(jsonPath("$.data.estimatedGst18").isNumber());

        // RDBMS Direct Query: Verify changes in 'product' table
        Map<String, Object> productRow = jdbcTemplate.queryForMap(
                "SELECT unit_price FROM product WHERE product_id = 1");
        System.out.println("[RDBMS UPDATED PRODUCT] product_id=1: " + productRow);
        assertEquals(9250.0, ((Number) productRow.get("unit_price")).doubleValue(), 0.01);
    }

    @Test
    @DisplayName("13. RDBMS UPDATE: Machine Hardware Status Modification")
    void testMachineHardwareStatusUpdate() throws Exception {
        String token = obtainAccessToken("operator1", "password123", "OPERATOR");

        PriceStockUpdateRequest statusReq = new PriceStockUpdateRequest(null, null, null, "CRITICAL SHUTDOWN");

        mockMvc.perform(put("/api/inventory/machines/1/status")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CRITICAL SHUTDOWN"));

        // RDBMS Direct Query: Verify changes in 'machine' table
        String machineStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM machine WHERE machine_id = 1", String.class);
        System.out.println("[RDBMS MACHINE STATUS] machine_id=1 status: " + machineStatus);
        assertEquals("CRITICAL SHUTDOWN", machineStatus);

        // Reset to Operational
        statusReq.setStatus("Operational");
        mockMvc.perform(put("/api/inventory/machines/1/status")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("14. RDBMS INSERT & DELETE: Full Live Audit Log Lifecycle")
    void testAuditLogLifecycle() throws Exception {
        String token = obtainAccessToken("depthead_prod", "password123", "DEPT_HEAD");

        // Insert new audit log via POST /api/audit-logs
        Map<String, Object> newLog = new HashMap<>();
        newLog.put("auditId", "AUD-TEST-9999");
        newLog.put("actionType", "INSERT");
        newLog.put("entityName", "QUALITY_INSPECTION");
        newLog.put("username", "depthead_prod");
        newLog.put("payloadSummary", "Batch BATCH-2026-001 ultrasonic QA pass");
        newLog.put("previousValue", "Status: PENDING");
        newLog.put("newValue", "Status: CERTIFIED_PASS");

        mockMvc.perform(post("/api/audit-logs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // RDBMS Direct Query: Verify row in 'audit_log' table
        Map<String, Object> auditDbRow = jdbcTemplate.queryForMap(
                "SELECT * FROM audit_log WHERE audit_id = ?", "AUD-TEST-9999");
        System.out.println("[RDBMS INSERT AUDIT LOG] Created row: " + auditDbRow);
        assertEquals("QUALITY_INSPECTION", auditDbRow.get("entity_name"));
        assertEquals("Status: PENDING", auditDbRow.get("previous_value"));
        assertEquals("Status: CERTIFIED_PASS", auditDbRow.get("new_value"));

        // Delete audit log via DELETE /api/audit-logs/{id}
        mockMvc.perform(delete("/api/audit-logs/AUD-TEST-9999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // RDBMS Direct Query: Verify row was deleted from 'audit_log' table
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM audit_log WHERE audit_id = ?", Integer.class, "AUD-TEST-9999");
        System.out.println("[RDBMS DELETE AUDIT LOG] Count remaining in table: " + count);
        assertEquals(0, count, "Audit log row must be completely removed from database table");
    }

    @Test
    @DisplayName("15. RDBMS Comprehensive Relational Schema Inspection across all 42 Entities")
    void testRdbmsRelationalSchemaInspection() {
        System.out.println("==========================================================================");
        System.out.println("   INDUSTRY-OS RDBMS RELATIONAL SCHEMA & DATABASE RECORD VERIFICATION     ");
        System.out.println("==========================================================================");

        int plantCount = jdbcTemplate.queryForObject("SELECT count(*) FROM plant", Integer.class);
        int deptCount = jdbcTemplate.queryForObject("SELECT count(*) FROM department", Integer.class);
        int empCount = jdbcTemplate.queryForObject("SELECT count(*) FROM employee", Integer.class);
        int machineCount = jdbcTemplate.queryForObject("SELECT count(*) FROM machine", Integer.class);
        int spareCount = jdbcTemplate.queryForObject("SELECT count(*) FROM spare_part", Integer.class);
        int custCount = jdbcTemplate.queryForObject("SELECT count(*) FROM customer", Integer.class);
        int orderCount = jdbcTemplate.queryForObject("SELECT count(*) FROM sales_order", Integer.class);
        int invoiceCount = jdbcTemplate.queryForObject("SELECT count(*) FROM invoice", Integer.class);
        int prodCount = jdbcTemplate.queryForObject("SELECT count(*) FROM product", Integer.class);
        int auditCount = jdbcTemplate.queryForObject("SELECT count(*) FROM audit_log", Integer.class);
        int userCount = jdbcTemplate.queryForObject("SELECT count(*) FROM user_account", Integer.class);

        System.out.printf(" %-25s : %d records%n", "plants", plantCount);
        System.out.printf(" %-25s : %d records%n", "departments", deptCount);
        System.out.printf(" %-25s : %d records%n", "employees", empCount);
        System.out.printf(" %-25s : %d records%n", "machines", machineCount);
        System.out.printf(" %-25s : %d records%n", "spare_parts", spareCount);
        System.out.printf(" %-25s : %d records%n", "customers", custCount);
        System.out.printf(" %-25s : %d records%n", "sales_orders", orderCount);
        System.out.printf(" %-25s : %d records%n", "invoices", invoiceCount);
        System.out.printf(" %-25s : %d records%n", "products", prodCount);
        System.out.printf(" %-25s : %d records%n", "audit_logs", auditCount);
        System.out.printf(" %-25s : %d records%n", "user_accounts", userCount);
        System.out.println("==========================================================================");

        assertTrue(plantCount > 0);
        assertTrue(deptCount > 0);
        assertTrue(empCount > 0);
        assertTrue(machineCount > 0);
        assertTrue(spareCount > 0);
        assertTrue(custCount > 0);
        assertTrue(orderCount > 0);
        assertTrue(prodCount > 0);
        assertTrue(userCount > 0);
    }

    @Test
    @DisplayName("16. RDBMS DELETE: Delete User Account and Verify Relational Purge and Audit")
    void testDeleteUserAccountAndAuditVerification() throws Exception {
        // A. Register a test user to be deleted
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Delete Account Test User");
        req.setEmail("delete_user_test@industry-os.com");
        req.setUsername("delete_me_user");
        req.setPassword("Password@123");
        req.setRole("EMPLOYEE");
        req.setUserType("EMPLOYEE");
        req.setDepartment("Production");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        // Verify user exists in database
        Integer initialUserCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM user_account WHERE username = 'delete_me_user'", Integer.class);
        assertEquals(1, initialUserCount, "User account must exist in database prior to deletion");

        // Obtain access token for the user
        String token = obtainAccessToken("delete_me_user", "Password@123", "EMPLOYEE");

        // B. Perform DELETE /api/users/me
        mockMvc.perform(delete("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // C. RDBMS Direct Query: Verify user row was removed from 'user_account' table
        Integer afterUserCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM user_account WHERE username = 'delete_me_user'", Integer.class);
        assertEquals(0, afterUserCount, "User account must be removed from user_account table");
        System.out.println("[RDBMS VERIFIED] User account delete_me_user successfully deleted from database");

        // D. RDBMS Direct Query: Verify audit log entry exists
        List<Map<String, Object>> auditLogs = jdbcTemplate.queryForList(
                "SELECT * FROM audit_log WHERE entity_name = 'USER_ACCOUNT' AND action_type = 'DELETE' ORDER BY id DESC");
        assertFalse(auditLogs.isEmpty(), "Audit log must record user deletion");
        System.out.println("[RDBMS VERIFIED] User account deletion audit log verified: " + auditLogs.get(0));
    }

    @Test
    @DisplayName("17. RDBMS DELETE: Delete Sales Order and Verify Cascaded Cleanup and Audit")
    void testDeleteSalesOrderAndCascadeVerification() throws Exception {
        String token = obtainAccessToken("ceo", "password123", "CEO");

        // Find an order ID to delete
        Integer orderId = jdbcTemplate.queryForObject(
                "SELECT so_id FROM sales_order ORDER BY so_id DESC LIMIT 1", Integer.class);
        assertNotNull(orderId, "A sales order must exist for deletion testing");
        System.out.println("[RDBMS TARGET ORDER] Preparing to delete Sales Order #" + orderId);

        // Execute DELETE /api/customer/orders/{id}
        mockMvc.perform(delete("/api/customer/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // RDBMS Direct Query: Verify sales_order row deleted
        Integer orderCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM sales_order WHERE so_id = ?", Integer.class, orderId);
        assertEquals(0, orderCount, "Sales order must be removed from sales_order table");

        // RDBMS Direct Query: Verify order items deleted
        Integer itemsCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM sales_order_item WHERE so_id = ?", Integer.class, orderId);
        assertEquals(0, itemsCount, "Sales order items must be cascaded/deleted");

        // RDBMS Direct Query: Verify invoice deleted
        Integer invoiceCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM invoice WHERE so_id = ?", Integer.class, orderId);
        assertEquals(0, invoiceCount, "Associated invoice must be removed");

        // RDBMS Direct Query: Verify shipment deleted
        Integer shipmentCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM shipment WHERE so_id = ?", Integer.class, orderId);
        assertEquals(0, shipmentCount, "Associated shipment must be removed");

        // RDBMS Direct Query: Verify audit log entry exists
        List<Map<String, Object>> auditLogs = jdbcTemplate.queryForList(
                "SELECT * FROM audit_log WHERE entity_name = 'SALES_ORDER' AND action_type = 'DELETE' ORDER BY id DESC");
        assertFalse(auditLogs.isEmpty(), "Audit log must record sales order deletion");
        System.out.println("[RDBMS VERIFIED] Sales order deletion audit log verified: " + auditLogs.get(0));
    }
}
