package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.Positive;

@Value
@Schema(name = "UpdateProductPhotoRequest", description = "Request to update an existing product photo")
public class UpdateProductPhotoRequest {
    
    @Schema(example = "https://example.com/photos/sunset1.jpg")
    String url;
    
    @Schema(example = "Sunset Print main view")
    String alt;
    
    @Schema(example = "1")
    Integer sortOrder;
    
    @Positive(message = "Width must be positive")
    @Schema(example = "1200")
    Integer width;
    
    @Positive(message = "Height must be positive")
    @Schema(example = "800")
    Integer height;
}
