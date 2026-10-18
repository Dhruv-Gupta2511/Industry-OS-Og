package com.industryos.service;

import com.industryos.config.JwtTokenProvider;
import com.industryos.dto.request.LoginRequest;
import com.industryos.dto.request.RegisterRequest;
import com.industryos.dto.response.AuthResponse;
import com.industryos.entity.*;
import com.industryos.exception.BadRequestException;
import com.industryos.exception.UnauthorizedException;
import com.industryos.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final ContractorRepository contractorRepository;
    private final AuditLogRepository auditLogRepository;

    public AuthService(UserAccountRepository userAccountRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       DepartmentRepository departmentRepository,
                       EmployeeRepository employeeRepository,
                       CustomerRepository customerRepository,
                       ContractorRepository contractorRepository,
                       AuditLogRepository auditLogRepository) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.contractorRepository = contractorRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public AuthResponse login(LoginRequest request) {
        UserAccount user = userAccountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        if (!user.isEnabled()) {
            throw new UnauthorizedException("User account is disabled");
        }

        // Validate userType if explicitly provided in the request
        if (StringUtils.hasText(request.getUserType())) {
            String requestedType = request.getUserType().trim().toUpperCase();
            String userRole = user.getRole().name();
            if ("CONTRACTOR".equals(requestedType)) {
                requestedType = "TRANSPORTER";
            }
            if (!userRole.equalsIgnoreCase(requestedType)) {
                throw new BadRequestException("Role mismatch: Account role is " + userRole + " but requested userType is " + request.getUserType());
            }
        }

        String token = tokenProvider.generateToken(user);

        String departmentName = "Enterprise Global";
        if (user.getDepartmentId() != null) {
            departmentName = departmentRepository.findById(user.getDepartmentId())
                    .map(Department::getDeptName)
                    .orElse("Department " + user.getDepartmentId());
        }

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                departmentName
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!StringUtils.hasText(request.getUsername())) {
            throw new BadRequestException("Username cannot be empty");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            throw new BadRequestException("Password cannot be empty");
        }
        if (userAccountRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already registered: " + request.getUsername());
        }

        String rawRole = StringUtils.hasText(request.getRole()) ? request.getRole() : request.getUserType();
        if (!StringUtils.hasText(rawRole)) {
            rawRole = "EMPLOYEE";
        }
        rawRole = rawRole.trim().toUpperCase();

        Role role;
        if (rawRole.contains("CEO") || rawRole.contains("HEAD_OF_COMPANY")) {
            role = Role.CEO;
        } else if (rawRole.contains("GM") || rawRole.contains("GENERAL_MANAGER")) {
            role = Role.GENERAL_MANAGER;
        } else if (rawRole.contains("DEPT") || rawRole.contains("HEAD")) {
            role = Role.DEPT_HEAD;
        } else if (rawRole.contains("CUSTOMER") || rawRole.contains("RETAILER")) {
            role = Role.CUSTOMER;
        } else if (rawRole.contains("TRANSPORTER") || rawRole.contains("CONTRACTOR")) {
            role = Role.TRANSPORTER;
        } else if (rawRole.contains("OPERATOR")) {
            role = Role.OPERATOR;
        } else {
            role = Role.EMPLOYEE;
        }

        String email = StringUtils.hasText(request.getEmail()) ? request.getEmail() : request.getUsername() + "@industry-os.com";
        if (userAccountRepository.existsByEmail(email)) {
            email = request.getUsername() + "." + (System.currentTimeMillis() % 1000) + "@industry-os.com";
        }

        Integer deptId = request.getDepartmentId();
        String departmentName = request.getDepartment();
        if (deptId == null) {
            deptId = 1;
        }
        if (!StringUtils.hasText(departmentName)) {
            departmentName = departmentRepository.findById(deptId)
                    .map(Department::getDeptName)
                    .orElse("Production & Precision Machining");
        }

        // Domain Table Mapping & Insertion
        Integer employeeId = null;
        Integer customerId = null;
        Integer contractorId = null;
        String mappedTable = "employee";

        if (role == Role.CUSTOMER) {
            Customer customer = new Customer();
            customer.setCustomerName(request.getFullName());
            customer.setContactPerson(request.getFullName());
            customer.setEmail(email);
            customer.setPhone("+91 9876543210");
            customer.setCity("Pune");
            customer.setState("Maharashtra");
            customer.setCreditLimit(new BigDecimal("2500000.00"));
            customer.setGstin("27AAB" + (1000 + (int)(Math.random() * 9000)) + "C1Z1");
            Customer savedCustomer = customerRepository.save(customer);
            customerId = savedCustomer.getCustomerId();
            mappedTable = "customer (ID: " + customerId + ")";
        } else if (role == Role.TRANSPORTER) {
            Contractor contractor = new Contractor();
            contractor.setCompanyName(request.getFullName());
            contractor.setServiceType("Heavy Freight Logistics & Transport");
            contractor.setPhone("+91 9811223344");
            contractor.setPlantId(1);
            contractor.setContractStart(LocalDate.now());
            contractor.setContractEnd(LocalDate.now().plusYears(2));
            contractor.setContractValue(new BigDecimal("5000000.00"));
            Contractor savedContractor = contractorRepository.save(contractor);
            contractorId = savedContractor.getContractorId();
            mappedTable = "contractor (ID: " + contractorId + ")";
        } else {
            String[] names = request.getFullName().split(" ", 2);
            String firstName = names[0];
            String lastName = names.length > 1 ? names[1] : "Staff";

            Employee emp = new Employee();
            emp.setFirstName(firstName);
            emp.setLastName(lastName);
            emp.setEmail(email);
            emp.setPhone("+91 9822001122");
            emp.setGender("M");
            emp.setDob(LocalDate.of(1992, 5, 14));
            emp.setHireDate(LocalDate.now());
            emp.setDeptId(deptId);
            emp.setDesigId(role == Role.CEO ? 1 : (role == Role.DEPT_HEAD ? 2 : 3));
            emp.setPlantId(1);
            emp.setShiftId(1);
            emp.setStatus("Active");
            Employee savedEmp = employeeRepository.save(emp);
            employeeId = savedEmp.getEmpId();
            mappedTable = "employee (ID: " + employeeId + ")";
        }

        UserAccount user = new UserAccount(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                role,
                request.getFullName(),
                email
        );
        user.setDepartmentId(deptId);
        user.setEmployeeId(employeeId);
        user.setCustomerId(customerId);
        user.setContractorId(contractorId);
        if (role == Role.OPERATOR) {
            user.setAssignedMachineId(1);
        }
        user.setEnabled(true);

        UserAccount savedUser = userAccountRepository.save(user);

        // Instant Audit Log
        String auditId = "AUD-" + (System.currentTimeMillis() % 100000);
        AuditLog auditLog = new AuditLog(
                auditId,
                "INSERT",
                "USER_REGISTRATION",
                savedUser.getUsername(),
                "Registered " + savedUser.getFullName() + " (" + role + ") mapped to table " + mappedTable,
                "None",
                "User ID: " + savedUser.getId() + ", Role: " + role
        );
        auditLogRepository.save(auditLog);

        String token = tokenProvider.generateToken(savedUser);
        String tableName = (role == Role.CUSTOMER) ? "customers" : ((role == Role.TRANSPORTER) ? "contractors" : "employees");

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getFullName(),
                savedUser.getRole(),
                departmentName,
                tableName
        );
    }
}
