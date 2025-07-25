package com.raii.jwtauth.api.v1.account;

import com.raii.jwtauth.api.v1.account.model.MeResponse;
import com.raii.jwtauth.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public ResponseEntity<MeResponse> me(User principal) {
        return ResponseEntity.ok(
                MeResponse.builder()
                        .login(principal.getLogin())
                        .email(principal.getEmail())
                        .roles(principal.getRoles())
                        .createdDate(principal.getCreatedDate())
                        .build()
        );
    }
}
