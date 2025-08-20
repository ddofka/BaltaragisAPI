package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.dto.*;
import org.codeacademy.baltaragisapi.enums.WaitlistAddStatus;
import org.codeacademy.baltaragisapi.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Public")
public class PublicController {

    private final CatalogService catalogService;
    private final CheckoutService checkoutService;
    private final WaitlistService waitlistService;
    private final PageService pageService;
    private final ArtistService artistService;

    public PublicController(CatalogService catalogService, CheckoutService checkoutService, WaitlistService waitlistService, PageService pageService, ArtistService artistService) {
        this.catalogService = catalogService;
        this.checkoutService = checkoutService;
        this.waitlistService = waitlistService;
        this.pageService = pageService;
        this.artistService = artistService;
    }

    @GetMapping("/artist")
    @Operation(summary = "Get artist profile",
        responses = {
            @ApiResponse(responseCode = "200", description = "Artist profile",
                headers = {
                    @io.swagger.v3.oas.annotations.headers.Header(name = "ETag", description = "Entity tag for caching", example = "\"abc123\""),
                    @io.swagger.v3.oas.annotations.headers.Header(name = "Last-Modified", description = "Last modification date", example = "Wed, 21 Oct 2015 07:28:00 GMT"),
                    @io.swagger.v3.oas.annotations.headers.Header(name = "Cache-Control", description = "Cache control directive", example = "public, max-age=600")
                }),
            @ApiResponse(responseCode = "304", description = "Not modified (conditional request)")
        }
    )
    public ResponseEntity<ArtistDto> getArtist() {
        ArtistDto artist = artistService.getProfile();
        String etag = generateETag(artist);
        OffsetDateTime lastModified = artist.getUpdatedAt();
        
        return ResponseEntity.ok()
            .eTag(etag)
            .lastModified(lastModified.toInstant())
            .body(artist);
    }

    @GetMapping("/products")
    @Operation(summary = "List published products",
        parameters = {
            @Parameter(name = "q", description = "Full-text query across name/shortDesc/longDesc", example = "print"),
            @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
            @Parameter(name = "size", description = "Page size", example = "12")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Page of products",
                headers = {
                    @io.swagger.v3.oas.annotations.headers.Header(name = "Cache-Control", description = "Cache control directive", example = "public, max-age=60")
                })
        }
    )
    public Page<ProductCardDto> listProducts(@RequestParam(value = "q", required = false) String q,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return catalogService.listPublished(q, pageable);
    }

    @GetMapping("/products/{slug}")
    @Operation(summary = "Get product details",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product detail",
                headers = {
                    @io.swagger.v3.oas.annotations.headers.Header(name = "ETag", description = "Entity tag for caching", example = "\"def456\""),
                    @io.swagger.v3.oas.annotations.headers.Header(name = "Last-Modified", description = "Last modification date", example = "Wed, 21 Oct 2015 07:28:00 GMT"),
                    @io.swagger.v3.oas.annotations.headers.Header(name = "Cache-Control", description = "Cache control directive", example = "public, max-age=300")
                }),
            @ApiResponse(responseCode = "304", description = "Not modified (conditional request)"),
            @ApiResponse(responseCode = "404", description = "Not found",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class),
                    examples = @ExampleObject(value = "{\n  \"type\": \"https://api.baltaragis.dev/problems/not_found\",\n  \"title\": \"Not Found\",\n  \"status\": 404,\n  \"detail\": \"Product not found\",\n  \"instance\": \"/api/v1/products/bad\",\n  \"code\": \"NOT_FOUND\"\n}")))
        }
    )
    public ResponseEntity<ProductDetailDto> getProduct(@PathVariable String slug) {
        ProductDetailDto product = catalogService.getBySlug(slug);
        String etag = generateETag(product);
        OffsetDateTime lastModified = product.getUpdatedAt();
        
        return ResponseEntity.ok()
            .eTag(etag)
            .lastModified(lastModified.toInstant())
            .body(product);
    }

    @PostMapping("/products/{slug}/waitlist")
    @Operation(summary = "Join stock waitlist",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = WaitlistRequest.class),
                examples = @ExampleObject(value = "{\n  \"email\": \"user@example.com\"\n}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Waitlist status",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.enums.WaitlistAddStatus.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class)))
        }
    )
    public ResponseEntity<?> addToWaitlist(@PathVariable String slug, @RequestBody WaitlistRequest body) {
        var product = catalogService.getBySlug(slug); // throws NotFound if missing
        WaitlistAddStatus status = waitlistService.addToWaitlist(product.getId(), body.getEmail());
        return ResponseEntity.ok(status);
    }

    @PostMapping("/orders")
    @Operation(summary = "Create order (single item)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = CreateOrderRequest.class),
                examples = @ExampleObject(name = "CreateOrderRequest", value = "{\n  \"productSlug\": \"sunset-print\",\n  \"qty\": 1,\n  \"email\": \"user@example.com\"\n}"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Order created",
                content = @Content(schema = @Schema(implementation = CreateOrderResponse.class),
                    examples = @ExampleObject(value = "{\n  \"orderId\": 123,\n  \"status\": \"PENDING\",\n  \"total\": \"45.00\",\n  \"currency\": \"EUR\"\n}"))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class),
                    examples = @ExampleObject(value = "{\n  \"type\": \"https://api.baltaragis.dev/problems/validation_failed\",\n  \"title\": \"Bad Request\",\n  \"status\": 400,\n  \"detail\": \"Invalid order request\",\n  \"code\": \"VALIDATION_FAILED\",\n  \"errors\": [{\n    \"field\": \"qty\", \"message\": \"Quantity must be greater than 0\"\n  }]\n}"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class))),
            @ApiResponse(responseCode = "409", description = "Insufficient stock",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class),
                    examples = @ExampleObject(value = "{\n  \"type\": \"https://api.baltaragis.dev/problems/insufficient_stock\",\n  \"title\": \"Conflict\",\n  \"detail\": \"Insufficient stock\",\n  \"code\": \"INSUFFICIENT_STOCK\"\n}")))
        }
    )
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest body) {
        return checkoutService.createSingleItemOrder(body);
    }

    @GetMapping("/pages")
    @Operation(summary = "List published pages")
    public List<PageDto> pages() {
        return pageService.listPublished();
    }

    @GetMapping("/pages/{slug}")
    @Operation(summary = "Get page by slug",
        responses = {
            @ApiResponse(responseCode = "200", description = "Page",
                headers = {
                    @io.swagger.v3.oas.annotations.headers.Header(name = "ETag", description = "Entity tag for caching", example = "\"ghi789\""),
                    @io.swagger.v3.oas.annotations.headers.Header(name = "Last-Modified", description = "Last modification date", example = "Wed, 21 Oct 2015 07:28:00 GMT"),
                    @io.swagger.v3.oas.annotations.headers.Header(name = "Cache-Control", description = "Cache control directive", example = "public, max-age=180")
                }),
            @ApiResponse(responseCode = "304", description = "Not modified (conditional request)"),
            @ApiResponse(responseCode = "404", description = "Not found",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class)))
        }
    )
    public ResponseEntity<PageDto> pageBySlug(@PathVariable String slug) {
        PageDto page = pageService.getBySlug(slug);
        String etag = generateETag(page);
        OffsetDateTime lastModified = page.getUpdatedAt();
        
        return ResponseEntity.ok()
            .eTag(etag)
            .lastModified(lastModified.toInstant())
            .body(page);
    }

    /**
     * Generate ETag for caching based on entity content and last modified time
     */
    private String generateETag(Object entity) {
        // Simple ETag generation - in production you might want a more sophisticated approach
        int hashCode = entity.hashCode();
        return "\"" + Integer.toHexString(hashCode) + "\"";
    }
}


