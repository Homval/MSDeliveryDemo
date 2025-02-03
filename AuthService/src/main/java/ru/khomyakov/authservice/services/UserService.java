package ru.khomyakov.authservice.services;

import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.entities.User;

public interface UserService {

    User loadUserByLoginRequest(LoginRequest loginRequest);
}
