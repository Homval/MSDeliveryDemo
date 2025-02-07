package ru.khomyakov.authservice.services;

import jakarta.validation.constraints.NotNull;
import ru.khomyakov.authservice.models.dto.JwtResponse;
import ru.khomyakov.authservice.models.dto.LoginRequest;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse refresh(String refreshToken);
    void logout(@NotNull String accessToken, String refreshToken);
}
