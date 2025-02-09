package ru.khomyakov.authservice.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.exceptions.AuthorizationException;
import ru.khomyakov.authservice.jwtUtils.JwtProvider;
import ru.khomyakov.authservice.models.dto.JwtResponse;
import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.entities.User;
import ru.khomyakov.authservice.services.AuthService;
import ru.khomyakov.authservice.services.TokenService;
import ru.khomyakov.authservice.services.UserService;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        User user = userService.loadUserByLoginRequest(loginRequest);
        return JwtResponse.builder()
                .accessToken(jwtProvider.createAccessToken(user))
                .refreshToken(jwtProvider.createRefreshToken(user))
                .build();
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new AuthorizationException("Invalid refresh token");
        }
        tokenService.blockRefreshToken(refreshToken);

        UUID userId;
        try {
            userId = jwtProvider.getUserIdFromRefreshToken(refreshToken);
        } catch (Exception e) {
            log.error("Invalid refresh token", e);
            throw new AuthorizationException("Invalid refresh token");
        }
        User user = userService.getById(userId);

        return JwtResponse.builder()
                .accessToken(jwtProvider.createAccessToken(user))
                .refreshToken(jwtProvider.createRefreshToken(user))
                .build();
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        tokenService.blockRefreshToken(refreshToken);
        tokenService.blockAccessToken(accessToken);
        log.info("User {} logged out", accessToken);
    }

    @Override
    public void register(String email) {
        userService.validateNewUserEmail(email);
        userService.createNewUser(email);
    }
}
