package org.codeacademy.baltaragisapi.service;

import java.time.OffsetDateTime;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.entity.StockWaitlist;
import org.codeacademy.baltaragisapi.enums.WaitlistAddStatus;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.codeacademy.baltaragisapi.repository.StockWaitlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaitlistService {

    private final ProductRepository productRepository;
    private final StockWaitlistRepository waitlistRepository;

    public WaitlistService(ProductRepository productRepository, StockWaitlistRepository waitlistRepository) {
        this.productRepository = productRepository;
        this.waitlistRepository = waitlistRepository;
    }

    @Transactional
    public WaitlistAddStatus addToWaitlist(Long productId, String email) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return WaitlistAddStatus.NOT_ELIGIBLE;
        int available = product.getQuantity() != null ? product.getQuantity() : 0;
        if (available > 0) return WaitlistAddStatus.NOT_ELIGIBLE;

        if (waitlistRepository.existsByProductIdAndEmailIgnoreCase(productId, email)) {
            return WaitlistAddStatus.ALREADY_SUBSCRIBED;
        }

        StockWaitlist entry = new StockWaitlist();
        entry.setProduct(product);
        entry.setEmail(email);
        entry.setCreatedAt(OffsetDateTime.now());
        waitlistRepository.save(entry);
        return WaitlistAddStatus.ADDED;
    }
}


