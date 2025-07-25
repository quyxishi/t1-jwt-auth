package com.raii.jwtauth.api.v1.account.model;

import com.raii.jwtauth.api.v1.model.SuccessResponse;
import com.raii.jwtauth.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Me response")
public class MeResponse extends SuccessResponse {
    @Schema(description = "Login")
    private final String login;

    @Schema(description = "Email")
    private final String email;

    @Schema(description = "Roles")
    private final List<UserRole> roles;

    @Schema(description = "Created date")
    private final LocalDateTime createdDate;
}
