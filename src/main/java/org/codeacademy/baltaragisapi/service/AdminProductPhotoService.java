package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.dto.admin.CreateProductPhotoRequest;
import org.codeacademy.baltaragisapi.dto.admin.UpdateProductPhotoRequest;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.repository.ProductPhotoRepository;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminProductPhotoService {

    private final ProductPhotoRepository photoRepository;
    private final ProductRepository productRepository;

    public AdminProductPhotoService(ProductPhotoRepository photoRepository, ProductRepository productRepository) {
        this.photoRepository = photoRepository;
        this.productRepository = productRepository;
    }

    public List<ProductPhoto> getAllPhotos() {
        return photoRepository.findAll();
    }

    public ProductPhoto getPhotoById(Long id) {
        return photoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product photo not found"));
    }

    public List<ProductPhoto> getPhotosByProductId(Long productId) {
        return photoRepository.findAllByProductIdOrderBySortOrderAscIdAsc(productId);
    }

    public ProductPhoto createPhoto(CreateProductPhotoRequest request) {
        // Verify product exists
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found"));

        ProductPhoto photo = new ProductPhoto();
        photo.setProduct(product);
        photo.setUrl(request.getUrl());
        photo.setAlt(request.getAlt());
        photo.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        photo.setWidth(request.getWidth());
        photo.setHeight(request.getHeight());

        return photoRepository.save(photo);
    }

    public ProductPhoto updatePhoto(Long id, UpdateProductPhotoRequest request) {
        ProductPhoto photo = getPhotoById(id);
        
        if (request.getUrl() != null) {
            photo.setUrl(request.getUrl());
        }
        if (request.getAlt() != null) {
            photo.setAlt(request.getAlt());
        }
        if (request.getSortOrder() != null) {
            photo.setSortOrder(request.getSortOrder());
        }
        if (request.getWidth() != null) {
            photo.setWidth(request.getWidth());
        }
        if (request.getHeight() != null) {
            photo.setHeight(request.getHeight());
        }

        return photoRepository.save(photo);
    }

    public void deletePhoto(Long id) {
        ProductPhoto photo = getPhotoById(id);
        photoRepository.delete(photo);
    }
}
