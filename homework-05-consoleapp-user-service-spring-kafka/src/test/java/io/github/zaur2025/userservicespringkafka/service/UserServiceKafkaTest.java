package io.github.zaur2025.userservicespringkafka.service;

import io.github.zaur2025.userservicespringkafka.dto.UserRequestDto;
import io.github.zaur2025.userservicespringkafka.entity.User;
import io.github.zaur2025.userservicespringkafka.kafka.UserEvent;
import io.github.zaur2025.userservicespringkafka.kafka.UserEventProducer;
import io.github.zaur2025.userservicespringkafka.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceKafkaTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventProducer eventProducer;  // мок продюсера

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto userRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequestDto();
        userRequest.setName("Тест");
        userRequest.setEmail("test@mail.ru");
        userRequest.setAge(25);

        savedUser = new User(1L, "Тест", "test@mail.ru", 25, LocalDateTime.now());
    }

    @Test
    void createUser_ShouldSendKafkaEvent() {
        // given
        when(userRepository.existsByEmail("test@mail.ru")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        userService.createUser(userRequest);

        // then - проверяем что продюсер вызван ровно 1 раз
        verify(eventProducer, times(1)).sendUserEvent(any(UserEvent.class));
    }

    @Test
    void deleteUser_ShouldSendKafkaEvent() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        doNothing().when(userRepository).deleteById(1L);

        // when
        userService.deleteUser(1L);

        // then - проверяем что продюсер вызван ровно 1 раз
        verify(eventProducer, times(1)).sendUserEvent(any(UserEvent.class));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldNotSendKafkaEvent() {
        // given
        when(userRepository.existsByEmail("test@mail.ru")).thenReturn(true);

        // when & then
        assertThrows(RuntimeException.class, () -> userService.createUser(userRequest));

        // проверяем что продюсер НЕ вызывался
        verify(eventProducer, never()).sendUserEvent(any());
    }

    @Test
    void deleteUser_WithNonExistentId_ShouldNotSendKafkaEvent() {
        // given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> userService.deleteUser(99L));

        // проверяем что продюсер НЕ вызывался
        verify(eventProducer, never()).sendUserEvent(any());
    }
}