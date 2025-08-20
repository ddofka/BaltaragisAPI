package org.codeacademy.baltaragisapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TranslationService translationService;

    /**
     * Send a simple text email
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param text email body text
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
            return true;
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            return false;
        }
    }

    /**
     * Send a waitlist notification email
     * 
     * @param to recipient email address
     * @param productName name of the product that's back in stock
     * @param productSlug slug of the product for the URL
     * @param locale locale for localization (en-US, lt-LT)
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendWaitlistNotification(String to, String productName, String productSlug, String locale) {
        try {
            String subject = getWaitlistSubject(locale);
            String text = getWaitlistEmailText(productName, productSlug, locale);
            
            return sendEmail(to, subject, text);
        } catch (Exception e) {
            log.error("Error preparing waitlist notification email for locale: {}", locale, e);
            // Fallback to default locale
            return sendWaitlistNotification(to, productName, productSlug, "en-US");
        }
    }

    private String getWaitlistSubject(String locale) {
        return translationService.getTranslation("email.waitlist.subject", locale);
    }

    private String getWaitlistEmailText(String productName, String productSlug, String locale) {
        String baseUrl = "http://localhost:8080"; // In production, this would come from configuration
        String productUrl = baseUrl + "/products/" + productSlug;
        
        String greeting = translationService.getTranslation("email.waitlist.greeting", locale);
        String productBackInStock = translationService.getTranslation("email.waitlist.product_back_in_stock", locale);
        String purchaseLink = translationService.getTranslation("email.waitlist.purchase_link", locale);
        String regards = translationService.getTranslation("email.waitlist.regards", locale);
        String team = translationService.getTranslation("email.waitlist.team", locale);
        
        // Format the message with placeholders
        String formattedProductBackInStock = MessageFormat.format(productBackInStock, productName);
        String formattedPurchaseLink = MessageFormat.format(purchaseLink, productUrl);
        
        return String.format(
            "%s\n\n" +
            "%s\n\n" +
            "%s\n\n" +
            "%s\n" +
            "%s",
            greeting,
            formattedProductBackInStock,
            formattedPurchaseLink,
            regards,
            team
        );
    }
}
