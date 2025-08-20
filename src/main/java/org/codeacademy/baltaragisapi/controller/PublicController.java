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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ArtistDto getArtist() {
        return artistService.getProfile();
    }

    @GetMapping("/products")
    @Operation(summary = "List published products",
        parameters = {
            @Parameter(name = "q", description = "Full-text query across name/shortDesc/longDesc", example = "print"),
            @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
            @Parameter(name = "size", description = "Page size", example = "12")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Page of products")
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
            @ApiResponse(responseCode = "200", description = "Product detail"),
            @ApiResponse(responseCode = "404", description = "Not found",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class),
                    examples = @ExampleObject(value = "{\n  \"type\": \"https://api.baltaragis.dev/problems/not_found\",\n  \"title\": \"Not Found\",\n  \"status\": 404,\n  \"detail\": \"Product not found\",\n  \"instance\": \"/api/v1/products/bad\",\n  \"code\": \"NOT_FOUND\"\n}")))
        }
    )
    public ProductDetailDto getProduct(@PathVariable String slug) {
        return catalogService.getBySlug(slug);
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
                    examples = @ExampleObject(value = "{\n  \"type\": \"https://api.baltaragis.dev/problems/insufficient_stock\",\n  \"title\": \"Conflict\",\n  \"status\": 409,\n  \"detail\": \"Insufficient stock\",\n  \"code\": \"INSUFFICIENT_STOCK\"\n}")))
        }
    )
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest body) {
        return checkoutService.createSingleItemOrder(body);
    }

    @GetMapping("/pages")
    @Operation(summary = "List published pages")
    public java.util.List<PageDto> pages() {
        return pageService.listPublished();
    }

    @GetMapping("/pages/{slug}")
    @Operation(summary = "Get page by slug",
        responses = {
            @ApiResponse(responseCode = "200", description = "Page"),
            @ApiResponse(responseCode = "404", description = "Not found",
                content = @Content(schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class)))
        }
    )
    public PageDto pageBySlug(@PathVariable String slug) {
        return pageService.getBySlug(slug);
    }
}


