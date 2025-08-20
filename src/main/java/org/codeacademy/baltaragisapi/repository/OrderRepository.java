package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}


