package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.dto.admin.CreateProductRequest;
import org.codeacademy.baltaragisapi.dto.admin.UpdateProductRequest;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.exception.ConflictException;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class AdminProductService {

    private final ProductRepository productRepository;
    private final WaitlistNotificationService waitlistNotificationService;

    public AdminProductService(ProductRepository productRepository, WaitlistNotificationService waitlistNotificationService) {
        this.productRepository = productRepository;
        this.waitlistNotificationService = waitlistNotificationService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public Product createProduct(CreateProductRequest request) {
        // Check for duplicate slug
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new ConflictException("Product with slug '" + request.getSlug() + "' already exists");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setShortDesc(request.getShortDesc());
        product.setLongDesc(request.getLongDesc());
        product.setPriceCents(request.getPriceCents());
        product.setCurrency(request.getCurrency() != null ? request.getCurrency() : "EUR");
        product.setQuantity(request.getQuantity() != null ? request.getQuantity() : 0);
        product.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : false);
        
        OffsetDateTime now = OffsetDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        Product savedProduct = productRepository.save(product);
        
        // Check if we should send waitlist notifications for newly created products
        if (savedProduct.getIsPublished() && savedProduct.getQuantity() > 0) {
            waitlistNotificationService.checkAndSendWaitlistNotifications(savedProduct, 0, false);
        }
        
        return savedProduct;
    }

    public Product updateProduct(Long id, UpdateProductRequest request) {
        Product product = getProductById(id);
        
        // Store previous values for waitlist notification logic
        Integer previousQuantity = product.getQuantity();
        Boolean previousIsPublished = product.getIsPublished();
        
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getShortDesc() != null) {
            product.setShortDesc(request.getShortDesc());
        }
        if (request.getLongDesc() != null) {
            product.setLongDesc(request.getLongDesc());
        }
        if (request.getPriceCents() != null) {
            product.setPriceCents(request.getPriceCents());
        }
        if (request.getCurrency() != null) {
            product.setCurrency(request.getCurrency());
        }
        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }
        if (request.getIsPublished() != null) {
            product.setIsPublished(request.getIsPublished());
        }
        
        product.setUpdatedAt(OffsetDateTime.now());
        Product updatedProduct = productRepository.save(product);
        
        // Check if we should send waitlist notifications
        waitlistNotificationService.checkAndSendWaitlistNotifications(updatedProduct, previousQuantity, previousIsPublished);
        
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
