package io.github.zaur2025.userservicespringkafka.service;

import io.github.zaur2025.userservicespringkafka.dto.UserRequestDto;
import io.github.zaur2025.userservicespringkafka.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
}