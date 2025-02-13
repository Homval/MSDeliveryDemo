package ru.khomyakov.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khomyakov.authservice.models.entities.Invite;

public interface InviteRepository extends JpaRepository<Invite, String> {
}
