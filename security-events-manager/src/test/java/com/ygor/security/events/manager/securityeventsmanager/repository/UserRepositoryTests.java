package com.ygor.security.events.manager.securityeventsmanager.repository;

import com.ygor.security.events.manager.securityeventsmanager.entities.User;
import com.ygor.security.events.manager.securityeventsmanager.factory.UserFactory;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private Long existingId;
    private Long notExistingId;
    private Long countTotalUsers;


    @BeforeEach
    void setUp() throws Exception{
        this.existingId = 1L;
        this.notExistingId = 10L;
        this.countTotalUsers = userRepository.count();
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        User user = UserFactory.createUserRoleClient();
        user = userRepository.save(user);

        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(countTotalUsers+1L, user.getId());
        Assertions.assertNotEquals(countTotalUsers, userRepository.count());
        Assertions.assertTrue(userRepository.existsById(user.getId()));


    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        userRepository.deleteById(existingId);

        Assertions.assertFalse(userRepository.existsById(existingId));
    }

    @Test
    public void deleteShouldNotDoAnythingWhenIdDoesNotExists() {
        userRepository.deleteById(notExistingId);
        Assertions.assertEquals(countTotalUsers, userRepository.count());
    }

    @Test
    public void findByIdShouldReturnAOptionalUserNotNullWhenIdExists() {
        Optional<User> user = userRepository.findById(existingId);
        Assertions.assertTrue(user.isPresent());
    }

    @Test
    public void findByIdShouldReturnAOptionalUserEmptyWhenIdDoesNotExists() {
        Optional<User> user = userRepository.findById(notExistingId);
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    public void findByEmailShouldReturnAUserNotNullWhenEmailExists() {
        User user = userRepository.findByEmail("bob@example.com");
        Assertions.assertNotNull(user);
    }

    @Test
    public void findByEmailShouldReturnNullWhenEmailDoesNotExists() {
        User user = userRepository.findByEmail("notexistingmail@notexistingprovider.com");
        Assertions.assertNull(user);
    }

}
