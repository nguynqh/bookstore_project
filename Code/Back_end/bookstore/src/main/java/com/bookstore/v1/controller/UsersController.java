package com.bookstore.v1.controller;

import com.bookstore.v1.dto.users.UsersDto;
import com.bookstore.v1.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestBody String username) {
        UsersDto usersDto = usersService.getCurrentUser(username);
        return ResponseEntity.ok(usersDto);
    }
}
