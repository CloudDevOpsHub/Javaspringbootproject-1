package com.clouddevopshub.demo;

import com.clouddevopshub.demo.controller.HealthController;
import com.clouddevopshub.demo.controller.UserController;
import com.clouddevopshub.demo.model.User;
import com.clouddevopshub.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UserController.class, HealthController.class})
class DemoApplicationTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@clouddevopshub.com")
                .department("DevOps")
                .build();
    }
    
    @Test
    @DisplayName("Health endpoint should return UP status")
    void healthEndpointShouldReturnUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
    
    @Test
    @DisplayName("Root endpoint should return welcome message")
    void rootEndpointShouldReturnWelcome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
    
    @Test
    @DisplayName("Get all users should return list")
    void getAllUsersShouldReturnList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }
    
    @Test
    @DisplayName("Get user by ID should return user")
    void getUserByIdShouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@clouddevopshub.com"));
    }
    
    @Test
    @DisplayName("Get non-existent user should return 404")
    void getNonExistentUserShouldReturn404() throws Exception {
        when(userService.getUserById(999L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Create user should return created user")
    void createUserShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        
        String userJson = """
            {
                "name": "Test User",
                "email": "test@clouddevopshub.com",
                "department": "DevOps"
            }
            """;
        
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test User"));
    }
    
    @Test
    @DisplayName("Delete user should return no content")
    void deleteUserShouldReturnNoContent() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);
        
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
