package com.ygor.security.events.manager.securityeventsmanager.controller;

import com.ygor.security.events.manager.securityeventsmanager.dtos.EventDTO;
import com.ygor.security.events.manager.securityeventsmanager.repository.EventRepository;
import com.ygor.security.events.manager.securityeventsmanager.services.EventService;
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
@RequestMapping(value = "/events")
@SecurityRequirement(name = "bearer-key")
public class EventController {

    @Autowired
    private EventService eventService;
    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAllPaged(Pageable pageable) {
        Page<EventDTO> list = eventService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EventDTO> findById(@PathVariable Long id) {
        EventDTO event = eventService.findById(id);
        return ResponseEntity.ok().body(event);
    }

    @PostMapping
    public ResponseEntity<EventDTO> insertNewEvent(@RequestBody EventDTO eventDTO) {
        eventDTO = eventService.insertNewEvent(eventDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eventDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(eventDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        eventDTO = eventService.updateEvent(id, eventDTO);
        return ResponseEntity.ok().body(eventDTO);
    }
}
