// Продюсер
package io.github.zaur2025.userservicespringkafka.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.topic.user-events}")
    private String topic;

    public void sendUserEvent(UserEvent event) {
        log.info("Отправка события {} в Kafka для пользователя {}",
                event.getEventType(), event.getEmail());
        kafkaTemplate.send(topic, event.getEmail(), event);
    }
}