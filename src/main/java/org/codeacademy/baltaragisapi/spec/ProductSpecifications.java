package org.codeacademy.baltaragisapi.spec;

import org.codeacademy.baltaragisapi.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecifications {

    private ProductSpecifications() {}

    public static Specification<Product> isPublished() {
        return (root, query, cb) -> cb.isTrue(root.get("isPublished"));
    }

    public static Specification<Product> byQuery(String queryText) {
        if (queryText == null || queryText.trim().isEmpty()) {
            return null;
        }
        String like = "%" + queryText.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(cb.lower(root.get("shortDesc")), like),
                cb.like(cb.lower(root.get("longDesc")), like)
        );
    }

    public static Specification<Product> byCollection(String collectionSlug) {
        // Placeholder for future relation; currently returns null to be ignored when composing
        return null;
    }

    public static Specification<Product> byMaterial(String material) {
        // Placeholder for future attribute; currently returns null
        return null;
    }
}


