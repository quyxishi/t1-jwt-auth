package com.raii.jwtauth.api.v1.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Login request")
public final class LoginRequest {
    @Schema(description = "Login")
    @NotBlank(message = "Login can't be empty")
    @Size(min = 3, max = 20, message = "Login must contain between 3 and 20 characters")
    private String login;

    @Schema(description = "Password")
    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 255, message = "Password must contain between 8 and 255 characters")
    private String passwd;
}
