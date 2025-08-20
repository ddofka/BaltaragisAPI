package org.codeacademy.baltaragisapi.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.time.Duration;

@Configuration
public class CacheConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        
        // Product listing - shorter cache as it changes more frequently
        interceptor.addCacheMapping(
            CacheControl.maxAge(Duration.ofSeconds(60)).cachePublic(),
            "/api/v1/products"
        );
        
        // Product detail - longer cache as individual products change less frequently
        interceptor.addCacheMapping(
            CacheControl.maxAge(Duration.ofSeconds(300)).cachePublic(),
            "/api/v1/products/*"
        );
        
        // Pages - medium cache as content might be updated occasionally
        interceptor.addCacheMapping(
            CacheControl.maxAge(Duration.ofSeconds(180)).cachePublic(),
            "/api/v1/pages/*"
        );
        
        // Artist profile - longer cache as it changes very rarely
        interceptor.addCacheMapping(
            CacheControl.maxAge(Duration.ofSeconds(600)).cachePublic(),
            "/api/v1/artist"
        );
        
        registry.addInterceptor(interceptor);
    }
}
