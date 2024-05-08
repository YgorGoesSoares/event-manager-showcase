package com.ygor.security.events.manager.securityeventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygor.security.events.manager.securityeventsmanager.dtos.UserDTO;
import com.ygor.security.events.manager.securityeventsmanager.factory.UserFactory;
import com.ygor.security.events.manager.securityeventsmanager.repository.UserRepository;
import com.ygor.security.events.manager.securityeventsmanager.security.services.TokenService;
import com.ygor.security.events.manager.securityeventsmanager.services.UserService;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.DatabaseException;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private UserDTO userDTO;
    private PageImpl<UserDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        userDTO = UserFactory.createUserDTORoleClient();
        page = new PageImpl<>(List.of(userDTO));

        Mockito.when(userService.findAllPaged(Mockito.any())).thenReturn(page);
        Mockito.when(userService.findById(existingId)).thenReturn(userDTO);
        Mockito.when(userService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(userService.insertNewUser(Mockito.any())).thenReturn(userDTO);

        Mockito.when(userService.updateUser(Mockito.eq(existingId), Mockito.any())).thenReturn(userDTO);
        Mockito.when(userService.updateUser(Mockito.eq(nonExistingId), Mockito.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(userService).deleteUser(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(userService).deleteUser(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(userService).deleteUser(dependentId);
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void insertShouldReturnUserDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.roles").exists());
    }

    @Test
    public void updateShouldReturnUserDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.roles").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnUserWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.roles").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
