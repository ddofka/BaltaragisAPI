package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.dto.admin.PhotoUploadResponse;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.codeacademy.baltaragisapi.service.PhotoUploadService;
import org.codeacademy.baltaragisapi.mapper.ProductPhotoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling product photo uploads in the admin section.
 */
@RestController
@RequestMapping("/api/v1/admin/products")
@Tag(name = "Admin: Product Photos", description = "Product photo upload and management operations")
public class AdminPhotoUploadController {
    
    private final PhotoUploadService photoUploadService;
    private final ProductPhotoMapper productPhotoMapper;
    
    public AdminPhotoUploadController(PhotoUploadService photoUploadService, ProductPhotoMapper productPhotoMapper) {
        this.photoUploadService = photoUploadService;
        this.productPhotoMapper = productPhotoMapper;
    }
    
    /**
     * Upload a single photo for a product.
     * 
     * @param productId The product ID
     * @param file The image file to upload
     * @param altText Optional alt text for accessibility
     * @param sortOrder Optional sort order for display
     * @return The uploaded photo details
     */
    @PostMapping(value = "/{productId}/photos/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload a product photo",
        description = "Upload a single photo for a product. Supports JPEG, PNG, and WebP formats up to 10MB."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Photo uploaded successfully",
            content = @Content(schema = @Schema(implementation = PhotoUploadResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file format, size, or missing file"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    public ResponseEntity<PhotoUploadResponse> uploadPhoto(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long productId,
            
            @Parameter(description = "Image file (JPEG, PNG, or WebP, max 10MB)")
            @RequestParam("file") MultipartFile file,
            
            @Parameter(description = "Alt text for accessibility", example = "Product image")
            @RequestParam(value = "altText", required = false) String altText,
            
            @Parameter(description = "Sort order for display", example = "1")
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder) {
        
        ProductPhoto photo = photoUploadService.uploadPhoto(productId, file, altText, sortOrder);
        
        PhotoUploadResponse response = productPhotoMapper.toPhotoUploadResponse(photo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Upload multiple photos for a product.
     * 
     * @param productId The product ID
     * @param files The image files to upload
     * @param altTexts Optional alt texts for each image
     * @return List of uploaded photo details
     */
    @PostMapping(value = "/{productId}/photos/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload multiple product photos",
        description = "Upload multiple photos for a product. Supports JPEG, PNG, and WebP formats up to 10MB each."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Photos uploaded successfully",
            content = @Content(schema = @Schema(implementation = PhotoUploadResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file format, size, or missing files"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    public ResponseEntity<List<PhotoUploadResponse>> uploadMultiplePhotos(
            @Parameter(description = "Product ID", example = "1")
            @PathVariable Long productId,
            
            @Parameter(description = "Image files (JPEG, PNG, or WebP, max 10MB each)")
            @RequestParam("files") List<MultipartFile> files,
            
            @Parameter(description = "Alt texts for accessibility")
            @RequestParam(value = "altTexts", required = false) List<String> altTexts) {
        
        List<ProductPhoto> photos = photoUploadService.uploadPhotos(productId, files, altTexts);
        
        List<PhotoUploadResponse> responses = photos.stream()
                .map(productPhotoMapper::toPhotoUploadResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
    

}
