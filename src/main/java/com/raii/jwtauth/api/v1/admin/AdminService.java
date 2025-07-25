package com.raii.jwtauth.api.v1.admin;

import com.raii.jwtauth.api.v1.model.SuccessResponse;
import com.raii.jwtauth.security.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final RefreshService refreshService;

    public ResponseEntity<SuccessResponse> revoke(String login) {
        refreshService.revokeRefreshToken(login);
        return ResponseEntity.ok(SuccessResponse.builder().build());
    }
}
