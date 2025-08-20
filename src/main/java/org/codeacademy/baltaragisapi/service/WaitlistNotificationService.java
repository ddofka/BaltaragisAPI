package org.codeacademy.baltaragisapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.entity.StockWaitlist;
import org.codeacademy.baltaragisapi.repository.StockWaitlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitlistNotificationService {

    private final StockWaitlistRepository waitlistRepository;
    private final EmailService emailService;
    private final LocaleResolverService localeResolverService;

    /**
     * Check if a product should trigger waitlist notifications and send them
     * This method should be called when a product's quantity or published status changes
     * 
     * @param product the product that was updated
     * @param previousQuantity the previous quantity value
     * @param previousIsPublished the previous published status
     */
    @Transactional
    public void checkAndSendWaitlistNotifications(Product product, Integer previousQuantity, Boolean previousIsPublished) {
        // Check if we should send notifications
        if (!shouldSendNotifications(product, previousQuantity, previousIsPublished)) {
            return;
        }

        log.info("Product {} is back in stock, sending waitlist notifications", product.getSlug());
        
        // Get all waitlist entries for this product that haven't been notified yet
        List<StockWaitlist> waitlistEntries = waitlistRepository.findByProductIdAndNotifiedAtIsNull(product.getId());
        
        if (waitlistEntries.isEmpty()) {
            log.info("No waitlist entries found for product {}", product.getSlug());
            return;
        }

        log.info("Found {} waitlist entries to notify for product {}", waitlistEntries.size(), product.getSlug());

        // Send emails and mark as notified
        for (StockWaitlist entry : waitlistEntries) {
            boolean emailSent = sendWaitlistNotification(entry, product);
            
            if (emailSent) {
                // Mark as notified
                entry.setNotifiedAt(OffsetDateTime.now());
                waitlistRepository.save(entry);
                log.info("Waitlist notification sent and marked as notified for email: {}", entry.getEmail());
            } else {
                log.warn("Failed to send waitlist notification for email: {}", entry.getEmail());
            }
        }
    }

    /**
     * Determine if we should send waitlist notifications based on the changes
     */
    private boolean shouldSendNotifications(Product product, Integer previousQuantity, Boolean previousIsPublished) {
        // Case 1: Quantity increased from 0 to > 0
        boolean quantityIncreased = (previousQuantity != null && previousQuantity == 0 && product.getQuantity() > 0);
        
        // Case 2: Product was unpublished and is now published with stock
        boolean publishedWithStock = (previousIsPublished != null && !previousIsPublished && 
                                    product.getIsPublished() && product.getQuantity() > 0);
        
        return quantityIncreased || publishedWithStock;
    }

    /**
     * Send a waitlist notification email to a specific subscriber
     */
    private boolean sendWaitlistNotification(StockWaitlist waitlistEntry, Product product) {
        try {
            // For now, we'll use a default locale (en-US) since we don't have user preferences
            // In the future, this could be stored in the waitlist entry or user profile
            String locale = "en-US";
            
            return emailService.sendWaitlistNotification(
                waitlistEntry.getEmail(),
                product.getName(),
                product.getSlug(),
                locale
            );
        } catch (Exception e) {
            log.error("Error sending waitlist notification to {} for product {}", 
                     waitlistEntry.getEmail(), product.getSlug(), e);
            return false;
        }
    }
}
