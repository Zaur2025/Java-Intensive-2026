package io.github.zaur2025.userservicespring.service;

import io.github.zaur2025.userservicespring.dto.UserRequestDto;
import io.github.zaur2025.userservicespring.dto.UserResponseDto;
import io.github.zaur2025.userservicespring.entity.User;
import io.github.zaur2025.userservicespring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Создание нового пользователя: {}", userRequestDto.getEmail());

        // Проверка на существующий email
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RuntimeException("Пользователь с email " + userRequestDto.getEmail() + " уже существует");
        }

        User user = new User(
                userRequestDto.getName(),
                userRequestDto.getEmail(),
                userRequestDto.getAge()
        );

        User savedUser = userRepository.save(user);
        log.info("Пользователь создан с ID: {}", savedUser.getId());

        return mapToDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        log.info("Поиск пользователя по ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));

        return mapToDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.info("Получение всех пользователей");

        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        log.info("Обновление пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));

        // Проверка email, если он меняется
        if (!user.getEmail().equals(userRequestDto.getEmail()) &&
                userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RuntimeException("Пользователь с email " + userRequestDto.getEmail() + " уже существует");
        }

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setAge(userRequestDto.getAge());

        User updatedUser = userRepository.save(user);
        log.info("Пользователь обновлен: {}", id);

        return mapToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь с ID " + id + " не найден");
        }

        userRepository.deleteById(id);
        log.info("Пользователь удален: {}", id);
    }

    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}