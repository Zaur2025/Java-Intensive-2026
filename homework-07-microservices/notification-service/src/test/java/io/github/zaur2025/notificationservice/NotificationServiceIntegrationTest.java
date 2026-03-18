package io.github.zaur2025.notificationservice;

import io.github.zaur2025.notificationservice.dto.UserEvent;
import io.github.zaur2025.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, topics = {"user-events-topic"})
public class NotificationServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private EmailService emailService;

    @Test
    void testApiCreation() {
        // Просто проверяем API
        String url = "http://localhost:" + port + "/api/notifications/send-creation?email=test@mail.ru";
        restTemplate.postForEntity(url, null, String.class);

        verify(emailService).sendCreationNotification("test@mail.ru");
    }

    @Test
    void testApiDeletion() {
        // Просто проверяем API
        String url = "http://localhost:" + port + "/api/notifications/send-deletion?email=test@mail.ru";
        restTemplate.postForEntity(url, null, String.class);

        verify(emailService).sendDeletionNotification("test@mail.ru");
    }
}