package com.ygor.security.events.manager.securityeventsmanager.services;

import com.ygor.security.events.manager.securityeventsmanager.dtos.EventDTO;
import com.ygor.security.events.manager.securityeventsmanager.entities.Event;
import com.ygor.security.events.manager.securityeventsmanager.factory.EventFactory;
import com.ygor.security.events.manager.securityeventsmanager.repository.EventRepository;
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
public class EventServiceTests {
    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Event event;
    private PageImpl<Event> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        event = EventFactory.createEvent();
        page = new PageImpl<>(List.of(event));

        Mockito.when(eventRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(eventRepository.save(ArgumentMatchers.any())).thenReturn(event);

        Mockito.when(eventRepository.findById(existingId)).thenReturn(Optional.of(event));
        Mockito.when(eventRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.doNothing().when(eventRepository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(eventRepository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(eventRepository).deleteById(dependentId);
    }


    @Test
    public void findAllPagedShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 12);

        Page<EventDTO> result = eventService.findAllPaged(pageable);

        Assertions.assertNotNull(result);

        Mockito.verify(eventRepository, times(1)).findAll(pageable);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            eventService.deleteEvent(dependentId);
        });

        Mockito.verify(eventRepository, times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            eventService.deleteEvent(nonExistingId);
        });

        Mockito.verify(eventRepository, times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            eventService.deleteEvent(existingId);
        });

        Mockito.verify(eventRepository, times(1)).deleteById(existingId);
    }

}
