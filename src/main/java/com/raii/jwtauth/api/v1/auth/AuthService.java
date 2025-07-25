package com.raii.jwtauth.api.v1.auth;

import com.raii.jwtauth.api.v1.auth.exceptions.NotValidRefreshTokenException;
import com.raii.jwtauth.api.v1.auth.model.CreateRequest;
import com.raii.jwtauth.api.v1.auth.model.LoginRequest;
import com.raii.jwtauth.api.v1.auth.model.RefreshRequest;
import com.raii.jwtauth.api.v1.auth.model.TokensResponse;
import com.raii.jwtauth.model.User;
import com.raii.jwtauth.model.UserRole;
import com.raii.jwtauth.security.JwtService;
import com.raii.jwtauth.security.RefreshService;
import com.raii.jwtauth.service.UserService;
import com.raii.jwtauth.service.exceptions.UserAlreadyExistsWithFieldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshService refreshService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<TokensResponse> login(LoginRequest payload)
            throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                payload.getLogin(),
                payload.getPasswd()
        ));

        final var user = userService.userDetailsService()
                .loadUserByUsername(payload.getLogin());

        final var accessToken = jwtService.issueAccessToken(user);
        final var refreshToken = jwtService.issueRefreshToken(user);

        refreshService.storeRefreshToken(user.getUsername(), refreshToken);

        return ResponseEntity.ok(
                TokensResponse.builder()
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build()
        );
    }

    public ResponseEntity<TokensResponse> create(CreateRequest payload)
            throws UserAlreadyExistsWithFieldException {
        var user = User.builder()
                .login(payload.getLogin())
                .email(payload.getEmail())
                .passwdHash(passwordEncoder.encode(payload.getPasswd()))
                .roles(List.of(UserRole.GUEST))
                .build();

        userService.create(user);

        return ResponseEntity.ok(
                TokensResponse.builder()
                        .access_token(jwtService.issueAccessToken(user))
                        .refresh_token(jwtService.issueRefreshToken(user))
                        .build()
        );
    }

    public ResponseEntity<TokensResponse> refresh(RefreshRequest payload)
            throws NotValidRefreshTokenException {
        final var claims = jwtService.validateToken(payload.getRefresh_token());
        final var subject = claims.getSubject();

        if (!refreshService.isActiveRefreshToken(subject, payload.getRefresh_token())) {
            throw new NotValidRefreshTokenException();
        }

        final var principle = userService.userDetailsService().loadUserByUsername(subject);
        final var newRefreshToken = jwtService.issueRefreshToken(principle);

        refreshService.storeRefreshToken(principle.getUsername(), newRefreshToken);

        return ResponseEntity.ok(
                TokensResponse.builder()
                        .access_token(jwtService.issueAccessToken(principle))
                        .refresh_token(newRefreshToken)
                        .build()
        );
    }
}
