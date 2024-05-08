package com.ygor.security.events.manager.securityeventsmanager.repository;

import com.ygor.security.events.manager.securityeventsmanager.entities.Event;
import com.ygor.security.events.manager.securityeventsmanager.factory.EventFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class EventRepositoryTests {

    @Autowired
    private EventRepository eventRepository;

    private Long existingId;
    private Long notExistingId;
    private Long countTotalEvents;


    @BeforeEach
    void setUp() throws Exception{
        this.existingId = 1L;
        this.notExistingId = 50L;
        this.countTotalEvents = eventRepository.count();
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Event event = EventFactory.createEvent();
        event.setId(null);
        event = eventRepository.save(event);

        Assertions.assertNotNull(event.getId());
        Assertions.assertEquals(countTotalEvents+1L, event.getId());
        Assertions.assertNotEquals(countTotalEvents, eventRepository.count());
        Assertions.assertTrue(eventRepository.existsById(event.getId()));


    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        eventRepository.deleteById(existingId);

        Assertions.assertFalse(eventRepository.existsById(existingId));
    }

    @Test
    public void deleteShouldNotDoAnythingWhenIdDoesNotExists() {
        eventRepository.deleteById(notExistingId);
        Assertions.assertEquals(countTotalEvents, eventRepository.count());
    }

    @Test
    public void findByIdShouldReturnAOptionalEventNotNullWhenIdExists() {
        Optional<Event> event= eventRepository.findById(existingId);
        Assertions.assertTrue(event.isPresent());
    }

    @Test
    public void findByIdShouldReturnAOptionalEventEmptyWhenIdDoesNotExists() {
        Optional<Event> event = eventRepository.findById(notExistingId);
        Assertions.assertTrue(event.isEmpty());
    }

}
