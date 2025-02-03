package ru.khomyakov.authservice.services.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.exceptions.AuthorizationException;
import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.entities.User;
import ru.khomyakov.authservice.repositories.UserRepository;
import ru.khomyakov.authservice.services.UserService;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByLoginRequest(LoginRequest loginRequest) {
        User user = userRepository.findUserByEmailAndIsEnabled(loginRequest.email(), true);
        validate(user, loginRequest);
        return user;
    }

    private void validate(User user, @NotBlank LoginRequest loginRequest) {
        if (user == null) {
            log.info("Account with login {} not found", loginRequest.email());
            throw new AuthorizationException(String.format("Account with email %s not found", loginRequest.email()));
        }
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            log.info("Account with login {} failed. Password incorrect", loginRequest.email());
        }
    }
}
