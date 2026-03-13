package io.github.zaur2025.userservicespringswaggerhateoas.service;

import io.github.zaur2025.userservicespringswaggerhateoas.dto.UserRequestDto;
import io.github.zaur2025.userservicespringswaggerhateoas.entity.User; // Теперь импортируем User, а не DTO

import java.util.List;

public interface UserService {
    User createUser(UserRequestDto userRequestDto); // Возвращает User
    User getUserById(Long id);                      // Возвращает User
    List<User> getAllUsers();                        // Возвращает List<User>
    User updateUser(Long id, UserRequestDto userRequestDto); // Возвращает User
    void deleteUser(Long id);
}