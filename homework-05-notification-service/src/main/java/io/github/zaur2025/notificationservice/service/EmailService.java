package io.github.zaur2025.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public void sendCreationNotification(String toEmail) {
        String subject = "Аккаунт создан";
        String text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        sendEmail(toEmail, subject, text);
    }

    public void sendDeletionNotification(String toEmail) {
        String subject = "Аккаунт удалён";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";
        sendEmail(toEmail, subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("✅ Email sent to {}", to);
        } catch (Exception e) {
            log.error("❌ Ошибка при отправке email на {}: {}", to, e.getMessage());
        }
    }
}