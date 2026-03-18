package io.github.zaur2025.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendCreationNotification_ShouldCallMailSender() {
        // when
        emailService.sendCreationNotification("test@example.com");

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendDeletionNotification_ShouldCallMailSender() {
        // when
        emailService.sendDeletionNotification("test@example.com");

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}