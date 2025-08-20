package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

@Value
@Schema(name = "UpdateProductRequest", description = "Request to update an existing product")
public class UpdateProductRequest {
    
    @Size(max = 255, message = "Product name must be 255 characters or less")
    @Schema(example = "Sunset Print")
    String name;
    
    @Size(max = 255, message = "Short description must be 255 characters or less")
    @Schema(example = "A3 giclée print")
    String shortDesc;
    
    @Schema(example = "High-quality giclée print of a sunset.")
    String longDesc;
    
    @Positive(message = "Price must be positive")
    @Schema(example = "4500", description = "Price in cents (e.g., 4500 = €45.00)")
    Integer priceCents;
    
    @Size(max = 3, message = "Currency code must be 3 characters or less")
    @Schema(example = "EUR")
    String currency;
    
    @Schema(example = "10")
    Integer quantity;
    
    @Schema(example = "true")
    Boolean isPublished;
}
