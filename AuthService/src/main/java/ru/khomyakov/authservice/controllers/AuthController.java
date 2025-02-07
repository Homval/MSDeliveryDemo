package ru.khomyakov.authservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khomyakov.authservice.models.dto.JwtResponse;
import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.dto.RefreshRequest;
import ru.khomyakov.authservice.services.AuthService;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@NoArgsConstructor
public class AuthController {

    AuthService authService;

    @Operation(summary = "jwt authorization endpoint")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "jwt refreshing endpoint")
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.ok(authService.refresh(refreshRequest.refreshToken()));
    }

    @Operation(summary = "logout")
    @PostMapping("/logout")
    public void logout(@RequestHeader(name = "Authorization") @NotNull String accessToken,
                       @RequestBody @NotNull RefreshRequest refreshRequest) {
        authService.logout(accessToken, refreshRequest.refreshToken());
    }
}
