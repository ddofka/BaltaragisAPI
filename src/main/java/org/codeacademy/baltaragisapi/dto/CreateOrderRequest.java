package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class CreateOrderRequest {
    @Schema(example = "1") Long productId;
    @Schema(example = "sunset-print") String productSlug;
    @Schema(example = "1") Integer qty;
    @Schema(example = "user@example.com") String email;
}


