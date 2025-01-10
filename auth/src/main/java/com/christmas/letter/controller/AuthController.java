package com.christmas.letter.controller;

import com.christmas.letter.model.AuthRequest;
import com.christmas.letter.model.UserEntity;
import com.christmas.letter.model.response.AuthResponse;
import com.christmas.letter.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    //TODO: remove testing helper method
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserEntity userEntity) {
        authService.addUser(userEntity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public AuthResponse authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        return authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());
    }
}
