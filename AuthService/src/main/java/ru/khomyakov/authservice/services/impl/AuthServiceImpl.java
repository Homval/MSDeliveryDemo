package ru.khomyakov.authservice.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.jwtUtils.JwtProvider;
import ru.khomyakov.authservice.models.dto.JwtResponse;
import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.entities.User;
import ru.khomyakov.authservice.services.AuthService;
import ru.khomyakov.authservice.services.UserService;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        User user = userService.loadUserByLoginRequest(loginRequest);
        return JwtResponse.builder()
                .accessToken(jwtProvider.createAccessToken(user))
                .refreshToken(jwtProvider.createRefreshToken(user))
                .build();
    }
}
