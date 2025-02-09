package ru.khomyakov.authservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.models.entities.Role;
import ru.khomyakov.authservice.models.enums.RoleEnum;
import ru.khomyakov.authservice.repositories.RoleRepository;
import ru.khomyakov.authservice.services.RoleService;

import java.util.EnumMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private static final EnumMap<RoleEnum, Role> roles = new EnumMap<>(RoleEnum.class);

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        fillRoles();
    }

    private void fillRoles() {
        for (RoleEnum role : RoleEnum.values()) {
            roles.put(role, roleRepository.save(Role.builder()
                            .name(role.name())
                            .build()));
        }
    }

    @Override
    public Set<Role> getRole(Set<RoleEnum> rolesNames) {
        return rolesNames.stream()
                .map(roles::get)
                .collect(Collectors.toSet());
    }
}
