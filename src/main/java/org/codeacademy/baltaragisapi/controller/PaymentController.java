package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.codeacademy.baltaragisapi.config.PaymentProperties;
import org.codeacademy.baltaragisapi.dto.CreateCheckoutSessionRequest;
import org.codeacademy.baltaragisapi.dto.CheckoutSessionResponse;
import org.codeacademy.baltaragisapi.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment and checkout endpoints")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentProperties paymentProperties;

    @PostMapping("/checkout-session")
    @Operation(summary = "Create checkout session", 
               description = "Create a checkout session for payment processing")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Checkout session created",
                    content = @Content(schema = @Schema(implementation = CheckoutSessionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Insufficient stock"),
        @ApiResponse(responseCode = "503", description = "Payments disabled")
    })
    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(
            @Valid @RequestBody CreateCheckoutSessionRequest request) {
        
        CheckoutSessionResponse response = paymentService.createCheckoutSession(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkout-session/status")
    @Operation(summary = "Check payment status", 
               description = "Check the current status of a checkout session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Session status retrieved"),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<String> getCheckoutSessionStatus(
            @RequestParam String sessionId) {
        
        // For now, return a stub status
        // In production, this would query Stripe for the actual session status
        return ResponseEntity.ok("PENDING");
    }
}
