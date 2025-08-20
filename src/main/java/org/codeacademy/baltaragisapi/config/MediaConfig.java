package org.codeacademy.baltaragisapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for serving static media files.
 * Maps /media/** URLs to the local media directory for development.
 */
@Configuration
public class MediaConfig implements WebMvcConfigurer {
    
    @Value("${app.media.base-path:media}")
    private String mediaBasePath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/media/**")
                .addResourceLocations("file:" + mediaBasePath + "/")
                .setCachePeriod(3600) // Cache for 1 hour
                .resourceChain(true);
    }
}
