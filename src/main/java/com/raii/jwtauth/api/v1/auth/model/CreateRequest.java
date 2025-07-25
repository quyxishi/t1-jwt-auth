package com.raii.jwtauth.api.v1.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Create request")
public final class CreateRequest {
    @Schema(description = "Login")
    @NotBlank(message = "Login can't be empty")
    @Size(min = 3, max = 20, message = "Login must contain between 3 and 20 characters")
    private String login;

    @Schema(description = "Email")
    @NotBlank(message = "Email can't be empty")
    @Size(min = 5, max = 50, message = "Email must contain between 5 and 50 characters")
    @Email
    private String email;

    @Schema(description = "Password")
    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 255, message = "Password must contain between 8 and 255 characters")
    private String passwd;
}
