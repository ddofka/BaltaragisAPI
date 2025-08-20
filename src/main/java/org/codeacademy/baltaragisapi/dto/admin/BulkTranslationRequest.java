package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Request DTO for bulk translation operations.
 */
@Value
@Schema(name = "BulkTranslationRequest", description = "Request for bulk translation operations")
public class BulkTranslationRequest {
    
    @NotNull(message = "Translations map is required")
    @NotEmpty(message = "Translations map cannot be empty")
    @Schema(example = "{\"en-US\": {\"common.add_to_cart\": \"Add to Cart\"}, \"lt-LT\": {\"common.add_to_cart\": \"Pridėti į krepšelį\"}}", 
            required = true, 
            description = "Map of locale to key-value translation pairs")
    Map<String, Map<String, String>> translations;
}
