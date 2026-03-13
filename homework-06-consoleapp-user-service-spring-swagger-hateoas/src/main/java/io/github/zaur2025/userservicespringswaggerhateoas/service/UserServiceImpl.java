package io.github.zaur2025.userservicespringswaggerhateoas.service;

import io.github.zaur2025.userservicespringswaggerhateoas.dto.UserRequestDto;
import io.github.zaur2025.userservicespringswaggerhateoas.entity.User;
import io.github.zaur2025.userservicespringswaggerhateoas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(UserRequestDto userRequestDto) {
        log.info("Создание нового пользователя: {}", userRequestDto.getEmail());

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

        return savedUser; // Возвращаем сущность, а не DTO
    }

    @Override
    public User getUserById(Long id) {
        log.info("Поиск пользователя по ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userRepository.findAll(); // Возвращаем список сущностей
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserRequestDto userRequestDto) {
        log.info("Обновление пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));

        if (!user.getEmail().equals(userRequestDto.getEmail()) &&
                userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RuntimeException("Пользователь с email " + userRequestDto.getEmail() + " уже существует");
        }

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setAge(userRequestDto.getAge());

        User updatedUser = userRepository.save(user);
        log.info("Пользователь обновлен: {}", id);

        return updatedUser; // Возвращаем сущность
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
}