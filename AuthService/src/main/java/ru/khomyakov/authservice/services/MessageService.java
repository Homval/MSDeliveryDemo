package ru.khomyakov.authservice.services;

public interface MessageService {
    void sendNewUserRegisterEmail(String email, String password);
}
