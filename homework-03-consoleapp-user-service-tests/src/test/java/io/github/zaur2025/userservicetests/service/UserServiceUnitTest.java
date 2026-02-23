package io.github.zaur2025.userservicetests.service;

import io.github.zaur2025.userservicetests.dao.UserDao;
import io.github.zaur2025.userservicetests.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Тестовый", "test@mail.ru", 25);
        testUser.setId(1L);
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        // Arrange
        when(userDao.findByEmail("test@mail.ru")).thenReturn(Optional.empty());
        when(userDao.save(any(User.class))).thenReturn(1L);

        // Act
        User result = userService.createUser("Тестовый", "test@mail.ru", 25);

        // Assert
        assertNotNull(result);
        assertEquals("Тестовый", result.getName());
        assertEquals("test@mail.ru", result.getEmail());
        assertEquals(25, result.getAge());
        assertEquals(1L, result.getId());

        verify(userDao).findByEmail("test@mail.ru");
        verify(userDao).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrow_WhenEmailExists() {
        // Arrange
        when(userDao.findByEmail("existing@mail.ru")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            userService.createUser("Имя", "existing@mail.ru", 30);
        });

        verify(userDao, never()).save(any());
    }

    @Test
    void createUser_ShouldThrow_WhenNameIsEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("", "test@mail.ru", 25);
        });

        verifyNoInteractions(userDao);
    }

    @Test
    void createUser_ShouldThrow_WhenEmailInvalid() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("Имя", "not-an-email", 25);
        });

        verifyNoInteractions(userDao);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        // Arrange
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userDao).findById(1L);
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(99L);

        // Assert
        assertTrue(result.isEmpty());
        verify(userDao).findById(99L);
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        // Arrange
        List<User> users = Arrays.asList(
                testUser,
                new User("Второй", "second@mail.ru", 30)
        );
        when(userDao.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        verify(userDao).findAll();
    }

    @Test
    void updateUser_ShouldCallDao() {
        // Arrange
        doNothing().when(userDao).update(testUser);

        // Act
        userService.updateUser(testUser);

        // Assert
        verify(userDao).update(testUser);
    }

    @Test
    void updateUser_ShouldThrow_WhenIdNull() {
        // Arrange
        User userWithNoId = new User("Без ID", "noid@mail.ru", 20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(userWithNoId);
        });

        verify(userDao, never()).update(any());
    }

    @Test
    void deleteUser_ShouldReturnTrue_WhenDeleted() {
        // Arrange
        when(userDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = userService.deleteUser(1L);

        // Assert
        assertTrue(result);
        verify(userDao).delete(1L);
    }

    @Test
    void deleteUser_ShouldReturnFalse_WhenNotExists() {
        // Arrange
        when(userDao.delete(99L)).thenReturn(false);

        // Act
        boolean result = userService.deleteUser(99L);

        // Assert
        assertFalse(result);
        verify(userDao).delete(99L);
    }
}