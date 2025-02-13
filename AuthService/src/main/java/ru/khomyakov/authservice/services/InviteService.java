package ru.khomyakov.authservice.services;

import ru.khomyakov.authservice.models.dto.RestoreMessage;
import ru.khomyakov.authservice.models.dto.RestoreTokenMessage;
import ru.khomyakov.authservice.models.entities.Invite;
import ru.khomyakov.authservice.models.entities.User;

public interface InviteService {
    RestoreTokenMessage startProcessPasswordRestore(User user);
}

