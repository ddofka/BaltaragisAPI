package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateOrderResponse {
    @Schema(example = "123") Long orderId;
    @Schema(example = "PENDING") String status;
    @Schema(example = "45.00") String total;
    @Schema(example = "EUR") String currency;
}


