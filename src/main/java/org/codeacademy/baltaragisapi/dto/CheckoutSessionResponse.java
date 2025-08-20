package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class CheckoutSessionResponse {
    
    @Schema(example = "https://checkout.stripe.com/pay/cs_test_123", 
            description = "URL to redirect customer for payment")
    String checkoutUrl;
    
    @Schema(example = "cs_test_123", 
            description = "Checkout session identifier")
    String sessionId;
    
    @Schema(example = "PENDING", 
            description = "Current status of the checkout session")
    String status;
}
