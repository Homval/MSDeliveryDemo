package ru.khomyakov.authservice.services.impl;

import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public void sendNewUserRegisterEmail(String email, String password) {
//        TODO
    }
}
