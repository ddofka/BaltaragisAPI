package org.codeacademy.baltaragisapi.mapper;

import org.codeacademy.baltaragisapi.dto.ProductCardDto;
import org.codeacademy.baltaragisapi.dto.ProductDetailDto;
import org.codeacademy.baltaragisapi.entity.Product;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "price", expression = "java(toDecimalString(product.getPriceCents()))")
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "isInStock", expression = "java(product.getQuantity() != null && product.getQuantity() > 0)")
    ProductCardDto toCard(Product product);

    @Mapping(target = "price", expression = "java(toDecimalString(product.getPriceCents()))")
    @Mapping(target = "isInStock", expression = "java(product.getQuantity() != null && product.getQuantity() > 0)")
    @Mapping(target = "photos", ignore = true)
    ProductDetailDto toDetail(Product product);

    default String toDecimalString(Integer cents) {
        if (cents == null) return null;
        return new BigDecimal(cents).movePointLeft(2).setScale(2, RoundingMode.UNNECESSARY).toPlainString();
    }

    default Integer toCents(String decimal) {
        if (decimal == null) return null;
        return new BigDecimal(decimal.replace(",", ".")).movePointRight(2).setScale(0, RoundingMode.UNNECESSARY).intValueExact();
    }
}


