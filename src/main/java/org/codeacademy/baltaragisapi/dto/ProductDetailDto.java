package org.codeacademy.baltaragisapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductDetailDto {
    Long id;
    String name;
    String slug;
    String price; // decimal string
    String currency;
    boolean isInStock;
    String longDesc;
    Integer quantity;
    List<String> photos; // list of URLs
}


