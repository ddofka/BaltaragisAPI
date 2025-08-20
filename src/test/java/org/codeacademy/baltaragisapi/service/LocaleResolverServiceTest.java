package org.codeacademy.baltaragisapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for LocaleResolverService.
 */
@ExtendWith(MockitoExtension.class)
class LocaleResolverServiceTest {
    
    private LocaleResolverService localeResolverService;
    
    @BeforeEach
    void setUp() {
        localeResolverService = new LocaleResolverService();
    }
    
    @Test
    void testResolveLocale_DefaultToEnglish() {
        // Given: No request context
        RequestContextHolder.resetRequestAttributes();
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("en-US", locale);
    }
    
    @Test
    void testResolveLocale_ExplicitHeaderOverride() {
        // Given: Request with X-Locale header
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Locale", "lt-LT");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("lt-LT", locale);
    }
    
    @Test
    void testResolveLocale_ExplicitQueryParameter() {
        // Given: Request with locale query parameter
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("locale", "lt-LT");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("lt-LT", locale);
    }
    
    @Test
    void testResolveLocale_AcceptLanguageHeader() {
        // Given: Request with Accept-Language header
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "lt,en;q=0.9");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("lt-LT", locale);
    }
    
    @Test
    void testResolveLocale_AcceptLanguageHeaderEnglish() {
        // Given: Request with Accept-Language header for English
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "en,lt;q=0.9");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("en-US", locale);
    }
    
    @Test
    void testResolveLocale_CloudflareIPCountryLithuania() {
        // Given: Request with Cloudflare IP country header for Lithuania
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("CF-IPCountry", "LT");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("lt-LT", locale);
    }
    
    @Test
    void testResolveLocale_XCountryHeaderLithuania() {
        // Given: Request with X-Country header for Lithuania
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Country", "LT");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("lt-LT", locale);
    }
    
    @Test
    void testResolveLocale_CloudflareIPCountryOther() {
        // Given: Request with Cloudflare IP country header for other country
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("CF-IPCountry", "US");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then
        assertEquals("en-US", locale);
    }
    
    @Test
    void testResolveLocale_PriorityOrder() {
        // Given: Request with multiple locale indicators (should prioritize explicit override)
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Locale", "en-US");
        request.addHeader("Accept-Language", "lt,en;q=0.9");
        request.addHeader("CF-IPCountry", "LT");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // When
        String locale = localeResolverService.resolveLocale();
        
        // Then: Should use explicit override (X-Locale) over Accept-Language and IP country
        assertEquals("en-US", locale);
    }
    
    @Test
    void testGetSupportedLocales() {
        // When
        var supportedLocales = localeResolverService.getSupportedLocales();
        
        // Then
        assertTrue(supportedLocales.contains("en-US"));
        assertTrue(supportedLocales.contains("lt-LT"));
        assertEquals(2, supportedLocales.size());
    }
    
    @Test
    void testIsSupportedLocale() {
        // Then
        assertTrue(localeResolverService.isSupportedLocale("en-US"));
        assertTrue(localeResolverService.isSupportedLocale("lt-LT"));
        assertFalse(localeResolverService.isSupportedLocale("de-DE"));
        assertFalse(localeResolverService.isSupportedLocale("invalid"));
        assertFalse(localeResolverService.isSupportedLocale(null));
    }
}
