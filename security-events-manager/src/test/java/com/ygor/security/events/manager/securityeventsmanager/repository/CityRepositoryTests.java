package com.ygor.security.events.manager.securityeventsmanager.repository;

import com.ygor.security.events.manager.securityeventsmanager.entities.City;
import com.ygor.security.events.manager.securityeventsmanager.factory.CityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class CityRepositoryTests {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private EventRepository eventRepository;

    private Long existingId;
    private Long notExistingId;
    private Long countTotalCitys;


    @BeforeEach
    void setUp() throws Exception{
        this.existingId = 1L;
        this.notExistingId = 50L;
        this.countTotalCitys = cityRepository.count();
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        City city = CityFactory.createCity();
        city.setId(null);
        city = cityRepository.save(city);

        Assertions.assertNotNull(city.getId());
        Assertions.assertEquals(countTotalCitys+1L, city.getId());
        Assertions.assertNotEquals(countTotalCitys, cityRepository.count());
        Assertions.assertTrue(cityRepository.existsById(city.getId()));
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        eventRepository.deleteAll();
        cityRepository.deleteById(existingId);

        Assertions.assertFalse(cityRepository.existsById(existingId));
    }

    @Test
    public void deleteShouldNotDoAnythingWhenIdDoesNotExists() {
        cityRepository.deleteById(notExistingId);
        Assertions.assertEquals(countTotalCitys, cityRepository.count());
    }

    @Test
    public void findByIdShouldReturnAOptionalCityNotNullWhenIdExists() {
        Optional<City> city= cityRepository.findById(existingId);
        Assertions.assertTrue(city.isPresent());
    }

    @Test
    public void findByIdShouldReturnAOptionalCityEmptyWhenIdDoesNotExists() {
        Optional<City> city = cityRepository.findById(notExistingId);
        Assertions.assertTrue(city.isEmpty());
    }

}
