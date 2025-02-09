package ru.khomyakov.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khomyakov.authservice.models.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
