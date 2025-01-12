package com.hotelmanager.DatabaseInitializers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hotelmanager.enumerations.User.RoleEnum;
import com.hotelmanager.models.Role;
import com.hotelmanager.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<Role> roles;
        if (roleRepository.count() == 0) {
            roles = initializeRoles();
            System.out.println("Roles have been initialized");
        } else {
            roles = roleRepository.findAll();
        }

        Map<String, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getName, role -> role));

    }

    private List<Role> initializeRoles() {
        List<Role> roles = new ArrayList<>();

        for (RoleEnum roleEnum : RoleEnum.values()) {
            Role role = new Role();
            role.setName(roleEnum.getRoleName());
            role.setUsers(new ArrayList<>());
            roles.add(role);
        }

        return roleRepository.saveAll(roles);
    }
}
