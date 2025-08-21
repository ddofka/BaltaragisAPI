package org.codeacademy.baltaragisapi.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.codeacademy.baltaragisapi.config.RateLimitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    
    private final RateLimitProperties rateLimitProperties;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    public RateLimitFilter(RateLimitProperties rateLimitProperties, ObjectMapper objectMapper) {
        this.rateLimitProperties = rateLimitProperties;
        this.objectMapper = objectMapper;
    }
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Only apply rate limiting to specific endpoints
        if (!shouldRateLimit(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Skip if rate limiting is disabled
        if (!rateLimitProperties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String clientIp = getClientIp(request);
        Bucket bucket = getBucket(clientIp);
        
        if (bucket.tryConsume(1)) {
            // Request allowed
            filterChain.doFilter(request, response);
        } else {
            // Rate limit exceeded
            log.warn("Rate limit exceeded for IP: {} on endpoint: {} {}", 
                    clientIp, request.getMethod(), request.getRequestURI());
            
            sendRateLimitExceededResponse(response);
        }
    }
    
    private boolean shouldRateLimit(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        // Rate limit POST requests to orders and waitlist endpoints
        if (!"POST".equals(method)) {
            return false;
        }
        
        return uri.equals("/api/v1/orders") || 
               uri.matches("/api/v1/products/[^/]+/waitlist");
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    private Bucket getBucket(String clientIp) {
        return buckets.computeIfAbsent(clientIp, this::createBucket);
    }
    
    private Bucket createBucket(String clientIp) {
        Duration windowDuration = Duration.ofMinutes(rateLimitProperties.getWindowMinutes());
        Bandwidth bandwidth = Bandwidth.classic(rateLimitProperties.getCapacity(), 
                                               Refill.intervally(rateLimitProperties.getCapacity(), windowDuration));
        return Bucket4j.builder().addLimit(bandwidth).build();
    }
    
    private void sendRateLimitExceededResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // Calculate retry after
        long retryAfterSeconds = Duration.ofMinutes(rateLimitProperties.getWindowMinutes()).toSeconds();
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        
        // Create RFC-7807 problem details
        ProblemDetails problem = new ProblemDetails(
                "about:blank",
                "Too Many Requests",
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Rate limit exceeded. Please try again later.",
                null,
                "RATE_LIMIT_EXCEEDED",
                OffsetDateTime.now().toString(),
                null
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(problem));
    }
    
    // Inner record for problem details response
    private record ProblemDetails(String type, String title, int status, String detail, String instance,
                                  String code, String timestamp, java.util.List<java.util.Map<String, String>> errors) {}
}
