package org.codeacademy.baltaragisapi.dto;

import lombok.Value;

@Value
public class CreateOrderRequest {
    Long productId;
    String productSlug;
    Integer qty;
    String email;
}


