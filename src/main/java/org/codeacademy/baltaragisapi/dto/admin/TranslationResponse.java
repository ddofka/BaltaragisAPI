package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import java.time.OffsetDateTime;

/**
 * Response DTO for translation operations.
 */
@Value
@Builder
@Schema(name = "TranslationResponse", description = "Translation entity response")
public class TranslationResponse {
    
    @Schema(example = "1", description = "Translation ID")
    Long id;
    
    @Schema(example = "common.add_to_cart", description = "Translation key")
    String key;
    
    @Schema(example = "en-US", description = "Locale code")
    String locale;
    
    @Schema(example = "Add to Cart", description = "Translated text value")
    String value;
    
    @Schema(description = "Last update timestamp")
    OffsetDateTime updatedAt;
}
