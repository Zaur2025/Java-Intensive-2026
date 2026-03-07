package io.github.zaur2025.notificationservice.kafka;

import io.github.zaur2025.notificationservice.dto.UserEvent;
import io.github.zaur2025.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "${app.kafka.topic.user-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUserEvent(UserEvent event) {
        log.info("📩 Получено событие из Kafka: {} для пользователя {}",
                event.getEventType(), event.getEmail());

        switch (event.getEventType()) {
            case "CREATED":
                emailService.sendCreationNotification(event.getEmail());
                break;
            case "DELETED":
                emailService.sendDeletionNotification(event.getEmail());
                break;
            default:
                log.warn("Неизвестный тип события: {}", event.getEventType());
        }
    }
}