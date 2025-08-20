package org.codeacademy.baltaragisapi.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateOrderResponse {
    Long orderId;
    String status;
    String total; // decimal string
    String currency;
}


