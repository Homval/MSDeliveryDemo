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
import ru.khomyakov.authservice.services.MessageService;
import ru.khomyakov.authservice.services.RoleService;
import ru.khomyakov.authservice.services.UserService;

import java.util.Set;
import java.util.UUID;

import static ru.khomyakov.authservice.models.enums.RoleEnum.USER;
import static ru.khomyakov.authservice.utils.PasswordGenerator.generatePassword;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;

    @Override
    public User loadUserByLoginRequest(LoginRequest loginRequest) {
        User user = userRepository.findUserByEmailAndIsEnabled(loginRequest.email(), true);
        validate(user, loginRequest);
        return user;
    }

    @Override
    public User getById(UUID userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public User createNewUser(String email) {
        String password = generatePassword();
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setIsEnabled(true);
        user.setRoles(roleService.getRole(Set.of(USER)));

        messageService.sendNewUserRegisterEmail(email, password);

        return userRepository.save(user);
    }

    @Override
    public void validateNewUserEmail(String email) {
        User user = userRepository.findUserByEmailAndIsEnabled(email, true);

        if (user != null) {
            log.error("Email {} already exists", email);
            throw new AuthorizationException(String.format("Email %s already registered", email));
        }
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmailAndIsEnabled(email, true);
        if (user == null) {
            throw new AuthorizationException(String.format("Account with email %s not found", email));
        }
        return user;
    }

    private void validate(User user, @NotBlank LoginRequest loginRequest) {
        if (user == null) {
            log.error("Account with login {} not found", loginRequest.email());
            throw new AuthorizationException(String.format("Account with email %s not found", loginRequest.email()));
        }
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            log.error("Account with login {} failed. Password incorrect", loginRequest.email());
        }
    }
}
