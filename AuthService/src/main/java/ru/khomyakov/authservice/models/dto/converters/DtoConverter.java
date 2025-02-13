package ru.khomyakov.authservice.models.dto.converters;

import ru.khomyakov.authservice.models.dto.RestoreMessage;
import ru.khomyakov.authservice.models.dto.RestoreTokenMessage;

public class DtoConverter {

    public static RestoreMessage convertRestoreTokenMessageToRestoreMessage(RestoreTokenMessage message) {
        return RestoreMessage.builder()
                .responseId(message.responseUuid())
                .statusType(message.statusType())
                .build();
    }
}
