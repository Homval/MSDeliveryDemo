package ru.khomyakov.authservice.models.dto;

import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
public record JwtAuthenticationDto(UUID userUuid, List<String> roles, Date createDate, Date expirationDate) {
}
