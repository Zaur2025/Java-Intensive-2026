package io.github.zaur2025.userservicespring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zaur2025.userservicespring.dto.UserRequestDto;
import io.github.zaur2025.userservicespring.dto.UserResponseDto;
import io.github.zaur2025.userservicespring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDto testUserResponse;
    private UserRequestDto testUserRequest;

    @BeforeEach
    void setUp() {
        testUserResponse = new UserResponseDto(
                1L, "Тестовый", "test@mail.ru", 25, LocalDateTime.now()
        );

        testUserRequest = new UserRequestDto();
        testUserRequest.setName("Тестовый");
        testUserRequest.setEmail("test@mail.ru");
        testUserRequest.setAge(25);
    }

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(testUserResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Тестовый"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"));
    }

    @Test
    void createUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        UserRequestDto invalidRequest = new UserRequestDto();
        invalidRequest.setName("");  // пустое имя
        invalidRequest.setEmail("invalid-email");  // неверный email
        invalidRequest.setAge(-5);  // отрицательный возраст

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUserResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Тестовый"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"));
    }

    @Test
    void getUserById_NotFound_ShouldReturnBadRequest() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new RuntimeException("Пользователь не найден"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        List<UserResponseDto> users = Arrays.asList(
                testUserResponse,
                new UserResponseDto(2L, "Второй", "second@mail.ru", 30, LocalDateTime.now())
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Тестовый"))
                .andExpect(jsonPath("$[1].name").value("Второй"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserRequestDto updateRequest = new UserRequestDto();
        updateRequest.setName("Обновленный");
        updateRequest.setEmail("updated@mail.ru");
        updateRequest.setAge(30);

        UserResponseDto updatedResponse = new UserResponseDto(
                1L, "Обновленный", "updated@mail.ru", 30, LocalDateTime.now()
        );

        when(userService.updateUser(eq(1L), any(UserRequestDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Обновленный"))
                .andExpect(jsonPath("$.email").value("updated@mail.ru"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_NotFound_ShouldReturnBadRequest() throws Exception {
        doThrow(new RuntimeException("Пользователь не найден")).when(userService).deleteUser(99L);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isBadRequest());
    }
}