package com.ygor.security.events.manager.securityeventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygor.security.events.manager.securityeventsmanager.dtos.EventDTO;
import com.ygor.security.events.manager.securityeventsmanager.factory.EventFactory;
import com.ygor.security.events.manager.securityeventsmanager.repository.EventRepository;
import com.ygor.security.events.manager.securityeventsmanager.repository.UserRepository;
import com.ygor.security.events.manager.securityeventsmanager.security.services.TokenService;
import com.ygor.security.events.manager.securityeventsmanager.services.EventService;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.DatabaseException;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private EventDTO eventDTO;
    private PageImpl<EventDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        eventDTO = new EventDTO(EventFactory.createEvent());
        page = new PageImpl<>(List.of(eventDTO));

        Mockito.when(eventService.findAllPaged(Mockito.any())).thenReturn(page);
        Mockito.when(eventService.findById(existingId)).thenReturn(eventDTO);
        Mockito.when(eventService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(eventService.insertNewEvent(Mockito.any())).thenReturn(eventDTO);

        Mockito.when(eventService.updateEvent(Mockito.eq(existingId), Mockito.any())).thenReturn(eventDTO);
        Mockito.when(eventService.updateEvent(Mockito.eq(nonExistingId), Mockito.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(eventService).deleteEvent(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(eventService).deleteEvent(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(eventService).deleteEvent(dependentId);
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void insertShouldReturnEventDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(eventDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.date").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.url").exists());
    }

    @Test
    public void updateShouldReturnEventDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(eventDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/events/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.date").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.url").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(eventDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/events/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/events")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnEventWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/events/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.date").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.url").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/events/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
