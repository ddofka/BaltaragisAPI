package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.StockWaitlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockWaitlistRepository extends JpaRepository<StockWaitlist, Long> {
    boolean existsByProductIdAndEmailIgnoreCase(Long productId, String email);
    
    /**
     * Find all waitlist entries for a product that haven't been notified yet
     */
    List<StockWaitlist> findByProductIdAndNotifiedAtIsNull(Long productId);
}


