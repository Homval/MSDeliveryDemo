package ru.khomyakov.authservice.repositories;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.khomyakov.authservice.models.dto.LoginRequest;
import ru.khomyakov.authservice.models.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserByEmailAndIsEnabled(@NotBlank String email, boolean b);

}
