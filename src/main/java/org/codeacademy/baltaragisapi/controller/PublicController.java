package org.codeacademy.baltaragisapi.controller;

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
    public Page<ProductCardDto> listProducts(@RequestParam(value = "q", required = false) String q,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return catalogService.listPublished(q, pageable);
    }

    @GetMapping("/products/{slug}")
    public ProductDetailDto getProduct(@PathVariable String slug) {
        return catalogService.getBySlug(slug);
    }

    @PostMapping("/products/{slug}/waitlist")
    public ResponseEntity<?> addToWaitlist(@PathVariable String slug, @RequestBody WaitlistRequest body) {
        var product = catalogService.getBySlug(slug);
        if (product == null) return ResponseEntity.notFound().build();
        WaitlistAddStatus status = waitlistService.addToWaitlist(product.getId(), body.getEmail());
        return ResponseEntity.ok(status);
    }

    @PostMapping("/orders")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest body) {
        return checkoutService.createSingleItemOrder(body);
    }

    @GetMapping("/pages")
    public java.util.List<PageDto> pages() {
        return pageService.listPublished();
    }

    @GetMapping("/pages/{slug}")
    public PageDto pageBySlug(@PathVariable String slug) {
        return pageService.getBySlug(slug);
    }
}


