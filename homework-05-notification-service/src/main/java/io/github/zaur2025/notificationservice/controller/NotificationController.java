package io.github.zaur2025.notificationservice.controller;

import io.github.zaur2025.notificationservice.service.EmailService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Log4j2
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send-creation")
    public ResponseEntity<String> sendCreationNotification(
            @RequestParam @Email @NotBlank String email) {
        log.info("📨 Отправка уведомления о создании на {}", email);
        emailService.sendCreationNotification(email);
        return ResponseEntity.ok("Уведомление о создании отправлено на " + email);
    }

    @PostMapping("/send-deletion")
    public ResponseEntity<String> sendDeletionNotification(
            @RequestParam @Email @NotBlank String email) {
        log.info("📨 Отправка уведомления об удалении на {}", email);
        emailService.sendDeletionNotification(email);
        return ResponseEntity.ok("Уведомление об удалении отправлено на " + email);
    }
}