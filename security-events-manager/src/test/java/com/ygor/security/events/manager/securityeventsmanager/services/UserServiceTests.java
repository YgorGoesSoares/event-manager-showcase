package com.ygor.security.events.manager.securityeventsmanager.services;

import com.ygor.security.events.manager.securityeventsmanager.dtos.UserDTO;
import com.ygor.security.events.manager.securityeventsmanager.entities.Event;
import com.ygor.security.events.manager.securityeventsmanager.entities.User;
import com.ygor.security.events.manager.securityeventsmanager.factory.EventFactory;
import com.ygor.security.events.manager.securityeventsmanager.factory.UserFactory;
import com.ygor.security.events.manager.securityeventsmanager.repository.EventRepository;
import com.ygor.security.events.manager.securityeventsmanager.repository.UserRepository;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.DatabaseException;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private User user;
    private PageImpl<User> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        user = UserFactory.createUserRoleClient();
        page = new PageImpl<>(List.of(user));

        Mockito.when(userRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.doNothing().when(userRepository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(userRepository).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<UserDTO> result = userService.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(userRepository, Mockito.times(1)).findAll(pageable);
    }


    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            userService.deleteUser(dependentId);
        });

        Mockito.verify(userRepository, times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(nonExistingId);
        });

        Mockito.verify(userRepository, times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            userService.deleteUser(existingId);
        });

        Mockito.verify(userRepository, times(1)).deleteById(existingId);
    }


}
