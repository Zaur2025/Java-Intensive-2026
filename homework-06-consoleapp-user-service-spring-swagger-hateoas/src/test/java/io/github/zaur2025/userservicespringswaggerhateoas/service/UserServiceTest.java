package io.github.zaur2025.userservicespringswaggerhateoas.service;

import io.github.zaur2025.userservicespringswaggerhateoas.dto.UserRequestDto;
import io.github.zaur2025.userservicespringswaggerhateoas.entity.User;
import io.github.zaur2025.userservicespringswaggerhateoas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequestDto testUserRequest;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Тестовый", "test@mail.ru", 25, LocalDateTime.now());

        testUserRequest = new UserRequestDto();
        testUserRequest.setName("Тестовый");
        testUserRequest.setEmail("test@mail.ru");
        testUserRequest.setAge(25);
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail("test@mail.ru")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUserRequest);

        assertNotNull(result);
        assertEquals("Тестовый", result.getName());
        assertEquals("test@mail.ru", result.getEmail());
        assertEquals(25, result.getAge());

        verify(userRepository).existsByEmail("test@mail.ru");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_EmailExists_ThrowsException() {
        when(userRepository.existsByEmail("test@mail.ru")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.createUser(testUserRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Тестовый", result.getName());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
    }

    @Test
    void getAllUsers_ReturnsList() {
        List<User> users = Arrays.asList(
                testUser,
                new User(2L, "Второй", "second@mail.ru", 30, LocalDateTime.now())
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Тестовый", result.get(0).getName());
        assertEquals("Второй", result.get(1).getName());
    }

    @Test
    void updateUser_Success() {
        UserRequestDto updateRequest = new UserRequestDto();
        updateRequest.setName("Обновленный");
        updateRequest.setEmail("new@mail.ru");
        updateRequest.setAge(30);

        User existingUser = new User(1L, "Старое имя", "old@mail.ru", 20, LocalDateTime.now());
        User updatedUser = new User(1L, "Обновленный", "new@mail.ru", 30, LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("new@mail.ru")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Обновленный", result.getName());
        assertEquals("new@mail.ru", result.getEmail());
        assertEquals(30, result.getAge());
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(99L));

        verify(userRepository, never()).deleteById(any());
    }
}