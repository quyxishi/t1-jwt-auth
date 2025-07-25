package com.raii.jwtauth.api.v1.auth.model;

import com.raii.jwtauth.api.v1.model.SuccessResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Login response")
public final class TokensResponse extends SuccessResponse {
    @Schema(description = "Access JWT-token")
    private final String access_token;

    @Schema(description = "Refresh JWT-token")
    private final String refresh_token;
}
