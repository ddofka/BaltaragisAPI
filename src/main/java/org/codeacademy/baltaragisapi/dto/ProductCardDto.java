package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(name = "ProductCard", description = "Compact product view for listings")
public class ProductCardDto {
    @Schema(example = "1") Long id;
    @Schema(example = "Sunset Print") String name;
    @Schema(example = "sunset-print") String slug;
    @Schema(example = "45.00", description = "Decimal price as string") String price;
    @Schema(example = "EUR") String currency;
    @Schema(example = "https://cdn.example.com/p/sunset1.jpg") String thumbnailUrl;
    @Schema(example = "true") boolean isInStock;
}


