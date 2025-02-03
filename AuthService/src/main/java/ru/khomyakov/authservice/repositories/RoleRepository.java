package ru.khomyakov.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khomyakov.authservice.models.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
