package ru.khomyakov.authservice.services;

import ru.khomyakov.authservice.models.dto.JwtResponse;
import ru.khomyakov.authservice.models.dto.LoginRequest;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
}
