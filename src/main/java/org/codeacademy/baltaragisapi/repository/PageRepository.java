package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {
    Optional<Page> findBySlugAndIsPublishedTrue(String slug);
    boolean existsBySlug(String slug);
    
    List<Page> findByIsPublishedTrue();
}


