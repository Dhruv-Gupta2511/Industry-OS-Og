package com.industryos.service;

import com.industryos.config.JwtTokenProvider;
import com.industryos.dto.request.LoginRequest;
import com.industryos.dto.response.AuthResponse;
import com.industryos.entity.Department;
import com.industryos.entity.UserAccount;
import com.industryos.exception.BadRequestException;
import com.industryos.exception.UnauthorizedException;
import com.industryos.repository.DepartmentRepository;
import com.industryos.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final DepartmentRepository departmentRepository;

    public AuthService(UserAccountRepository userAccountRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       DepartmentRepository departmentRepository) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.departmentRepository = departmentRepository;
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
            // Handle TRANSPORTER/CONTRACTOR equivalence
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
}

