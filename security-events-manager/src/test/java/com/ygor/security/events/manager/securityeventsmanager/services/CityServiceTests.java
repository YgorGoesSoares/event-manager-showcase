package com.ygor.security.events.manager.securityeventsmanager.services;

import com.ygor.security.events.manager.securityeventsmanager.dtos.CityDTO;
import com.ygor.security.events.manager.securityeventsmanager.dtos.UserDTO;
import com.ygor.security.events.manager.securityeventsmanager.entities.City;
import com.ygor.security.events.manager.securityeventsmanager.factory.CityFactory;
import com.ygor.security.events.manager.securityeventsmanager.factory.UserFactory;
import com.ygor.security.events.manager.securityeventsmanager.repository.CityRepository;
import com.ygor.security.events.manager.securityeventsmanager.repository.CityRepository;
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
public class CityServiceTests {
    @InjectMocks
    private CityService cityService;
    @Mock
    private CityRepository cityRepository;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private City city;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        city = CityFactory.createCity();
        PageImpl<City> page = new PageImpl<>(List.of(city));

        Mockito.when(cityRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(cityRepository.save(ArgumentMatchers.any())).thenReturn(city);

        Mockito.when(cityRepository.findById(existingId)).thenReturn(Optional.of(city));
        Mockito.when(cityRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.doNothing().when(cityRepository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(cityRepository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(cityRepository).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<CityDTO> result = cityService.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(cityRepository, Mockito.times(1)).findAll(pageable);
    }


    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            cityService.deleteCity(dependentId);
        });

        Mockito.verify(cityRepository, times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            cityService.deleteCity(nonExistingId);
        });

        Mockito.verify(cityRepository, times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            cityService.deleteCity(existingId);
        });

        Mockito.verify(cityRepository, times(1)).deleteById(existingId);
    }


}
