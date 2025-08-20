package org.codeacademy.baltaragisapi.dto;

import java.time.OffsetDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(name = "ProductDetail", description = "Product detail view")
public class ProductDetailDto {
    @Schema(example = "1") Long id;
    @Schema(example = "Sunset Print") String name;
    @Schema(example = "sunset-print") String slug;
    @Schema(example = "45.00") String price;
    @Schema(example = "EUR") String currency;
    @Schema(example = "true") boolean isInStock;
    @Schema(example = "High-quality giclée print of a sunset.") String longDesc;
    @Schema(example = "3") Integer quantity;
    @Schema(description = "Photo URLs") List<String> photos;
    @Schema(description = "Last update timestamp") OffsetDateTime updatedAt;
}


