package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.codeacademy.baltaragisapi.exception.ValidationException;
import org.codeacademy.baltaragisapi.media.MediaStorage;
import org.codeacademy.baltaragisapi.repository.ProductPhotoRepository;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.codeacademy.baltaragisapi.mapper.ProductPhotoMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Service for handling product photo uploads with validation and metadata extraction.
 */
@Service
@Transactional
public class PhotoUploadService {
    
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    
    private final MediaStorage mediaStorage;
    private final ProductRepository productRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final ProductPhotoMapper productPhotoMapper;
    
    public PhotoUploadService(MediaStorage mediaStorage, 
                            ProductRepository productRepository, 
                            ProductPhotoRepository productPhotoRepository,
                            ProductPhotoMapper productPhotoMapper) {
        this.mediaStorage = mediaStorage;
        this.productRepository = productRepository;
        this.productPhotoRepository = productPhotoRepository;
        this.productPhotoMapper = productPhotoMapper;
    }
    
    /**
     * Upload a photo for a product and create a ProductPhoto record.
     * 
     * @param productId The product ID
     * @param file The uploaded file
     * @param altText Optional alt text for the image
     * @param sortOrder Optional sort order
     * @return The created ProductPhoto entity
     */
    public ProductPhoto uploadPhoto(Long productId, MultipartFile file, String altText, Integer sortOrder) {
        // Validate product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Product not found with ID: " + productId, null));
        
        // Validate file
        validateFile(file);
        
        // Extract image dimensions
        ImageDimensions dimensions = extractImageDimensions(file);
        
        // Generate filename with timestamp to avoid conflicts
        String filename = generateFilename(file.getOriginalFilename());
        
        try {
            // Upload file to storage
            String fileUrl = mediaStorage.uploadFile(file, productId, filename);
            
            // Create ProductPhoto record
            ProductPhoto photo = new ProductPhoto();
            photo.setProduct(product);
            photo.setUrl(fileUrl);
            photo.setAlt(altText != null ? altText : "");
            photo.setWidth(dimensions.width());
            photo.setHeight(dimensions.height());
            photo.setSortOrder(sortOrder != null ? sortOrder : getNextSortOrder(productId));
            
            return productPhotoRepository.save(photo);
        } catch (IOException e) {
            throw new ValidationException("Failed to upload file: " + e.getMessage(), null);
        }
    }
    
    /**
     * Upload multiple photos for a product.
     * 
     * @param productId The product ID
     * @param files The uploaded files
     * @param altTexts Optional alt texts for each image
     * @return List of created ProductPhoto entities
     */
    public List<ProductPhoto> uploadPhotos(Long productId, List<MultipartFile> files, List<String> altTexts) {
        if (files == null || files.isEmpty()) {
            throw new ValidationException("No files provided for upload", null);
        }
        
        return files.stream()
                .map(file -> {
                    String altText = altTexts != null && !altTexts.isEmpty() ? altTexts.get(0) : null;
                    return uploadPhoto(productId, file, altText, null);
                })
                .toList();
    }
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("File is required", null);
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException("File size exceeds maximum allowed size of 10MB", null);
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new ValidationException("Only JPEG, PNG, and WebP images are allowed", null);
        }
    }
    
    private ImageDimensions extractImageDimensions(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new ValidationException("Invalid image file", null);
            }
            return new ImageDimensions(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            throw new ValidationException("Failed to read image dimensions: " + e.getMessage(), null);
        }
    }
    
    private String generateFilename(String originalFilename) {
        if (originalFilename == null) {
            originalFilename = "image";
        }
        
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
            originalFilename = originalFilename.substring(0, lastDotIndex);
        }
        
        long timestamp = System.currentTimeMillis();
        return originalFilename + "_" + timestamp + extension;
    }
    
    private Integer getNextSortOrder(Long productId) {
        Integer maxSortOrder = productPhotoRepository.findMaxSortOrderByProductId(productId);
        return (maxSortOrder != null ? maxSortOrder : 0) + 1;
    }
    
    /**
     * Record class for image dimensions.
     */
    private record ImageDimensions(int width, int height) {}
}
