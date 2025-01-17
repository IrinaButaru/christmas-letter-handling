package com.christmas.letter.service;

import com.christmas.letter.exception.GlobalExceptionHandler;
import com.christmas.letter.exception.UnauthorizedException;
import com.christmas.letter.model.entity.UserEntity;
import com.christmas.letter.model.response.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserInfoService userInfoService;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    //TODO: remove testing helper method
    public void addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userInfoService.saveUser(userEntity);
    }

    public AuthResponse authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(email);
            return AuthResponse.builder()
                    .token(token)
                    .roles(userInfoService.getRoles(email))
                    .build();
        } else {
            throw new UnauthorizedException(GlobalExceptionHandler.UNAUTHORIZED_MESSAGE);
        }
    }
}
