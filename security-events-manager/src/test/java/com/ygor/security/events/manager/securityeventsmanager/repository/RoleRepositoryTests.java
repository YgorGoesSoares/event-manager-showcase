package com.ygor.security.events.manager.securityeventsmanager.repository;

import com.ygor.security.events.manager.securityeventsmanager.entities.Role;
import com.ygor.security.events.manager.securityeventsmanager.factory.RoleFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private Long existingIdRole;
    private Long notExistingIdRole;
    private Long countTotalRoles;


    @BeforeEach
    void setUp() throws Exception{
        this.existingIdRole = 1L;
        this.notExistingIdRole = 10L;
        this.countTotalRoles = roleRepository.count();
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Role role = RoleFactory.createRole();
        role.setId(null);

        role = roleRepository.save(role);

        Assertions.assertTrue(roleRepository.existsById(role.getId()));
        Assertions.assertEquals(countTotalRoles+1L, roleRepository.count());
        Assertions.assertNotNull(role);


    }

    @Test
    public void deleteShouldDeleteRoleWhenIdExists() {
        userRepository.deleteAll();
        roleRepository.deleteById(existingIdRole);

        Assertions.assertFalse(roleRepository.existsById(existingIdRole));
    }

    @Test
    public void deleteShouldNotDoAnythingWhenIdDoesNotExists() {
        roleRepository.deleteById(notExistingIdRole);
        Assertions.assertEquals(countTotalRoles, roleRepository.count());
    }

    @Test
    public void findByIdShouldReturnAOptionalRoleNotNullWhenIdExists() {
        Optional<Role> role = roleRepository.findById(existingIdRole);
        Assertions.assertTrue(role.isPresent());
    }

    @Test
    public void findByIdShouldReturnAOptionalRoleEmptyWhenIdDoesNotExists() {
        Optional<Role> role = roleRepository.findById(notExistingIdRole);
        Assertions.assertTrue(role.isEmpty());
    }

}
