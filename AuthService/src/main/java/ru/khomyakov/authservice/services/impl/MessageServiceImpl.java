package ru.khomyakov.authservice.services.impl;

import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.services.MessageService;

import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public void sendNewUserRegisterEmail(String email, String password) {
//        TODO
    }

    @Override
    public void sendRestorePasswordEmail(UUID uuid, String token, String email) {
//        TODO
    }
}
