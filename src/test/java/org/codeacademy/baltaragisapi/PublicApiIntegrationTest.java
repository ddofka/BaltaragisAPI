package org.codeacademy.baltaragisapi;

import org.codeacademy.baltaragisapi.dto.*;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.codeacademy.baltaragisapi.enums.OrderStatus;
import org.codeacademy.baltaragisapi.enums.WaitlistAddStatus;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.codeacademy.baltaragisapi.repository.ProductPhotoRepository;
import org.codeacademy.baltaragisapi.repository.OrderRepository;
import org.codeacademy.baltaragisapi.repository.StockWaitlistRepository;
import org.codeacademy.baltaragisapi.repository.PageRepository;
import org.codeacademy.baltaragisapi.repository.ArtistProfileRepository;
import org.codeacademy.baltaragisapi.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PublicApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPhotoRepository productPhotoRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockWaitlistRepository stockWaitlistRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ArtistProfileRepository artistProfileRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1";
        // Clear any existing orders before each test (delete child records first)
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        stockWaitlistRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testListPublishedProducts_UnpublishedHidden() {
        // Given: Products exist (some published, some not) - seeded by Flyway
        
        // When: Request published products
        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/products?page=0&size=10",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map>() {}
        );

        // Then: Only published products are returned
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        // Verify the response structure
        Map<String, Object> body = response.getBody();
        assertThat(body.get("content")).isNotNull();
        List<Map<String, Object>> products = (List<Map<String, Object>>) body.get("content");
        assertThat(products).isNotEmpty();
        
        // Verify all returned products have required fields
        products.forEach(product -> {
            assertThat(product.get("slug")).isNotNull();
            assertThat(product.get("name")).isNotNull();
            assertThat(product.get("price")).isNotNull();
        });
    }

    @Test
    @Order(2)
    void testGetProductBySlug_ExistingProduct_Returns200() {
        // Given: Product exists (seeded by Flyway)
        String existingSlug = "sunset-print";
        
        // When: Request product by slug
        ResponseEntity<ProductDetailDto> response = restTemplate.getForEntity(
            baseUrl + "/products/" + existingSlug,
            ProductDetailDto.class
        );

        // Then: Product is returned with 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSlug()).isEqualTo(existingSlug);
        assertThat(response.getBody().getName()).isNotNull();
        assertThat(response.getBody().getPrice()).isNotNull();
        assertThat(response.getBody().getPhotos()).isNotNull();
    }

    @Test
    @Order(3)
    void testGetProductBySlug_UnknownSlug_Returns404() {
        // Given: Non-existent slug
        String unknownSlug = "non-existent-product";
        
        // When: Request product by unknown slug
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/products/" + unknownSlug,
            Map.class
        );

        // Then: 404 is returned
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo(404);
        assertThat(response.getBody().get("title")).isEqualTo("Not Found");
    }

    @Test
    @Order(4)
    void testCreateOrder_WithEnoughStock_StockDecreases() {
        // Given: Product with sufficient stock
        Product product = productRepository.findBySlug("sunset-print").orElseThrow();
        int initialStock = product.getQuantity();
        assertThat(initialStock).isGreaterThan(0);
        
        CreateOrderRequest request = new CreateOrderRequest(
            null, // productId
            product.getSlug(), // productSlug
            1, // qty
            "test@example.com" // email
        );
        
        // When: Create order
        ResponseEntity<CreateOrderResponse> response = restTemplate.postForEntity(
            baseUrl + "/orders",
            new HttpEntity<>(request),
            CreateOrderResponse.class
        );

        // Then: Order is created successfully
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getOrderId()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(OrderStatus.PENDING.name());
        
        // And: Stock is decreased
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(initialStock - 1);
        
        // And: Order exists in database
        assertThat(orderRepository.findById(response.getBody().getOrderId())).isPresent();
    }

    @Test
    @Order(5)
    void testCreateOrder_WithZeroStock_Returns409() {
        // Given: Product with zero stock
        Product product = productRepository.findBySlug("forest-sketch").orElseThrow(); // This should be out of stock
        assertThat(product.getQuantity()).isEqualTo(0);
        
        // Verify no orders exist before the test
        long initialOrderCount = orderRepository.count();
        System.out.println("Initial order count: " + initialOrderCount);
        
        CreateOrderRequest request = new CreateOrderRequest(
            null, // productId
            product.getSlug(), // productSlug
            1, // qty
            "test@example.com" // email
        );
        
        // When: Create order
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/orders",
            new HttpEntity<>(request),
            Map.class
        );

        // Then: 409 Conflict is returned
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo(409);
        assertThat(response.getBody().get("title")).isEqualTo("Conflict");
        assertThat(response.getBody().get("code")).isEqualTo("INSUFFICIENT_STOCK");
        
        // And: Stock remains zero
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(0);
        
        // And: No order was created
        long finalOrderCount = orderRepository.count();
        System.out.println("Final order count: " + finalOrderCount);
        assertThat(finalOrderCount).isEqualTo(initialOrderCount);
    }

    @Test
    @Order(6)
    void testAddToWaitlist_Twice_SecondIsIdempotent() {
        // Given: Product with zero stock
        Product product = productRepository.findBySlug("forest-sketch").orElseThrow();
        assertThat(product.getQuantity()).isEqualTo(0);
        
        String email = "waitlist@example.com";
        WaitlistRequest request = new WaitlistRequest(email);
        
        // When: Add to waitlist first time
        ResponseEntity<WaitlistAddStatus> firstResponse = restTemplate.postForEntity(
            baseUrl + "/products/" + product.getSlug() + "/waitlist",
            new HttpEntity<>(request),
            WaitlistAddStatus.class
        );

        // Then: First addition is successful
        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firstResponse.getBody()).isEqualTo(WaitlistAddStatus.ADDED);
        
        // And: Waitlist entry exists
        assertThat(stockWaitlistRepository.existsByProductIdAndEmailIgnoreCase(product.getId(), email)).isTrue();
        
        // When: Add to waitlist second time
        ResponseEntity<WaitlistAddStatus> secondResponse = restTemplate.postForEntity(
            baseUrl + "/products/" + product.getSlug() + "/waitlist",
            new HttpEntity<>(request),
            WaitlistAddStatus.class
        );

        // Then: Second addition returns already subscribed
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(secondResponse.getBody()).isEqualTo(WaitlistAddStatus.ALREADY_SUBSCRIBED);
        
        // And: Only one waitlist entry exists
        assertThat(stockWaitlistRepository.count()).isEqualTo(1);
    }

    @Test
    @Order(7)
    void testGetPages_ReturnsPublishedPages() {
        // When: Request published pages
        ResponseEntity<List<PageDto>> response = restTemplate.exchange(
            baseUrl + "/pages",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<PageDto>>() {}
        );

        // Then: Published pages are returned
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        
        // Verify all returned pages are published
        response.getBody().forEach(page -> {
            assertThat(page.getSlug()).isNotNull();
            assertThat(page.getTitle()).isNotNull();
            assertThat(page.getContentMd()).isNotNull();
        });
    }

    @Test
    @Order(8)
    void testGetPageBySlug_ExistingPage_Returns200() {
        // Given: Page exists (seeded by Flyway)
        String existingSlug = "home";
        
        // When: Request page by slug
        ResponseEntity<PageDto> response = restTemplate.getForEntity(
            baseUrl + "/pages/" + existingSlug,
            PageDto.class
        );

        // Then: Page is returned with 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSlug()).isEqualTo(existingSlug);
        assertThat(response.getBody().getTitle()).isNotNull();
        assertThat(response.getBody().getContentMd()).isNotNull();
    }

    @Test
    @Order(9)
    void testGetArtist_ReturnsArtistProfile() {
        // When: Request artist profile
        ResponseEntity<ArtistDto> response = restTemplate.getForEntity(
            baseUrl + "/artist",
            ArtistDto.class
        );

        // Then: Artist profile is returned
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isNotNull();
        assertThat(response.getBody().getBio()).isNotNull();
    }
}
