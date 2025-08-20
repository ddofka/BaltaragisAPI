package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}


