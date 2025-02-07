package ru.khomyakov.authservice.services;

public interface TokenService {
    boolean isRefreshTokenBlocked(String token);
    void blockRefreshToken(String refreshToken);
    void blockAccessToken(String accessToken);
}
