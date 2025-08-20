package org.codeacademy.baltaragisapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TranslationService translationService;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, translationService);
    }

    @Test
    void testSendEmail_Success() {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test email body";

        // When
        boolean result = emailService.sendEmail(to, subject, text);

        // Then
        assertTrue(result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_Failure() {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test email body";
        
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        // When
        boolean result = emailService.sendEmail(to, subject, text);

        // Then
        assertFalse(result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendWaitlistNotification_Success() {
        // Given
        String to = "test@example.com";
        String productName = "Test Product";
        String productSlug = "test-product";
        String locale = "en-US";
        
        when(translationService.getTranslation("email.waitlist.subject", locale)).thenReturn("Product back in stock!");
        when(translationService.getTranslation("email.waitlist.greeting", locale)).thenReturn("Hello!");
        when(translationService.getTranslation("email.waitlist.product_back_in_stock", locale)).thenReturn("The product \"{0}\" is back in stock!");
        when(translationService.getTranslation("email.waitlist.purchase_link", locale)).thenReturn("You can purchase it here: {0}");
        when(translationService.getTranslation("email.waitlist.regards", locale)).thenReturn("Best regards,");
        when(translationService.getTranslation("email.waitlist.team", locale)).thenReturn("Baltaragis team");

        // When
        boolean result = emailService.sendWaitlistNotification(to, productName, productSlug, locale);

        // Then
        assertTrue(result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendWaitlistNotification_TranslationError_FallbackToEnglish() {
        // Given
        String to = "test@example.com";
        String productName = "Test Product";
        String productSlug = "test-product";
        String locale = "lt-LT";
        
        // First call fails, second call (fallback) succeeds
        when(translationService.getTranslation(anyString(), eq("lt-LT")))
            .thenThrow(new RuntimeException("Translation error"));
        when(translationService.getTranslation("email.waitlist.subject", "en-US")).thenReturn("Product back in stock!");
        when(translationService.getTranslation("email.waitlist.greeting", "en-US")).thenReturn("Hello!");
        when(translationService.getTranslation("email.waitlist.product_back_in_stock", "en-US")).thenReturn("The product \"{0}\" is back in stock!");
        when(translationService.getTranslation("email.waitlist.purchase_link", "en-US")).thenReturn("You can purchase it here: {0}");
        when(translationService.getTranslation("email.waitlist.regards", "en-US")).thenReturn("Best regards,");
        when(translationService.getTranslation("email.waitlist.team", "en-US")).thenReturn("Baltaragis team");

        // When
        boolean result = emailService.sendWaitlistNotification(to, productName, productSlug, locale);

        // Then
        assertTrue(result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
