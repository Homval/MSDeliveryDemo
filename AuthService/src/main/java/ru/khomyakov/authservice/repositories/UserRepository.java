package ru.khomyakov.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khomyakov.authservice.models.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
