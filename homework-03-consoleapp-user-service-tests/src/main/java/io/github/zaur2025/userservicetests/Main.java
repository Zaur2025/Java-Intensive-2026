package io.github.zaur2025.userservicetests;

import io.github.zaur2025.userservicetests.dao.UserDao;
import io.github.zaur2025.userservicetests.entity.User;
import io.github.zaur2025.userservicetests.util.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final UserDao userDao = new UserDao();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== User Service Console Application ===");
        System.out.println("Подключение к базе данных...");

        // Тестовое подключение к БД
        try {
            HibernateUtil.getSessionFactory().openSession();
            System.out.println("✓ Подключение к БД успешно");
        } catch (Exception e) {
            System.err.println("✗ Ошибка подключения к БД: " + e.getMessage());
            System.exit(1);
        }

        showMenu();
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\n===== ГЛАВНОЕ МЕНЮ =====");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Найти пользователя по ID");
            System.out.println("3. Найти пользователя по email");
            System.out.println("4. Показать всех пользователей");
            System.out.println("5. Обновить пользователя");
            System.out.println("6. Удалить пользователя");
            System.out.println("7. Удалить всех пользователей");
            System.out.println("8. Выход");
            System.out.print("Выберите действие (1-8): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> findUserById();
                    case 3 -> findUserByEmail();
                    case 4 -> showAllUsers();
                    case 5 -> updateUser();
                    case 6 -> deleteUser();
                    case 7 -> deleteAllUsers();
                    case 8 -> {
                        System.out.println("Завершение работы...");
                        HibernateUtil.shutdown();
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите число от 1 до 8.");
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static void createUser() {
        System.out.println("\n--- Создание нового пользователя ---");

        System.out.print("Введите имя: ");
        String name = scanner.nextLine().trim();

        System.out.print("Введите email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Введите возраст: ");
        Integer age = null;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Возраст не указан или неверный формат. Установлено: null");
        }

        User user = new User(name, email, age);
        Long userId = userDao.save(user);

        System.out.println("✓ Пользователь создан с ID: " + userId);
    }

    private static void findUserById() {
        System.out.println("\n--- Поиск пользователя по ID ---");
        System.out.print("Введите ID пользователя: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Optional<User> userOpt = userDao.findById(id);

            if (userOpt.isPresent()) {
                System.out.println("✓ Найден пользователь:");
                printUser(userOpt.get());
            } else {
                System.out.println("✗ Пользователь с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID. Должно быть число.");
        }
    }

    private static void findUserByEmail() {
        System.out.println("\n--- Поиск пользователя по email ---");
        System.out.print("Введите email: ");

        String email = scanner.nextLine().trim();
        Optional<User> userOpt = userDao.findByEmail(email);

        if (userOpt.isPresent()) {
            System.out.println("✓ Найден пользователь:");
            printUser(userOpt.get());
        } else {
            System.out.println("✗ Пользователь с email " + email + " не найден");
        }
    }

    private static void showAllUsers() {
        System.out.println("\n--- Все пользователи ---");

        List<User> users = userDao.findAll();

        if (users.isEmpty()) {
            System.out.println("В базе данных нет пользователей.");
        } else {
            System.out.println("Найдено пользователей: " + users.size());
            for (int i = 0; i < users.size(); i++) {
                System.out.println("\n[" + (i + 1) + "]");
                printUser(users.get(i));
            }
        }
    }

    private static void updateUser() {
        System.out.println("\n--- Обновление пользователя ---");
        System.out.print("Введите ID пользователя для обновления: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Optional<User> userOpt = userDao.findById(id);

            if (userOpt.isEmpty()) {
                System.out.println("✗ Пользователь с ID " + id + " не найден");
                return;
            }

            User user = userOpt.get();
            System.out.println("Текущие данные:");
            printUser(user);

            System.out.print("Новое имя (оставьте пустым чтобы не менять): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                user.setName(newName);
            }

            System.out.print("Новый email (оставьте пустым чтобы не менять): ");
            String newEmail = scanner.nextLine().trim();
            if (!newEmail.isEmpty()) {
                user.setEmail(newEmail);
            }

            System.out.print("Новый возраст (оставьте пустым чтобы не менять): ");
            String ageInput = scanner.nextLine().trim();
            if (!ageInput.isEmpty()) {
                try {
                    user.setAge(Integer.parseInt(ageInput));
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат возраста. Возраст не изменён.");
                }
            }

            userDao.update(user);
            System.out.println("✓ Пользователь обновлён");
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID.");
        }
    }

    private static void deleteUser() {
        System.out.println("\n--- Удаление пользователя ---");
        System.out.print("Введите ID пользователя для удаления: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            boolean deleted = userDao.delete(id);

            if (deleted) {
                System.out.println("✓ Пользователь с ID " + id + " удалён");
            } else {
                System.out.println("✗ Пользователь с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID.");
        }
    }

    private static void deleteAllUsers() {
        System.out.println("\n--- Удаление всех пользователей ---");
        System.out.print("Вы уверены? Это действие нельзя отменить! (yes/no): ");

        String confirmation = scanner.nextLine().trim().toLowerCase();
        if ("yes".equals(confirmation) || "y".equals(confirmation)) {
            int count = userDao.deleteAll();
            System.out.println("✓ Удалено пользователей: " + count);
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    private static void printUser(User user) {
        System.out.println("ID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + (user.getAge() != null ? user.getAge() : "не указан"));
        System.out.println("Создан: " + user.getCreatedAt());
    }
}