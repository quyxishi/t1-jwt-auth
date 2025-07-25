package com.raii.jwtauth.api.v1.auth;

import com.raii.jwtauth.api.v1.auth.exceptions.NotValidRefreshTokenException;
import com.raii.jwtauth.api.v1.auth.model.CreateRequest;
import com.raii.jwtauth.api.v1.auth.model.LoginRequest;
import com.raii.jwtauth.api.v1.auth.model.RefreshRequest;
import com.raii.jwtauth.api.v1.auth.model.TokensResponse;
import com.raii.jwtauth.service.exceptions.UserAlreadyExistsWithFieldException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(@RequestBody LoginRequest payload) {
        log.info("Login request: {}", payload);
        return authService.login(payload);
    }

    @PostMapping("/create")
    public ResponseEntity<TokensResponse> create(@RequestBody CreateRequest payload)
            throws UserAlreadyExistsWithFieldException {
        log.info("Create request: {}", payload);
        return authService.create(payload);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokensResponse> refresh(@RequestBody RefreshRequest payload)
            throws NotValidRefreshTokenException {
        return authService.refresh(payload);
    }
}
