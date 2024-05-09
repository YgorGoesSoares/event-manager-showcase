package com.ygor.security.events.manager.securityeventsmanager.controller;

import com.ygor.security.events.manager.securityeventsmanager.dtos.CityDTO;
import com.ygor.security.events.manager.securityeventsmanager.services.CityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/cities")
@SecurityRequirement(name = "bearer-key")
public class CityController {

    @Autowired
    CityService cityService;
    @GetMapping
    public ResponseEntity<Page<CityDTO>> findAllPaged(Pageable pageable) {
        Page<CityDTO> list = cityService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CityDTO> findById(@PathVariable Long id) {
        CityDTO city = cityService.findById(id);
        return ResponseEntity.ok().body(city);
    }

    @PostMapping
    public ResponseEntity<CityDTO> insertNewCity(@RequestBody CityDTO cityDTO) {
        cityDTO = cityService.insertNewCity(cityDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cityDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(cityDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id, @Valid @RequestBody CityDTO cityDTO) {
        cityDTO = cityService.updateCity(id, cityDTO);
        return ResponseEntity.ok().body(cityDTO);
    }
}
