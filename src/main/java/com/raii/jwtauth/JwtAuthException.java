package com.raii.jwtauth;

import com.raii.jwtauth.api.v1.model.ErrorResponse;
import com.raii.jwtauth.service.exceptions.UserAlreadyExistsWithFieldException;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum JwtAuthException {
    USER_ALREADY_EXISTS(
            "A User with this '%s' already exists",
            (e, detail) -> String.format(detail, ((UserAlreadyExistsWithFieldException) e).getField()),
            HttpStatus.CONFLICT
    ),
    BAD_CREDENTIALS(
            "Username and/or password is incorrect",
            null,
            HttpStatus.UNAUTHORIZED
    ),
    /* Refresh token is revoked/expired. */
    INVALID_REFRESH_TOKEN(
            "Not valid refresh token provided",
            null,
            HttpStatus.BAD_REQUEST
    ),
    INVALID_TOKEN(
            "Token malformed or expired",
            null,
            HttpStatus.BAD_REQUEST
    ),
    ACCESS_DENIED(
            "Not authorized or insufficient rights",
            null,
            HttpStatus.UNAUTHORIZED
    ),
    NOT_FOUND(
            "No static resource %s",
            (e, detail) -> String.format(detail, ((NoResourceFoundException) e).getResourcePath()),
            HttpStatus.NOT_FOUND
    );

    private final String detail;

    @Nullable
    private final BiFunction<Exception, String, String> formatter;
    private final HttpStatus status;

    public String getDetail(Exception e) {
        return (formatter != null) ? formatter.apply(e, detail) : detail;
    }

    private ResponseEntity<ErrorResponse> buildResponse(String computed) {
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .detail(String.format("%s: %s", this.name(), computed))
                        .build());
    }

    public ResponseEntity<ErrorResponse> issueResponseEntity() {
        return buildResponse(getDetail());
    }

    public ResponseEntity<ErrorResponse> issueResponseEntity(Exception e) {
        return buildResponse(getDetail(e));
    }
}
