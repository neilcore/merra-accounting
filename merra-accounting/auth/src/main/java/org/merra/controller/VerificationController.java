package org.merra.controller;

import org.merra.api.ApiResponse;
import org.merra.dto.AuthResponse;
import org.merra.service.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/req/signup/")
public class VerificationController {
    private final AuthService authService;

    public VerificationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String tokenParam) {
        var res = authService.verifyEmail(tokenParam);
        ApiResponse apiRes = new ApiResponse(
                "Email successfully verified",
                true,
                HttpStatus.CREATED,
                res);

        return ResponseEntity.ok(apiRes);
    }

}
