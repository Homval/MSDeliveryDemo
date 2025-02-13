package ru.khomyakov.authservice.services;

import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.entities.User;

import java.util.UUID;

public interface UserService {
    User loadUserByLoginRequest(LoginRequest loginRequest);
    User getById(UUID userId);
    User createNewUser(String email);
    void validateNewUserEmail(String email);
    User findUserByEmail(String email);
}
