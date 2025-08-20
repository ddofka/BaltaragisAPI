package org.codeacademy.baltaragisapi.service;

import java.util.List;
import java.util.stream.Collectors;
import org.codeacademy.baltaragisapi.dto.ProductCardDto;
import org.codeacademy.baltaragisapi.dto.ProductDetailDto;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.mapper.ProductMapper;
import org.codeacademy.baltaragisapi.repository.ProductPhotoRepository;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.codeacademy.baltaragisapi.spec.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    private final ProductRepository productRepository;
    private final ProductPhotoRepository photoRepository;
    private final ProductMapper productMapper;

    public CatalogService(ProductRepository productRepository, ProductPhotoRepository photoRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.photoRepository = photoRepository;
        this.productMapper = productMapper;
    }

    public Page<ProductCardDto> listPublished(String query, Pageable pageable) {
        Specification<Product> spec = Specification.where(ProductSpecifications.isPublished())
                .and(ProductSpecifications.byQuery(query));
        return productRepository.findAll(spec, pageable)
                .map(product -> {
                    ProductCardDto dto = productMapper.toCard(product);
                    var thumb = photoRepository.findFirstByProductIdOrderBySortOrderAscIdAsc(product.getId());
                    return ProductCardDto.builder()
                            .id(dto.getId())
                            .name(dto.getName())
                            .slug(dto.getSlug())
                            .price(dto.getPrice())
                            .currency(dto.getCurrency())
                            .thumbnailUrl(thumb != null ? thumb.getUrl() : null)
                            .isInStock(dto.isInStock())
                            .build();
                });
    }

    public ProductDetailDto getBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElse(null);
        if (product == null) return null;
        ProductDetailDto base = productMapper.toDetail(product);
        List<String> urls = photoRepository.findAllByProductIdOrderBySortOrderAscIdAsc(product.getId())
                .stream().map(p -> p.getUrl()).collect(Collectors.toList());
        return ProductDetailDto.builder()
                .id(base.getId())
                .name(base.getName())
                .slug(base.getSlug())
                .price(base.getPrice())
                .currency(base.getCurrency())
                .isInStock(base.isInStock())
                .longDesc(base.getLongDesc())
                .quantity(base.getQuantity())
                .photos(urls)
                .build();
    }
}


