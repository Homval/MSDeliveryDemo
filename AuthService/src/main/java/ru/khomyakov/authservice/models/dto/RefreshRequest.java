package ru.khomyakov.authservice.models.dto;

import lombok.Builder;

@Builder
public record RefreshRequest(String refreshToken) {
}
