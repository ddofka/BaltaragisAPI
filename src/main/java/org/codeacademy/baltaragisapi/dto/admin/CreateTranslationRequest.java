package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for creating or updating a translation.
 */
@Value
@Schema(name = "CreateTranslationRequest", description = "Request to create or update a translation")
public class CreateTranslationRequest {
    
    @NotBlank(message = "Translation key is required")
    @Schema(example = "common.add_to_cart", required = true, description = "Translation key identifier")
    String key;
    
    @NotBlank(message = "Locale is required")
    @Pattern(regexp = "^(en-US|lt-LT)$", message = "Locale must be either 'en-US' or 'lt-LT'")
    @Schema(example = "en-US", required = true, description = "Locale code (en-US or lt-LT)")
    String locale;
    
    @NotBlank(message = "Translation value is required")
    @Schema(example = "Add to Cart", required = true, description = "Translated text value")
    String value;
}
