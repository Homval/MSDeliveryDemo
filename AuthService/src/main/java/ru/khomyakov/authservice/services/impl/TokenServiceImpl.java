package ru.khomyakov.authservice.services.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.services.TokenService;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final ValueOperations<String, String> tokenOperations;
    private final String refreshTokenPrefix;
    private final String accessTokenPrefix;
    private final long refreshTokenExpiration;
    private final long accessTokenExpiration;

    public TokenServiceImpl(ValueOperations<String, String> tokenOperations,
                            @Value("${redis.cache.prefix.block-refresh-token}") String refreshTokenPrefix,
                            @Value("${redis.cache.prefix.block-access-token}") String accessTokenPrefix,
                            @Value("${jwt.refresh.expiration}") long refreshTokenExpiration,
                            @Value("${jwt.access.expiration}") long accessTokenExpiration) {
        this.tokenOperations = tokenOperations;
        this.refreshTokenPrefix = refreshTokenPrefix;
        this.accessTokenPrefix = accessTokenPrefix;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    @Override
    public boolean isRefreshTokenBlocked(String token) {
        return tokenOperations.get(refreshTokenPrefix + token) != null;
    }

    @Override
    public void blockRefreshToken(String refreshToken) {
        tokenOperations.set(refreshTokenPrefix + refreshToken, LocalDateTime.now().toString(),
                refreshTokenExpiration, TimeUnit.SECONDS);
        log.info("Refresh token {} blocked", refreshToken);
    }

    @Override
    public void blockAccessToken(String accessToken) {
        tokenOperations.set(accessTokenPrefix + accessToken, LocalDateTime.now().toString(),
                accessTokenExpiration, TimeUnit.SECONDS);
        log.info("Access token {} blocked", accessToken);
    }
}
