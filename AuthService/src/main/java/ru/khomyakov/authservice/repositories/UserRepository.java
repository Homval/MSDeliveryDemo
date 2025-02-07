package ru.khomyakov.authservice.repositories;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.khomyakov.authservice.models.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserByEmailAndIsEnabled(@NotBlank String email, boolean b);

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.roles r
        WHERE u.id = :id
        """)
    User findUserById(UUID id);
}
