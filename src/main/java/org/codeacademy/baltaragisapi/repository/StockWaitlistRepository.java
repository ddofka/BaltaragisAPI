package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.StockWaitlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockWaitlistRepository extends JpaRepository<StockWaitlist, Long> {
    boolean existsByProductIdAndEmailIgnoreCase(Long productId, String email);
}


