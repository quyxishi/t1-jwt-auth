package com.raii.jwtauth.api.v1.admin;

import com.raii.jwtauth.api.v1.model.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @DeleteMapping("/revoke/{login}")
    public ResponseEntity<SuccessResponse> revoke(@PathVariable String login) {
        log.info("Refresh token revoke request: {}", login);
        return adminService.revoke(login);
    }
}
