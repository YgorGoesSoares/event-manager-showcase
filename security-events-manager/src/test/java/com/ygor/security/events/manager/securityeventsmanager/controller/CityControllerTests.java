package com.ygor.security.events.manager.securityeventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygor.security.events.manager.securityeventsmanager.dtos.CityDTO;
import com.ygor.security.events.manager.securityeventsmanager.factory.CityFactory;
import com.ygor.security.events.manager.securityeventsmanager.repository.CityRepository;
import com.ygor.security.events.manager.securityeventsmanager.repository.UserRepository;
import com.ygor.security.events.manager.securityeventsmanager.security.services.TokenService;
import com.ygor.security.events.manager.securityeventsmanager.services.CityService;
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

@WebMvcTest(CityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private CityService cityService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private CityDTO cityDTO;
    private PageImpl<CityDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        cityDTO = new CityDTO(CityFactory.createCity());
        page = new PageImpl<>(List.of(cityDTO));

        Mockito.when(cityService.findAllPaged(Mockito.any())).thenReturn(page);
        Mockito.when(cityService.findById(existingId)).thenReturn(cityDTO);
        Mockito.when(cityService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(cityService.insertNewCity(Mockito.any())).thenReturn(cityDTO);

        Mockito.when(cityService.updateCity(Mockito.eq(existingId), Mockito.any())).thenReturn(cityDTO);
        Mockito.when(cityService.updateCity(Mockito.eq(nonExistingId), Mockito.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(cityService).deleteCity(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(cityService).deleteCity(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(cityService).deleteCity(dependentId);
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/cities/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/cities/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void insertShouldReturnCityDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(cityDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/cities")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    public void updateShouldReturnCityDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(cityDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/cities/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(cityDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/cities/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/cities")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnCityWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/cities/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/cities/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
