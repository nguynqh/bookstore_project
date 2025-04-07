package com.bookstore.v1.service;

import com.bookstore.v1.dto.users.PasswordUpdateRequest;
import com.bookstore.v1.dto.users.UsersDto;
import com.bookstore.v1.entity.Users;
import com.bookstore.v1.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersDto getCurrentUser(String username) {
        Optional<Users> users = usersRepository.findByUsername(username);

        return users.map(this::mapUserToDto).orElse(null);
    }

    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    public UsersDto updateUser(UsersDto usersDto) {
        return null;
    }

    public boolean updatePassword(PasswordUpdateRequest request) {
        return false;
    }

//    -------------------------------------------------------------------------------

    private UsersDto mapUserToDto(Users user) {
        return UsersDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .postalCode(user.getPostalCode())
                .country(user.getCountry())
                .role(user.getRole())
                .build();
    }
}
