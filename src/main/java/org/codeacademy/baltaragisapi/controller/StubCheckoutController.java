package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeacademy.baltaragisapi.config.PaymentProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Stub Checkout", description = "Development stub for payment simulation")
public class StubCheckoutController {

    private final PaymentProperties paymentProperties;

    @GetMapping("/stub-checkout")
    @Operation(summary = "Stub checkout page", 
               description = "Development stub page to simulate payment flow")
    public String stubCheckout(
            @Parameter(description = "Checkout session ID") @RequestParam String sessionId,
            @Parameter(description = "Success redirect URL") @RequestParam(required = false) String successUrl,
            @Parameter(description = "Cancel redirect URL") @RequestParam(required = false) String cancelUrl,
            Model model) {
        
        if (!paymentProperties.isEnabled()) {
            log.warn("Stub checkout accessed but payments are disabled");
            throw new RuntimeException("Payments are disabled");
        }

        log.info("Stub checkout page accessed for session: {}", sessionId);
        
        // Add data to the model for the view
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("successUrl", successUrl != null ? successUrl : "http://localhost:3000/success");
        model.addAttribute("cancelUrl", cancelUrl != null ? cancelUrl : "http://localhost:3000/cancel");
        
        // Return a simple HTML template name (you can create a proper template later)
        return "stub-checkout";
    }

    @PostMapping("/stub-checkout/simulate-success")
    @Operation(summary = "Simulate successful payment", 
               description = "Development endpoint to simulate successful payment")
    public ResponseEntity<Map<String, String>> simulateSuccessfulPayment(
            @RequestParam String sessionId) {
        
        if (!paymentProperties.isEnabled()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Payments are disabled"));
        }

        log.info("Simulating successful payment for session: {}", sessionId);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("sessionId", sessionId);
        response.put("message", "Payment simulation successful");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stub-checkout/simulate-cancel")
    @Operation(summary = "Simulate cancelled payment", 
               description = "Development endpoint to simulate cancelled payment")
    public ResponseEntity<Map<String, String>> simulateCancelledPayment(
            @RequestParam String sessionId) {
        
        if (!paymentProperties.isEnabled()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Payments are disabled"));
        }

        log.info("Simulating cancelled payment for session: {}", sessionId);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "cancelled");
        response.put("sessionId", sessionId);
        response.put("message", "Payment simulation cancelled");
        
        return ResponseEntity.ok(response);
    }
}
