# INDUSTRY-OS: Enterprise Industrial Management Backend

A high-performance, production-ready Java Spring Boot RESTful API backend for **INDUSTRY-OS**, built on top of the comprehensive 42-table industrial management relational database architecture.

---

## 🛠 Tech Stack & Architecture

- **Language / Runtime:** Java 17+ (Java 27 runtime verified)
- **Framework:** Spring Boot 3.3.4 (Spring Web, Spring Security 6, Spring Data JPA, Bean Validation)
- **Security:** Stateless JWT authentication (JJWT 0.12.6) with HMAC-SHA256 and BCrypt password encryption
- **Databases Supported:**
  - **Embedded Default:** H2 in MySQL-compatibility mode (`MODE=MySQL`) for instant local testing and demonstration.
  - **MySQL 8.0+:** `com.mysql:mysql-connector-j` driver (`application-mysql.properties`).
  - **PostgreSQL 14+:** `org.postgresql:postgresql` driver (`application-postgres.properties`).
- **Architecture Layers:**
  - `com.industryos.controller`: Clean REST API endpoints with `@PreAuthorize` method security.
  - `com.industryos.service`: Core business rules, aggregation algorithms, dynamic invoice computation, and health score calculations.
  - `com.industryos.repository`: Spring Data JPA repositories with query methods.
  - `com.industryos.entity`: 25+ JPA Entities mapping all industrial management schema tables.
  - `com.industryos.dto`: Strictly decoupled Request and Response DTOs.
  - `com.industryos.config`: Security filter chain, JWT provider, UserDetailsService, and DataInitializer.
  - `com.industryos.exception`: Centralized `@RestControllerAdvice` exception handler.

---

## 🔐 Role-Based Access Control (RBAC) & Seed Credentials

All accounts come pre-configured with the default password: **`password123`**.

| Role | Username | Password | Linked Entity / Scope | Description |
|---|---|---|---|---|
| **`CEO`** | `ceo` | `password123` | Suresh Pillai (Corporate Head) | Global unrestricted access across all departments, plants & financial records |
| **`GENERAL_MANAGER`** | `gm` | `password123` | Anil Singh (GM) | Global unrestricted operational & financial access |
| **`DEPT_HEAD`** | `depthead_prod` | `password123` | Geeta Gupta (Dept 1: Production) | Programmatic access scoped strictly to Dept 1 (Employees, Lines, Machines, Work Orders) |
| **`DEPT_HEAD`** | `depthead_maint` | `password123` | Ashok Saxena (Dept 2: Maintenance) | Programmatic access scoped strictly to Dept 2 (Machinery, Maintenance Logs, Part Usages) |
| **`OPERATOR`** | `operator1` | `password123` | Aarti Yadav (Machine 1: CNC Lathe) | Scoped to assigned machine: real-time health score & live spare parts stock |
| **`EMPLOYEE`** | `employee1` | `password123` | Nitin Mishra (Supervisor) | General employee directory profile |
| **`CUSTOMER`** | `customer1` | `password123` | Acme Engineering Solutions Ltd (Cust 5) | Active shopping cart, previous order history & dynamic live invoice |
| **`TRANSPORTER`** | `transporter1` | `password123` | SafeGuard Express Logistics (Contractor 1) | Fleet vehicles, active 2-year contract terms, and trip-to-invoice mapping |

---

## 🚀 How to Run

### Option 1: Run via Maven Wrapper / Installed Maven
```bash
# In the industry-os directory:
mvn spring-boot:run
```

To run with MySQL:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### Option 2: Run the Packaged Executable JAR
```bash
java -jar target/industry-os-1.0.0.jar
```

The server will start on port **`8080`**.

---

## 📡 Core API Endpoints

### 1. Authentication & Profile
- **`POST /api/auth/login`**
  - **Request Body:**
    ```json
    {
      "username": "operator1",
      "password": "password123",
      "userType": "OPERATOR"
    }
    ```
  - **Response:**
    ```json
    {
      "success": true,
      "message": "Login successful",
      "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "tokenType": "Bearer",
        "userId": 5,
        "username": "operator1",
        "fullName": "Aarti Yadav",
        "role": "OPERATOR",
        "department": "Production"
      }
    }
    ```

- **`GET /api/users/me`**
  - Header: `Authorization: Bearer <token>`
  - Returns logged-in user profile, role, department name, project/plant, and assigned entity references.

---

### 2. Role-Specific Endpoints

#### A. OPERATOR (Machine Assigned)
- **`GET /api/operator/machine/inventory`**
  - **Authorized Roles:** `OPERATOR`, `CEO`, `GENERAL_MANAGER`
  - **Returns:**
    - Assigned machine details (CNC Lathe, Mazak QT-200, Line A, Pune Works, purchase cost, last service).
    - Machine health rating (`healthScore`, `healthStatus`: `EXCELLENT`, accumulated downtime, labour cost, parts cost, service recommendation).
    - Live inventory count of the exact spare parts needed for that machine (`partName`, `stockQty`, `reorderLevel`, `stockStatus`: `ADEQUATE` / `LOW_STOCK`, `totalUsedOnThisMachine`).
    - Recent maintenance service history logs.

#### B. CUSTOMER (Retailer / Buyer)
- **`GET /api/customer/dashboard`**
  - **Authorized Roles:** `CUSTOMER`, `CEO`, `GENERAL_MANAGER`
  - **Returns:**
    - Active shopping cart with itemized lines, quantity, unit price, tax (18%), and subtotal.
    - Previous orders list (`Delivered`, `Shipped`, `Placed`) with shipment and invoice statuses.
    - Dynamically generated invoice for the latest order (`subtotal`, `18% GST tax`, `totalPayable`, `amountPaid`, `outstandingBalance`, payment status).

#### C. TRANSPORTER (Contractor / Logistics)
- **`GET /api/transporter/dashboard`**
  - **Authorized Roles:** `TRANSPORTER`, `CEO`, `GENERAL_MANAGER`
  - **Returns:**
    - Vehicle fleet details (`regNo`, `capacityTons`, `driverName`, status: `Available` / `On Trip` / `Under Repair`).
    - Active contract terms (contract value, start date, end date, remaining days, plant location).
    - Payment details & invoices mapped directly to each specific truck trip.
    - Logistics fleet financials summary.

#### D. DEPT_HEAD (Department Head)
- **`GET /api/depthead/dashboard`**
- **`GET /api/depthead/aggregate`**
  - **Authorized Roles:** `DEPT_HEAD`, `CEO`, `GENERAL_MANAGER`
  - **Access Rule:** Strict department isolation. A Department Head can **only** access data for their assigned department. Cross-department requests return `401 Unauthorized`.
  - **Returns:**
    - Staff headcount overview (total, active, on leave, resigned).
    - List of department employees with designations, grades, and shifts.
    - Machinery and production lines tied to the department.
    - Work orders and production batch progress (`progressPercent`, `goodProducedQty`, `rejectedQty`).
    - Maintenance service history.
    - Departmental budget metrics (`budgetPerEmployee`, staff ratios).

#### E. CEO & GENERAL_MANAGER (Global Access)
- **`GET /api/executive/dashboard`**
- **`GET /api/executive/financials`**
- **`GET /api/executive/plants`**
- **`GET /api/executive/departments`**
  - **Authorized Roles:** `CEO`, `GENERAL_MANAGER`
  - **Returns:**
    - Unrestricted company overview across all 42 database tables.
    - Global financials (total sales revenue, invoiced total, cleared collections, accounts receivable, maintenance expenses, total fleet capacity).
    - Plant-by-plant operational metrics (capacities, employees, lines, energy kWh consumption, energy cost, safety incident records).
    - Department budgets breakdown.
    - Product profitability margins (unit price, standard cost, margin per unit, margin %).
    - Cross-department drill-down capabilities.

---

## 🧪 Automated Test Suite

Run the full integration test suite:
```bash
mvn test
```

All 10 integration tests verify:
- Complete authentication flow across all 7 roles.
- Strict route protection and 403 Forbidden checks for unauthorized roles.
- Department isolation checks for DEPT_HEAD.
- Operator machine inventory and health calculation.
- Customer active cart and dynamic invoice generation.
- Transporter fleet and trip-to-invoice mapping.
- Executive global aggregate financials.
