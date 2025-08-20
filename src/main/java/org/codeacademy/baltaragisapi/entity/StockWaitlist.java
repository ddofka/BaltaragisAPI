package org.codeacademy.baltaragisapi.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "stock_waitlist", uniqueConstraints = {
        @UniqueConstraint(name = "uk_waitlist_product_email", columnNames = {"product_id", "email"})
})
public class StockWaitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "notified_at")
    private OffsetDateTime notifiedAt;

}


