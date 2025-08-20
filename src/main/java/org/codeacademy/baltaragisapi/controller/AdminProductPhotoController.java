package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.dto.admin.CreateProductPhotoRequest;
import org.codeacademy.baltaragisapi.dto.admin.UpdateProductPhotoRequest;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.codeacademy.baltaragisapi.service.AdminProductPhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/product-photos")
@Tag(name = "Admin: Product Photos", description = "Product photo management endpoints")
public class AdminProductPhotoController {

    private final AdminProductPhotoService photoService;

    public AdminProductPhotoController(AdminProductPhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping
    @Operation(summary = "List all product photos",
        description = "Get all product photos for admin management")
    public List<ProductPhoto> getAllPhotos() {
        return photoService.getAllPhotos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get photo by ID",
        description = "Get a specific product photo by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Photo found"),
            @ApiResponse(responseCode = "404", description = "Photo not found")
        })
    public ProductPhoto getPhotoById(@PathVariable Long id) {
        return photoService.getPhotoById(id);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get photos by product ID",
        description = "Get all photos for a specific product, ordered by sort order")
    public List<ProductPhoto> getPhotosByProductId(@PathVariable Long productId) {
        return photoService.getPhotosByProductId(productId);
    }

    @PostMapping
    @Operation(summary = "Create new product photo",
        description = "Create a new product photo",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = CreateProductPhotoRequest.class),
                examples = @ExampleObject(
                    name = "CreateProductPhotoRequest",
                    value = "{\n  \"productId\": 1,\n  \"url\": \"https://example.com/photos/sunset1.jpg\",\n  \"alt\": \"Sunset Print main view\",\n  \"sortOrder\": 1,\n  \"width\": 1200,\n  \"height\": 800\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Photo created successfully",
                content = @Content(schema = @Schema(implementation = ProductPhoto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        })
    public ResponseEntity<ProductPhoto> createPhoto(@Valid @RequestBody CreateProductPhotoRequest request) {
        ProductPhoto photo = photoService.createPhoto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update photo",
        description = "Update an existing product photo by ID",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = UpdateProductPhotoRequest.class),
                examples = @ExampleObject(
                    name = "UpdateProductPhotoRequest",
                    value = "{\n  \"url\": \"https://example.com/photos/sunset1-updated.jpg\",\n  \"alt\": \"Updated Sunset Print view\",\n  \"sortOrder\": 2\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Photo updated successfully"),
            @ApiResponse(responseCode = "404", description = "Photo not found")
        })
    public ProductPhoto updatePhoto(@PathVariable Long id, @Valid @RequestBody UpdateProductPhotoRequest request) {
        return photoService.updatePhoto(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete photo",
        description = "Delete a product photo by ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Photo deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Photo not found")
        })
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
}
