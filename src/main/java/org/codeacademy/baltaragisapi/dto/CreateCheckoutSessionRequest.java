package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Value
@Schema(name = "CreateCheckoutSessionRequest", description = "Request to create a checkout session")
public class CreateCheckoutSessionRequest {
    
    @NotNull(message = "Product slug is required")
    @Schema(example = "sunset-print", required = true, description = "Product slug to purchase")
    String productSlug;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Schema(example = "1", required = true, description = "Quantity to purchase")
    Integer qty;
    
    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(example = "user@example.com", required = true, description = "Customer email address")
    String email;
    
    @Schema(example = "http://localhost:3000/success", 
            description = "URL to redirect after successful payment")
    String successUrl;
    
    @Schema(example = "http://localhost:3000/cancel", 
            description = "URL to redirect after cancelled payment")
    String cancelUrl;
}
