package ru.khomyakov.authservice.services;

import java.util.UUID;

public interface MessageService {
    void sendNewUserRegisterEmail(String email, String password);
    void sendRestorePasswordEmail(UUID uuid, String token, String email);
}

