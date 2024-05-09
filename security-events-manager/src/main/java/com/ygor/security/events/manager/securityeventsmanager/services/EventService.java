package com.ygor.security.events.manager.securityeventsmanager.services;

import com.ygor.security.events.manager.securityeventsmanager.dtos.EventDTO;
import com.ygor.security.events.manager.securityeventsmanager.entities.City;
import com.ygor.security.events.manager.securityeventsmanager.entities.Event;
import com.ygor.security.events.manager.securityeventsmanager.repository.CityRepository;
import com.ygor.security.events.manager.securityeventsmanager.repository.EventRepository;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.DatabaseException;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final CityRepository cityRepository;

    @Autowired
    EventService(EventRepository eventRepository, CityRepository cityRepository) {
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> findAllPaged(Pageable pageable) {
        Page<Event> list = eventRepository.findAll(pageable);
        return list.map(EventDTO::new);
    }

    @Transactional(readOnly = true)
    public EventDTO findById(Long id) {
        Optional<Event> object = eventRepository.findById(id);
        Event entity = object.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new EventDTO(entity);
    }

    @Transactional
    public EventDTO insertNewEvent(EventDTO event) {
        Event entity = new Event();
        City city = cityRepository.findByName(event.getCity().getName());
        if (city == null) {
            throw new ResourceNotFoundException("City not found on database.");
        }
        copyDtoToEntity(event, entity);
        event.getCity().setId(city.getId());
        entity = eventRepository.save(entity);
        return new EventDTO(entity);

    }

    @Transactional
    public EventDTO updateEvent(Long id, EventDTO event) {
        try {
            Event entity = eventRepository.getReferenceById(id);
            City city = cityRepository.findByName(event.getCity().getName());
            if (city == null) {
                throw new ResourceNotFoundException("City not found on database.");
            }
            event.getCity().setId(city.getId());
            copyDtoToEntity(event, entity);
            entity = eventRepository.save(entity);
            return new EventDTO(entity);
        } catch (EntityNotFoundException e) {
    throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void deleteEvent(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    private void copyDtoToEntity(EventDTO eventDTO, Event entity){
        entity.setCity(eventDTO.getCity());
        entity.setName(eventDTO.getName());
        entity.setUrl(eventDTO.getUrl());
        entity.setDate(eventDTO.getDate());
    }
}
