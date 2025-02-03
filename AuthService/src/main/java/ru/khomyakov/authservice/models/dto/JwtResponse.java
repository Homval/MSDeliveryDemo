package ru.khomyakov.authservice.models.dto;

import lombok.Builder;

@Builder
public record JwtResponse(String accessToken, String refreshToken) {
}
