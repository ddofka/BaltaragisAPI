package org.codeacademy.baltaragisapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

/**
 * Entity for storing internationalized translations.
 * Maps translation keys to localized values for different locales.
 */
@Entity
@Table(name = "translation", indexes = {
    @Index(name = "idx_translation_locale_key", columnList = "locale, translation_key", unique = true),
    @Index(name = "idx_translation_locale", columnList = "locale")
})
@Getter
@Setter
public class Translation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "translation_key", nullable = false, length = 255)
    private String key;
    
    @Column(nullable = false, length = 10)
    private String locale;
    
    @Column(name = "translation_value", columnDefinition = "TEXT", nullable = false)
    private String value;
    
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    protected void onCreate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
