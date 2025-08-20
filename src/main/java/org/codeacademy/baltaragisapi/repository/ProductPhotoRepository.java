package org.codeacademy.baltaragisapi.repository;

import java.util.List;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
    List<ProductPhoto> findAllByProductIdOrderBySortOrderAscIdAsc(Long productId);
    ProductPhoto findFirstByProductIdOrderBySortOrderAscIdAsc(Long productId);
}


