package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.entity.StockWaitlist;
import org.codeacademy.baltaragisapi.repository.StockWaitlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitlistNotificationServiceTest {

    @Mock
    private StockWaitlistRepository waitlistRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private LocaleResolverService localeResolverService;

    private WaitlistNotificationService waitlistNotificationService;

    @BeforeEach
    void setUp() {
        waitlistNotificationService = new WaitlistNotificationService(
            waitlistRepository, emailService, localeResolverService
        );
    }

    @Test
    void testCheckAndSendWaitlistNotifications_QuantityIncreasedFromZero() {
        // Given
        Product product = createProduct("Test Product", "test-product", 5, true);
        Integer previousQuantity = 0;
        Boolean previousIsPublished = true;
        
        StockWaitlist waitlistEntry1 = createWaitlistEntry("user1@example.com", product);
        StockWaitlist waitlistEntry2 = createWaitlistEntry("user2@example.com", product);
        List<StockWaitlist> waitlistEntries = Arrays.asList(waitlistEntry1, waitlistEntry2);
        
        when(waitlistRepository.findByProductIdAndNotifiedAtIsNull(product.getId()))
            .thenReturn(waitlistEntries);
        when(emailService.sendWaitlistNotification(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(true);

        // When
        waitlistNotificationService.checkAndSendWaitlistNotifications(product, previousQuantity, previousIsPublished);

        // Then
        verify(waitlistRepository).findByProductIdAndNotifiedAtIsNull(product.getId());
        verify(emailService, times(2)).sendWaitlistNotification(anyString(), anyString(), anyString(), anyString());
        verify(waitlistRepository, times(2)).save(any(StockWaitlist.class));
        
        // Verify notifiedAt was set
        assertNotNull(waitlistEntry1.getNotifiedAt());
        assertNotNull(waitlistEntry2.getNotifiedAt());
    }

    @Test
    void testCheckAndSendWaitlistNotifications_PublishedWithStock() {
        // Given
        Product product = createProduct("Test Product", "test-product", 3, true);
        Integer previousQuantity = 0;
        Boolean previousIsPublished = false;
        
        StockWaitlist waitlistEntry = createWaitlistEntry("user@example.com", product);
        List<StockWaitlist> waitlistEntries = Collections.singletonList(waitlistEntry);
        
        when(waitlistRepository.findByProductIdAndNotifiedAtIsNull(product.getId()))
            .thenReturn(waitlistEntries);
        when(emailService.sendWaitlistNotification(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(true);

        // When
        waitlistNotificationService.checkAndSendWaitlistNotifications(product, previousQuantity, previousIsPublished);

        // Then
        verify(waitlistRepository).findByProductIdAndNotifiedAtIsNull(product.getId());
        verify(emailService).sendWaitlistNotification(anyString(), anyString(), anyString(), anyString());
        verify(waitlistRepository).save(waitlistEntry);
        assertNotNull(waitlistEntry.getNotifiedAt());
    }

    @Test
    void testCheckAndSendWaitlistNotifications_NoStockChange() {
        // Given
        Product product = createProduct("Test Product", "test-product", 5, true);
        Integer previousQuantity = 5;
        Boolean previousIsPublished = true;

        // When
        waitlistNotificationService.checkAndSendWaitlistNotifications(product, previousQuantity, previousIsPublished);

        // Then
        verify(waitlistRepository, never()).findByProductIdAndNotifiedAtIsNull(any());
        verify(emailService, never()).sendWaitlistNotification(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testCheckAndSendWaitlistNotifications_NoWaitlistEntries() {
        // Given
        Product product = createProduct("Test Product", "test-product", 5, true);
        Integer previousQuantity = 0;
        Boolean previousIsPublished = true;
        
        when(waitlistRepository.findByProductIdAndNotifiedAtIsNull(product.getId()))
            .thenReturn(Collections.emptyList());

        // When
        waitlistNotificationService.checkAndSendWaitlistNotifications(product, previousQuantity, previousIsPublished);

        // Then
        verify(waitlistRepository).findByProductIdAndNotifiedAtIsNull(product.getId());
        verify(emailService, never()).sendWaitlistNotification(anyString(), anyString(), anyString(), anyString());
        verify(waitlistRepository, never()).save(any(StockWaitlist.class));
    }

    @Test
    void testCheckAndSendWaitlistNotifications_EmailFailure() {
        // Given
        Product product = createProduct("Test Product", "test-product", 5, true);
        Integer previousQuantity = 0;
        Boolean previousIsPublished = true;
        
        StockWaitlist waitlistEntry = createWaitlistEntry("user@example.com", product);
        List<StockWaitlist> waitlistEntries = Collections.singletonList(waitlistEntry);
        
        when(waitlistRepository.findByProductIdAndNotifiedAtIsNull(product.getId()))
            .thenReturn(waitlistEntries);
        when(emailService.sendWaitlistNotification(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(false);

        // When
        waitlistNotificationService.checkAndSendWaitlistNotifications(product, previousQuantity, previousIsPublished);

        // Then
        verify(waitlistRepository).findByProductIdAndNotifiedAtIsNull(product.getId());
        verify(emailService).sendWaitlistNotification(anyString(), anyString(), anyString(), anyString());
        // Should not save if email failed
        verify(waitlistRepository, never()).save(any(StockWaitlist.class));
        assertNull(waitlistEntry.getNotifiedAt());
    }

    private Product createProduct(String name, String slug, Integer quantity, Boolean isPublished) {
        Product product = new Product();
        product.setId(1L);
        product.setName(name);
        product.setSlug(slug);
        product.setQuantity(quantity);
        product.setIsPublished(isPublished);
        product.setCreatedAt(OffsetDateTime.now());
        product.setUpdatedAt(OffsetDateTime.now());
        return product;
    }

    private StockWaitlist createWaitlistEntry(String email, Product product) {
        StockWaitlist entry = new StockWaitlist();
        entry.setId(1L);
        entry.setEmail(email);
        entry.setProduct(product);
        entry.setCreatedAt(OffsetDateTime.now());
        entry.setNotifiedAt(null);
        return entry;
    }
}
