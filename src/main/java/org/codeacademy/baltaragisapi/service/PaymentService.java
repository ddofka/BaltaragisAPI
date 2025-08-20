package org.codeacademy.baltaragisapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeacademy.baltaragisapi.config.PaymentProperties;
import org.codeacademy.baltaragisapi.dto.CreateCheckoutSessionRequest;
import org.codeacademy.baltaragisapi.dto.CheckoutSessionResponse;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.exception.FeatureDisabledException;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.exception.InsufficientStockException;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProperties paymentProperties;
    private final ProductRepository productRepository;

    /**
     * Create a checkout session for payment
     * 
     * @param request the checkout session request
     * @return checkout session response with payment URL
     * @throws FeatureDisabledException if payments are disabled
     * @throws NotFoundException if product not found
     * @throws InsufficientStockException if insufficient stock
     */
    public CheckoutSessionResponse createCheckoutSession(CreateCheckoutSessionRequest request) {
        // Check if payments are enabled
        if (!paymentProperties.isEnabled()) {
            log.warn("Payment checkout session requested but payments are disabled");
            throw new FeatureDisabledException("Payments are currently disabled");
        }

        // Validate product exists
        Product product = productRepository.findBySlug(request.getProductSlug())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // Validate stock availability
        int requested = request.getQty();
        int available = product.getQuantity() != null ? product.getQuantity() : 0;
        if (requested > available) {
            throw new InsufficientStockException("Insufficient stock");
        }

        // Generate a deterministic stub session ID for development
        String sessionId = generateStubSessionId(request.getProductSlug(), request.getEmail());
        
        // Create stub checkout URL (in production, this would be a real Stripe checkout URL)
        String checkoutUrl = createStubCheckoutUrl(sessionId, request.getSuccessUrl(), request.getCancelUrl());
        
        log.info("Created checkout session {} for product {} (qty: {}) - email: {}", 
                sessionId, request.getProductSlug(), request.getQty(), request.getEmail());

        return new CheckoutSessionResponse(checkoutUrl, sessionId, "PENDING");
    }

    /**
     * Generate a deterministic stub session ID for development
     * In production, this would be replaced with Stripe session ID
     */
    private String generateStubSessionId(String productSlug, String email) {
        // Create a deterministic hash-based ID for consistent testing
        String input = productSlug + ":" + email + ":" + System.currentTimeMillis() / 60000; // Minute-based
        int hash = input.hashCode();
        return "cs_stub_" + Math.abs(hash) + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Create a stub checkout URL for development
     * In production, this would be a real Stripe checkout URL
     */
    private String createStubCheckoutUrl(String sessionId, String successUrl, String cancelUrl) {
        String baseUrl = "http://localhost:8080";
        
        // Create a stub checkout page that simulates payment flow
        return String.format("%s/api/v1/payments/stub-checkout?sessionId=%s&successUrl=%s&cancelUrl=%s",
                baseUrl, sessionId, 
                successUrl != null ? URLEncoder.encode(successUrl, StandardCharsets.UTF_8) : URLEncoder.encode(baseUrl + "/success", StandardCharsets.UTF_8),
                cancelUrl != null ? URLEncoder.encode(cancelUrl, StandardCharsets.UTF_8) : URLEncoder.encode(baseUrl + "/cancel", StandardCharsets.UTF_8));
    }
}
