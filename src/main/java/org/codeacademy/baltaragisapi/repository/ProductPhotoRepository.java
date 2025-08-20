package org.codeacademy.baltaragisapi.repository;

import java.util.List;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
    List<ProductPhoto> findAllByProductIdOrderBySortOrderAscIdAsc(Long productId);
    ProductPhoto findFirstByProductIdOrderBySortOrderAscIdAsc(Long productId);
    
    /**
     * Find the maximum sort order for a product's photos.
     * 
     * @param productId The product ID
     * @return The maximum sort order, or null if no photos exist
     */
    Integer findMaxSortOrderByProductId(Long productId);
}


