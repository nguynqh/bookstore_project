package com.bookstore.authservice.service;

import com.bookstore.authservice.dto.JwtResponse;
import com.bookstore.authservice.dto.LoginRequest;
import com.bookstore.authservice.dto.RegisterRequest;
import com.bookstore.authservice.dto.UserDTO;
import com.bookstore.authservice.entity.Employee;
import com.bookstore.authservice.entity.Role;
import com.bookstore.authservice.entity.User;
import com.bookstore.authservice.exception.UserAlreadyExistsException;
import com.bookstore.authservice.repository.EmployeeRepository;
import com.bookstore.authservice.repository.RoleRepository;
import com.bookstore.authservice.repository.UserRepository;
import com.bookstore.authservice.security.JwtProvider;
import com.bookstore.authservice.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_"))
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());

        // Update last login
        User user = userRepository.findById(userDetails.getId()).orElseThrow();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    @Transactional
    public UserDTO registerUser(RegisterRequest registerRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .passwordHash(encoder.encode(registerRequest.getPassword()))
                .active(true)
                .roles(new HashSet<>())
                .build();

        // Set role
        String roleName = registerRequest.getRole() != null ? registerRequest.getRole().toUpperCase() : "USER";
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role USER is not found")));
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // Create employee if data is provided
        if (registerRequest.getFirstName() != null && registerRequest.getLastName() != null) {
            Employee employee = Employee.builder()
                    .user(savedUser)
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .position(registerRequest.getPosition())
                    .department(registerRequest.getDepartment())
                    .phone(registerRequest.getPhone())
                    .hireDate(LocalDate.now())
                    .build();

            employeeRepository.save(employee);
        }

        return mapToUserDTO(savedUser);
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtProvider.validateJwtToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtProvider.getUsernameFromJwtToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create authentication object
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtProvider.generateAccessToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_"))
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Return the same refresh token
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    public boolean validateToken(String token) {
        return jwtProvider.validateJwtToken(token);
    }

    private UserDTO mapToUserDTO(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserDTO.EmployeeDTO employeeDTO = null;
        if (user.getEmployee() != null) {
            employeeDTO = UserDTO.EmployeeDTO.builder()
                    .id(user.getEmployee().getId())
                    .firstName(user.getEmployee().getFirstName())
                    .lastName(user.getEmployee().getLastName())
                    .position(user.getEmployee().getPosition())
                    .department(user.getEmployee().getDepartment())
                    .phone(user.getEmployee().getPhone())
                    .build();
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .active(user.isActive())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .employee(employeeDTO)
                .build();
    }
}