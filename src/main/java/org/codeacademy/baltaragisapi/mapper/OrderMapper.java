package org.codeacademy.baltaragisapi.mapper;

import org.codeacademy.baltaragisapi.dto.CreateOrderResponse;
import org.codeacademy.baltaragisapi.entity.Order;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ProductMapper.class)
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "total", expression = "java(new java.math.BigDecimal(order.getTotalCents()).movePointLeft(2).toPlainString())")
    @Mapping(target = "currency", source = "currency")
    CreateOrderResponse toCreateResponse(Order order);
}


