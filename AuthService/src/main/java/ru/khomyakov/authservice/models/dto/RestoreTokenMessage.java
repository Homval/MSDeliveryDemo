package ru.khomyakov.authservice.models.dto;

import lombok.Builder;
import ru.khomyakov.authservice.models.enums.StatusType;

import java.util.UUID;

@Builder
public record RestoreTokenMessage(UUID responseUuid, String token, StatusType statusType) {
}
