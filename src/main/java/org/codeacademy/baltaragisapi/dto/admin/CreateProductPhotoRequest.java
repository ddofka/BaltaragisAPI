package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Value
@Schema(name = "CreateProductPhotoRequest", description = "Request to create a new product photo")
public class CreateProductPhotoRequest {
    
    @NotNull(message = "Product ID is required")
    @Schema(example = "1", required = true)
    Long productId;
    
    @NotBlank(message = "Photo URL is required")
    @Schema(example = "https://example.com/photos/sunset1.jpg", required = true)
    String url;
    
    @Schema(example = "Sunset Print main view")
    String alt;
    
    @Schema(example = "1", defaultValue = "0")
    Integer sortOrder = 0;
    
    @Positive(message = "Width must be positive")
    @Schema(example = "1200")
    Integer width;
    
    @Positive(message = "Height must be positive")
    @Schema(example = "800")
    Integer height;
}
