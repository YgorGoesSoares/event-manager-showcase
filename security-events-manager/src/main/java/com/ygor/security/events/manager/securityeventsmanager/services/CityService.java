package com.ygor.security.events.manager.securityeventsmanager.services;

import com.ygor.security.events.manager.securityeventsmanager.dtos.CityDTO;
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
public class CityService {

    private final CityRepository cityRepository;
    private final EventRepository eventRepository;
    @Autowired
    CityService(EventRepository eventRepository, CityRepository cityRepository) {
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public Page<CityDTO> findAllPaged(Pageable pageable) {
        Page<City> list = cityRepository.findAll(pageable);
        return list.map(CityDTO::new);
    }

    @Transactional(readOnly = true)
    public CityDTO findById(Long id) {
        Optional<City> object = cityRepository.findById(id);
        City entity = object.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new CityDTO(entity);
    }

    @Transactional
    public CityDTO insertNewCity(CityDTO city) {
        City entity = new City();
        copyDtoToEntity(city, entity);
        entity = cityRepository.save(entity);
        return new CityDTO(entity);

    }

    @Transactional
    public CityDTO updateCity(Long id, CityDTO city) {
        try {
            City entity = cityRepository.getReferenceById(id);
            copyDtoToEntity(city, entity);
            entity = cityRepository.save(entity);
            return new CityDTO(entity);
        } catch (EntityNotFoundException e) {
    throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void deleteCity(Long id) {
        try {
            cityRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    private void copyDtoToEntity(CityDTO cityDTO, City entity){

        entity.setName(cityDTO.getName());
        }
}

