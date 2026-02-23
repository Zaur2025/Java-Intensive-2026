package io.github.zaur2025.userservicetests.dao;

import io.github.zaur2025.userservicetests.entity.User;
import io.github.zaur2025.userservicetests.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        // Настраиваем Hibernate на тестовую БД в контейнере
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        // Инициализируем SessionFactory (перезагружаем конфигурацию)
        HibernateUtil.resetSessionFactory();
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        // Очищаем таблицу перед каждым тестом
        userDao.deleteAll();
    }

    @Test
    @Order(1)
    void save_ShouldPersistUserAndGenerateId() {
        // Arrange
        User user = new User("Иван Петров", "ivan@mail.ru", 25);

        // Act
        Long id = userDao.save(user);

        // Assert
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    @Order(2)
    void findById_ShouldReturnUser_WhenExists() {
        // Arrange
        User user = new User("Мария Иванова", "maria@mail.ru", 30);
        Long savedId = userDao.save(user);

        // Act
        Optional<User> found = userDao.findById(savedId);

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Мария Иванова", found.get().getName());
        assertEquals("maria@mail.ru", found.get().getEmail());
        assertEquals(30, found.get().getAge());
        assertNotNull(found.get().getCreatedAt());
    }

    @Test
    @Order(3)
    void findById_ShouldReturnEmpty_WhenNotExists() {
        // Act
        Optional<User> found = userDao.findById(999L);

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    @Order(4)
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        userDao.save(new User("User1", "user1@mail.ru", 20));
        userDao.save(new User("User2", "user2@mail.ru", 25));
        userDao.save(new User("User3", "user3@mail.ru", 30));

        // Act
        List<User> users = userDao.findAll();

        // Assert
        assertEquals(3, users.size());
    }

    @Test
    @Order(5)
    void findByEmail_ShouldReturnUser_WhenExists() {
        // Arrange
        userDao.save(new User("Петр Сидоров", "petr@mail.ru", 35));

        // Act
        Optional<User> found = userDao.findByEmail("petr@mail.ru");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Петр Сидоров", found.get().getName());
    }

    @Test
    @Order(6)
    void findByEmail_ShouldReturnEmpty_WhenNotExists() {
        // Act
        Optional<User> found = userDao.findByEmail("nonexistent@mail.ru");

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    @Order(7)
    void update_ShouldModifyExistingUser() {
        // Arrange
        User user = new User("Старое имя", "old@mail.ru", 40);
        Long id = userDao.save(user);

        // Act
        user.setName("Новое имя");
        user.setEmail("new@mail.ru");
        user.setAge(41);
        userDao.update(user);

        // Assert
        Optional<User> updated = userDao.findById(id);
        assertTrue(updated.isPresent());
        assertEquals("Новое имя", updated.get().getName());
        assertEquals("new@mail.ru", updated.get().getEmail());
        assertEquals(41, updated.get().getAge());
        // created_at не должен измениться
        assertEquals(user.getCreatedAt(), updated.get().getCreatedAt());
    }

    @Test
    @Order(8)
    void delete_ShouldRemoveUser_WhenExists() {
        // Arrange
        User user = new User("На удаление", "delete@mail.ru", 50);
        Long id = userDao.save(user);
        assertTrue(userDao.findById(id).isPresent());

        // Act
        boolean deleted = userDao.delete(id);

        // Assert
        assertTrue(deleted);
        assertTrue(userDao.findById(id).isEmpty());
    }

    @Test
    @Order(9)
    void delete_ShouldReturnFalse_WhenUserNotExists() {
        // Act
        boolean deleted = userDao.delete(999L);

        // Assert
        assertFalse(deleted);
    }

    @Test
    @Order(10)
    void deleteAll_ShouldRemoveAllUsers() {
        // Arrange
        userDao.save(new User("User1", "u1@mail.ru", 20));
        userDao.save(new User("User2", "u2@mail.ru", 25));
        assertEquals(2, userDao.findAll().size());

        // Act
        int deletedCount = userDao.deleteAll();

        // Assert
        assertEquals(2, deletedCount);
        assertTrue(userDao.findAll().isEmpty());
    }
}