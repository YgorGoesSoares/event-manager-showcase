package com.ygor.security.events.manager.securityeventsmanager.factory;

import com.ygor.security.events.manager.securityeventsmanager.dtos.UserDTO;
import com.ygor.security.events.manager.securityeventsmanager.entities.Role;
import com.ygor.security.events.manager.securityeventsmanager.entities.User;

import java.util.HashSet;
import java.util.Set;

public class UserFactory {
    public static User createUserRoleClient() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, "ROLE_CLIENT"));
        user.setEmail("email@teste.com");
        user.setPassword("password123");
        user.setRoles(roles);
        return user;

    }

    public static User createUserRoleAdmin() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2L, "ROLE_ADMIN"));
        user.setEmail("email@teste.com");
        user.setPassword("password123");
        user.setRoles(roles);
        return user;

    }

    public static UserDTO createUserDTORoleClient() {
        User user = createUserRoleClient();
        return new UserDTO(user);
    }

    public static UserDTO createUserDTORoleAdmin() {
        User user = createUserRoleAdmin();
        return new UserDTO(user);
    }
}
