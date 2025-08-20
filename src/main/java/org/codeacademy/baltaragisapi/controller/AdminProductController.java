package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.dto.admin.CreateProductRequest;
import org.codeacademy.baltaragisapi.dto.admin.UpdateProductRequest;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.service.AdminProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
@Tag(name = "Admin: Products", description = "Product management endpoints")
public class AdminProductController {

    private final AdminProductService productService;

    public AdminProductController(AdminProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List all products",
        description = "Get all products (published and unpublished) for admin management")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID",
        description = "Get a specific product by its ID for admin management",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        })
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @Operation(summary = "Create new product",
        description = "Create a new product with validation for duplicate slugs",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = CreateProductRequest.class),
                examples = @ExampleObject(
                    name = "CreateProductRequest",
                    value = "{\n  \"name\": \"Sunset Print\",\n  \"slug\": \"sunset-print\",\n  \"shortDesc\": \"A3 giclée print\",\n  \"longDesc\": \"High-quality giclée print of a sunset.\",\n  \"priceCents\": 4500,\n  \"currency\": \"EUR\",\n  \"quantity\": 10,\n  \"isPublished\": true\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Product with duplicate slug already exists",
                content = @Content(
                    schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class),
                    examples = @ExampleObject(
                        value = "{\n  \"type\": \"https://api.baltaragis.dev/problems/conflict\",\n  \"title\": \"Conflict\",\n  \"status\": 409,\n  \"detail\": \"Product with slug 'sunset-print' already exists\",\n  \"code\": \"DUPLICATE_SLUG\"\n}"
                    )
                ))
        })
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product",
        description = "Update an existing product by ID",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = UpdateProductRequest.class),
                examples = @ExampleObject(
                    name = "UpdateProductRequest",
                    value = "{\n  \"name\": \"Updated Sunset Print\",\n  \"priceCents\": 5000,\n  \"isPublished\": false\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        })
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product",
        description = "Delete a product by ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
