package org.codeacademy.baltaragisapi.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductCardDto {
    Long id;
    String name;
    String slug;
    String price; // decimal string (e.g., "45.00")
    String currency;
    String thumbnailUrl;
    boolean isInStock;
}


