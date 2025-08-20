package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.config.PaymentProperties;
import org.codeacademy.baltaragisapi.dto.CreateCheckoutSessionRequest;
import org.codeacademy.baltaragisapi.dto.CheckoutSessionResponse;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.exception.FeatureDisabledException;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.exception.InsufficientStockException;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentProperties paymentProperties;

    @Mock
    private ProductRepository productRepository;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentProperties, productRepository);
    }

    @Test
    void testCreateCheckoutSession_PaymentsEnabled_Success() {
        // Given
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest(
            "test-product", 2, "test@example.com", null, null
        );
        
        Product product = createProduct("test-product", 10, 5000);
        
        when(paymentProperties.isEnabled()).thenReturn(true);
        when(productRepository.findBySlug("test-product")).thenReturn(Optional.of(product));

        // When
        CheckoutSessionResponse response = paymentService.createCheckoutSession(request);

        // Then: Checkout session is created successfully
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getSessionId()).isNotNull();
        assertThat(response.getCheckoutUrl()).isNotNull();
        assertThat(response.getCheckoutUrl()).contains("/stub-checkout");
        assertThat(response.getCheckoutUrl()).contains(response.getSessionId());
        
        verify(paymentProperties).isEnabled();
        verify(productRepository).findBySlug("test-product");
    }

    @Test
    void testCreateCheckoutSession_PaymentsDisabled_ThrowsException() {
        // Given
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest(
            "test-product", 1, "test@example.com", null, null
        );
        
        when(paymentProperties.isEnabled()).thenReturn(false);

        // When & Then
        FeatureDisabledException exception = assertThrows(
            FeatureDisabledException.class,
            () -> paymentService.createCheckoutSession(request)
        );
        
        assertEquals("Payments are currently disabled", exception.getMessage());
        verify(paymentProperties).isEnabled();
        verify(productRepository, never()).findBySlug(anyString());
    }

    @Test
    void testCreateCheckoutSession_ProductNotFound_ThrowsException() {
        // Given
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest(
            "non-existent", 1, "test@example.com", null, null
        );
        
        when(paymentProperties.isEnabled()).thenReturn(true);
        when(productRepository.findBySlug("non-existent")).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> paymentService.createCheckoutSession(request)
        );
        
        assertEquals("Product not found", exception.getMessage());
        verify(paymentProperties).isEnabled();
        verify(productRepository).findBySlug("non-existent");
    }

    @Test
    void testCreateCheckoutSession_InsufficientStock_ThrowsException() {
        // Given
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest(
            "test-product", 5, "test@example.com", null, null
        );
        
        Product product = createProduct("test-product", 3, 5000);
        
        when(paymentProperties.isEnabled()).thenReturn(true);
        when(productRepository.findBySlug("test-product")).thenReturn(Optional.of(product));

        // When & Then
        InsufficientStockException exception = assertThrows(
            InsufficientStockException.class,
            () -> paymentService.createCheckoutSession(request)
        );
        
        assertEquals("Insufficient stock", exception.getMessage());
        verify(paymentProperties).isEnabled();
        verify(productRepository).findBySlug("test-product");
    }

    @Test
    void testCreateCheckoutSession_DeterministicSessionId() {
        // Given
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest(
            "test-product", 1, "test@example.com", null, null
        );
        
        Product product = createProduct("test-product", 10, 5000);
        
        when(paymentProperties.isEnabled()).thenReturn(true);
        when(productRepository.findBySlug("test-product")).thenReturn(Optional.of(product));

        // When
        CheckoutSessionResponse response1 = paymentService.createCheckoutSession(request);
        CheckoutSessionResponse response2 = paymentService.createCheckoutSession(request);

        // Then
        assertNotNull(response1.getSessionId());
        assertNotNull(response2.getSessionId());
        // Session IDs should be different due to timestamp component
        assertNotEquals(response1.getSessionId(), response2.getSessionId());
        // But should follow the same pattern
        assertTrue(response1.getSessionId().startsWith("cs_stub_"));
        assertTrue(response2.getSessionId().startsWith("cs_stub_"));
    }

    @Test
    void testCreateCheckoutSession_WithCustomUrls() {
        // Given
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest(
            "test-product", 1, "test@example.com", 
            "http://localhost:3000/success", 
            "http://localhost:3000/cancel"
        );
        
        Product product = createProduct("test-product", 10, 5000);
        
        when(paymentProperties.isEnabled()).thenReturn(true);
        when(productRepository.findBySlug("test-product")).thenReturn(Optional.of(product));

        // When
        CheckoutSessionResponse response = paymentService.createCheckoutSession(request);

        // Then
        assertNotNull(response);
        assertTrue(response.getCheckoutUrl().contains("successUrl=http%3A%2F%2Flocalhost%3A3000%2Fsuccess"));
        assertTrue(response.getCheckoutUrl().contains("cancelUrl=http%3A%2F%2Flocalhost%3A3000%2Fcancel"));
    }

    private Product createProduct(String slug, Integer quantity, Integer priceCents) {
        Product product = new Product();
        product.setSlug(slug);
        product.setQuantity(quantity);
        product.setPriceCents(priceCents);
        product.setCurrency("EUR");
        return product;
    }
}
