package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Value
@Schema(name = "CreateProductRequest", description = "Request to create a new product")
public class CreateProductRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be 255 characters or less")
    @Schema(example = "Sunset Print", required = true)
    String name;
    
    @NotBlank(message = "Product slug is required")
    @Size(max = 255, message = "Product slug must be 255 characters or less")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Schema(example = "sunset-print", required = true)
    String slug;
    
    @Schema(example = "A3 giclée print", description = "Short description")
    String shortDesc;
    
    @Schema(example = "High-quality giclée print of a sunset.", description = "Long description")
    String longDesc;
    
    @NotNull(message = "Price in cents is required")
    @Positive(message = "Price must be positive")
    @Schema(example = "4500", required = true, description = "Price in cents (e.g., 4500 = €45.00)")
    Integer priceCents;
    
    @Size(max = 3, message = "Currency code must be 3 characters or less")
    @Schema(example = "EUR", defaultValue = "EUR")
    String currency = "EUR";
    
    @NotNull(message = "Quantity is required")
    @Schema(example = "10", required = true, defaultValue = "0")
    Integer quantity = 0;
    
    @Schema(example = "true", defaultValue = "false")
    Boolean isPublished = false;
}
