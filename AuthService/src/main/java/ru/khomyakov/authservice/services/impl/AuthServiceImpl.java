package ru.khomyakov.authservice.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.exceptions.AuthorizationException;
import ru.khomyakov.authservice.jwtUtils.JwtProvider;
import ru.khomyakov.authservice.models.dto.JwtResponse;
import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.dto.RestoreMessage;
import ru.khomyakov.authservice.models.dto.RestoreTokenMessage;
import ru.khomyakov.authservice.models.dto.converters.DtoConverter;
import ru.khomyakov.authservice.models.entities.Invite;
import ru.khomyakov.authservice.models.entities.User;
import ru.khomyakov.authservice.services.*;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final TokenService tokenService;
    private final InviteService inviteService;
    private final MessageService messageService;

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

    @Override
    public RestoreMessage startPasswordRestore(String email) {
        User user = userService.findUserByEmail(email);
        RestoreTokenMessage message = inviteService.startProcessPasswordRestore(user);
        messageService.sendRestorePasswordEmail(message.responseUuid(), message.token(), email);
        RestoreMessage response = DtoConverter.convertRestoreTokenMessageToRestoreMessage(message);
        return response;
    }
}
