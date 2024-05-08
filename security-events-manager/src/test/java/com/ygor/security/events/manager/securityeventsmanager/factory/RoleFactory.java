package com.ygor.security.events.manager.securityeventsmanager.factory;

import com.ygor.security.events.manager.securityeventsmanager.entities.Role;

public class RoleFactory {
    public static Role createRole() {
        return new Role(3L, "ROLE_CLIENT");
    }
}
