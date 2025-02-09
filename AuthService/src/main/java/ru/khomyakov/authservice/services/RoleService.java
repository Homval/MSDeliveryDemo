package ru.khomyakov.authservice.services;

import ru.khomyakov.authservice.models.entities.Role;
import ru.khomyakov.authservice.models.enums.RoleEnum;

import java.util.Set;

public interface RoleService {
    Set<Role> getRole(Set<RoleEnum> role);
}
