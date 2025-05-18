package com.bookstore.authservice.service;

import com.bookstore.authservice.dto.UserDTO;
import com.bookstore.authservice.entity.Role;
import com.bookstore.authservice.entity.User;
import com.bookstore.authservice.exception.UserAlreadyExistsException;
import com.bookstore.authservice.repository.RoleRepository;
import com.bookstore.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToUserDTO);
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return mapToUserDTO(user);
    }

    @Transactional
    public UserDTO updateUserRole(UUID id, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        User updatedUser = userRepository.save(user);
        return mapToUserDTO(updatedUser);
    }

    @Transactional
    public UserDTO updateUserStatus(UUID id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setActive(active);
        User updatedUser = userRepository.save(user);

        return mapToUserDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
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