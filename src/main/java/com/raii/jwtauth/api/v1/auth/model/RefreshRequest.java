package com.raii.jwtauth.api.v1.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Token refresh request")
public final class RefreshRequest {
    @Schema(description = "Refresh token")
    @NotBlank(message = "Refresh token can't be empty")
    @Pattern(regexp = "^(?:[\\w-]+\\.){2}[\\w-]*?$")
    private String refresh_token;
}
