package io.github.zaur2025.userservicetests.service;

import io.github.zaur2025.userservicetests.dao.UserDao;
import io.github.zaur2025.userservicetests.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email, Integer age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email");
        }

        // Проверка уникальности email
        if (userDao.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Пользователь с таким email уже существует");
        }

        User user = new User(name, email, age);
        Long id = userDao.save(user);
        user.setId(id);
        return user;
    }

    public Optional<User> getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некорректный ID");
        }
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("ID пользователя не указан");
        }
        // Дополнительные проверки...
        userDao.update(user);
    }

    public boolean deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некорректный ID");
        }
        return userDao.delete(id);
    }
}