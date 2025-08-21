package org.codeacademy.baltaragisapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class SitemapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSitemap_shouldReturnValidXml() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(header().exists("Cache-Control"))
                .andExpect(header().exists("ETag"))
                .andExpect(content().string(containsString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")))
                .andExpect(content().string(containsString("<urlset")))
                .andExpect(content().string(containsString("xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"")))
                .andExpect(content().string(containsString("xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"")));
    }

    @Test
    void getSitemap_shouldIncludeHomePageUrl() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<loc>http://localhost:8080/</loc>")))
                .andExpect(content().string(containsString("<priority>1.0</priority>")))
                .andExpect(content().string(containsString("<changefreq>daily</changefreq>")));
    }

    @Test
    void getSitemap_shouldIncludeProductsListingUrl() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<loc>http://localhost:8080/products</loc>")))
                .andExpect(content().string(containsString("<priority>0.9</priority>")))
                .andExpect(content().string(containsString("<changefreq>daily</changefreq>")));
    }

    @Test
    void getSitemap_shouldIncludePublishedProducts() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                // Should include published products from seed data
                .andExpect(content().string(containsString("/products/")))
                .andExpect(content().string(containsString("<priority>0.8</priority>")))
                .andExpect(content().string(containsString("<changefreq>weekly</changefreq>")));
    }

    @Test
    void getSitemap_shouldIncludeAlternateLanguageLinks() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("hreflang=\"en\"")))
                .andExpect(content().string(containsString("hreflang=\"lt\"")))
                .andExpect(content().string(containsString("rel=\"alternate\"")));
    }

    @Test
    void getSitemap_shouldIncludePublishedPages() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                // Should include published pages from seed data
                .andExpect(content().string(containsString("/pages/")))
                .andExpect(content().string(containsString("<priority>0.7</priority>")))
                .andExpect(content().string(containsString("<changefreq>monthly</changefreq>")));
    }

    @Test
    void getRobotsTxt_shouldReturnValidRobotsTxt() throws Exception {
        mockMvc.perform(get("/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(header().exists("Cache-Control"))
                .andExpect(header().exists("ETag"))
                .andExpect(content().string(containsString("User-agent: *")))
                .andExpect(content().string(containsString("Allow: /")))
                .andExpect(content().string(containsString("Sitemap: http://localhost:8080/sitemap.xml")));
    }

    @Test
    void getSitemap_shouldHaveCacheHeaders() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", containsString("max-age=300")))
                .andExpect(header().string("Cache-Control", containsString("public")))
                .andExpect(header().exists("ETag"));
    }

    @Test
    void getRobotsTxt_shouldHaveCacheHeaders() throws Exception {
        mockMvc.perform(get("/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", containsString("max-age=3600")))
                .andExpect(header().string("Cache-Control", containsString("public")))
                .andExpect(header().exists("ETag"));
    }
}
