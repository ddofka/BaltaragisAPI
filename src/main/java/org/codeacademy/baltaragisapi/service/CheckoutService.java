package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.dto.CreateOrderRequest;
import org.codeacademy.baltaragisapi.dto.CreateOrderResponse;
import org.codeacademy.baltaragisapi.entity.Order;
import org.codeacademy.baltaragisapi.entity.OrderItem;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.enums.OrderStatus;
import org.codeacademy.baltaragisapi.exception.ValidationException;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.exception.InsufficientStockException;
import org.codeacademy.baltaragisapi.mapper.OrderMapper;
import org.codeacademy.baltaragisapi.repository.OrderItemRepository;
import org.codeacademy.baltaragisapi.repository.OrderRepository;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    public CheckoutService(ProductRepository productRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, OrderMapper orderMapper) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public CreateOrderResponse createSingleItemOrder(CreateOrderRequest req) {
        if ((req.getProductId() == null && (req.getProductSlug() == null || req.getProductSlug().isBlank())) || req.getQty() == null || req.getQty() <= 0) {
            java.util.Map<String, String> errors = new java.util.HashMap<>();
            if (req.getQty() == null || req.getQty() <= 0) errors.put("qty", "Quantity must be greater than 0");
            if (req.getProductId() == null && (req.getProductSlug() == null || req.getProductSlug().isBlank())) errors.put("product", "productId or productSlug required");
            throw new ValidationException("Invalid order request", errors);
        }

        Product product = req.getProductId() != null
                ? productRepository.findById(req.getProductId()).orElse(null)
                : productRepository.findBySlug(req.getProductSlug()).orElse(null);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        int requested = req.getQty();
        int available = product.getQuantity() != null ? product.getQuantity() : 0;
        if (requested > available) {
            throw new InsufficientStockException("Insufficient stock");
        }

        int itemPriceCents = product.getPriceCents();
        int totalCents = Math.multiplyExact(itemPriceCents, requested);

        Order order = new Order();
        order.setEmail(req.getEmail());
        order.setCurrency(product.getCurrency());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalCents(totalCents);
        order.setCreatedAt(OffsetDateTime.now());
        orderRepository.save(order);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQty(requested);
        item.setPriceCents(itemPriceCents);
        orderItemRepository.save(item);

        product.setQuantity(available - requested);
        productRepository.save(product);

        return orderMapper.toCreateResponse(order);
    }
}


