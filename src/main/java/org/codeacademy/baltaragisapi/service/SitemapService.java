package org.codeacademy.baltaragisapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeacademy.baltaragisapi.entity.Page;
import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.repository.PageRepository;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SitemapService {
    
    private final ProductRepository productRepository;
    private final PageRepository pageRepository;
    
    @Value("${app.base-url:https://www.baltaragis.com}")
    private String baseUrl;
    
    public String generateSitemapXml() {
        log.info("Generating sitemap for base URL: {}", baseUrl);
        
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n");
        
        // Add home page
        addHomePageUrl(xml);
        
        // Add products listing page
        addProductsListingUrl(xml);
        
        // Add individual product pages
        addProductUrls(xml);
        
        // Add published pages
        addPageUrls(xml);
        
        xml.append("</urlset>");
        
        log.info("Generated sitemap XML with base URL: {}", baseUrl);
        return xml.toString();
    }
    
    private void addHomePageUrl(StringBuilder xml) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(baseUrl).append("/</loc>\n");
        xml.append("    <lastmod>").append(OffsetDateTime.now().format(DateTimeFormatter.ISO_INSTANT)).append("</lastmod>\n");
        xml.append("    <changefreq>daily</changefreq>\n");
        xml.append("    <priority>1.0</priority>\n");
        addAlternateLinks(xml, baseUrl + "/", baseUrl + "/?locale=lt");
        xml.append("  </url>\n");
    }
    
    private void addProductsListingUrl(StringBuilder xml) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(baseUrl).append("/products</loc>\n");
        xml.append("    <lastmod>").append(getLatestProductUpdateTime().format(DateTimeFormatter.ISO_INSTANT)).append("</lastmod>\n");
        xml.append("    <changefreq>daily</changefreq>\n");
        xml.append("    <priority>0.9</priority>\n");
        addAlternateLinks(xml, baseUrl + "/products", baseUrl + "/products?locale=lt");
        xml.append("  </url>\n");
    }
    
    private void addProductUrls(StringBuilder xml) {
        List<Product> publishedProducts = productRepository.findByIsPublishedTrue();
        
        for (Product product : publishedProducts) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(baseUrl).append("/products/").append(product.getSlug()).append("</loc>\n");
            xml.append("    <lastmod>").append(product.getUpdatedAt().format(DateTimeFormatter.ISO_INSTANT)).append("</lastmod>\n");
            xml.append("    <changefreq>weekly</changefreq>\n");
            xml.append("    <priority>0.8</priority>\n");
            addAlternateLinks(xml, 
                baseUrl + "/products/" + product.getSlug(), 
                baseUrl + "/products/" + product.getSlug() + "?locale=lt");
            xml.append("  </url>\n");
        }
    }
    
    private void addPageUrls(StringBuilder xml) {
        List<Page> publishedPages = pageRepository.findByIsPublishedTrue();
        
        for (Page page : publishedPages) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(baseUrl).append("/pages/").append(page.getSlug()).append("</loc>\n");
            xml.append("    <lastmod>").append(page.getUpdatedAt().format(DateTimeFormatter.ISO_INSTANT)).append("</lastmod>\n");
            xml.append("    <changefreq>monthly</changefreq>\n");
            xml.append("    <priority>0.7</priority>\n");
            addAlternateLinks(xml, 
                baseUrl + "/pages/" + page.getSlug(), 
                baseUrl + "/pages/" + page.getSlug() + "?locale=lt");
            xml.append("  </url>\n");
        }
    }
    
    private void addAlternateLinks(StringBuilder xml, String enUrl, String ltUrl) {
        xml.append("    <xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"").append(enUrl).append("\"/>\n");
        xml.append("    <xhtml:link rel=\"alternate\" hreflang=\"lt\" href=\"").append(ltUrl).append("\"/>\n");
    }
    
    private OffsetDateTime getLatestProductUpdateTime() {
        return productRepository.findByIsPublishedTrue()
            .stream()
            .map(Product::getUpdatedAt)
            .max(OffsetDateTime::compareTo)
            .orElse(OffsetDateTime.now());
    }
    
    public String generateRobotsTxt() {
        StringBuilder robots = new StringBuilder();
        robots.append("User-agent: *\n");
        robots.append("Allow: /\n");
        robots.append("\n");
        robots.append("# Sitemap\n");
        robots.append("Sitemap: ").append(baseUrl).append("/sitemap.xml\n");
        
        return robots.toString();
    }
}
