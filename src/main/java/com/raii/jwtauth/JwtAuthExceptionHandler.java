package com.raii.jwtauth;

import com.raii.jwtauth.api.v1.auth.exceptions.NotValidRefreshTokenException;
import com.raii.jwtauth.api.v1.model.ErrorResponse;
import com.raii.jwtauth.service.exceptions.UserAlreadyExistsWithFieldException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class JwtAuthExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsWithFieldException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsWithFieldException(final UserAlreadyExistsWithFieldException e) {
        return JwtAuthException.USER_ALREADY_EXISTS.issueResponseEntity(e);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(final BadCredentialsException e) {
        return JwtAuthException.BAD_CREDENTIALS.issueResponseEntity(e);
    }

    @ExceptionHandler(NotValidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleNotValidRefreshTokenException(final NotValidRefreshTokenException e) {
        return JwtAuthException.INVALID_REFRESH_TOKEN.issueResponseEntity(e);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(final JwtException e) {
        return JwtAuthException.INVALID_TOKEN.issueResponseEntity(e);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(final SignatureException e) {
        return JwtAuthException.INVALID_TOKEN.issueResponseEntity(e);
    }

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class, AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(final Exception e) {
        return JwtAuthException.ACCESS_DENIED.issueResponseEntity(e);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(final NoResourceFoundException e) {
        return JwtAuthException.NOT_FOUND.issueResponseEntity(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .detail(e.getMessage())
                        .build());
    }
}
