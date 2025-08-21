package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeacademy.baltaragisapi.service.SitemapService;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SEO", description = "SEO endpoints for sitemap and robots.txt")
public class SitemapController {
    
    private final SitemapService sitemapService;
    
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(
        summary = "Get sitemap.xml",
        description = "Returns the website sitemap in XML format with all published pages and products. " +
                     "Includes alternate language links for internationalization and cache headers for performance.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Sitemap XML returned successfully",
                content = @Content(
                    mediaType = MediaType.APPLICATION_XML_VALUE,
                    schema = @Schema(type = "string", example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>...")
                )
            )
        }
    )
    public ResponseEntity<String> getSitemap() {
        try {
            log.info("Generating sitemap.xml");
            
            String xmlContent = sitemapService.generateSitemapXml();
            String etag = generateETag(xmlContent);
            
            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .eTag(etag)
                .contentType(MediaType.APPLICATION_XML)
                .body(xmlContent);
                
        } catch (Exception e) {
            log.error("Error generating sitemap.xml", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
        summary = "Get robots.txt",
        description = "Returns the robots.txt file with sitemap reference and crawling directives.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Robots.txt returned successfully",
                content = @Content(
                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                    schema = @Schema(type = "string", example = "User-agent: *\\nAllow: /\\n\\nSitemap: https://www.baltaragis.com/sitemap.xml")
                )
            )
        }
    )
    public ResponseEntity<String> getRobotsTxt() {
        try {
            log.info("Generating robots.txt");
            
            String robotsContent = sitemapService.generateRobotsTxt();
            String etag = generateETag(robotsContent);
            
            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .eTag(etag)
                .contentType(MediaType.TEXT_PLAIN)
                .body(robotsContent);
                
        } catch (Exception e) {
            log.error("Error generating robots.txt", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    

    
    private String generateETag(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(content.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return "\"" + hexString.toString() + "\"";
        } catch (NoSuchAlgorithmException e) {
            log.warn("MD5 algorithm not available, using hashCode for ETag", e);
            return "\"" + Math.abs(content.hashCode()) + "\"";
        }
    }
}
