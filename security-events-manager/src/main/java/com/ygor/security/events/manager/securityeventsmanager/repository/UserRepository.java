package com.ygor.security.events.manager.securityeventsmanager.repository;

import com.ygor.security.events.manager.securityeventsmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
