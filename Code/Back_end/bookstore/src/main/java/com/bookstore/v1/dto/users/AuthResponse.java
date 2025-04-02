package com.bookstore.v1.dto.users;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;
}
