package io.github.zaur2025.userservicespring.service;

import io.github.zaur2025.userservicespring.dto.UserRequestDto;
import io.github.zaur2025.userservicespring.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
}