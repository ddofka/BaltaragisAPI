package org.codeacademy.baltaragisapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Set;

/**
 * Service for resolving the appropriate locale based on various factors.
 * Implements the locale negotiation order: explicit override > Accept-Language > IP country default.
 */
@Service
public class LocaleResolverService {
    
    private static final Set<String> SUPPORTED_LOCALES = Set.of("en-US", "lt-LT");
    private static final String DEFAULT_LOCALE = "en-US";
    private static final String LITHUANIA_COUNTRY_CODE = "LT";
    
    /**
     * Resolve the locale for the current request.
     * 
     * @return The resolved locale string
     */
    public String resolveLocale() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return DEFAULT_LOCALE;
        }
        
        HttpServletRequest request = attributes.getRequest();
        
        // 1. Check for explicit override (X-Locale header or query parameter)
        String explicitLocale = getExplicitLocale(request);
        if (explicitLocale != null && isValidLocale(explicitLocale)) {
            return explicitLocale;
        }
        
        // 2. Check Accept-Language header
        String acceptLanguageLocale = getAcceptLanguageLocale(request);
        if (acceptLanguageLocale != null && isValidLocale(acceptLanguageLocale)) {
            return acceptLanguageLocale;
        }
        
        // 3. Default based on IP country
        return getDefaultLocaleByCountry(request);
    }
    
    /**
     * Get explicit locale from X-Locale header or query parameter.
     * 
     * @param request The HTTP request
     * @return The explicit locale or null if not specified
     */
    private String getExplicitLocale(HttpServletRequest request) {
        // Check X-Locale header first
        String headerLocale = request.getHeader("X-Locale");
        if (headerLocale != null && !headerLocale.trim().isEmpty()) {
            return headerLocale.trim();
        }
        
        // Check query parameter
        String queryLocale = request.getParameter("locale");
        if (queryLocale != null && !queryLocale.trim().isEmpty()) {
            return queryLocale.trim();
        }
        
        return null;
    }
    
    /**
     * Get locale from Accept-Language header.
     * 
     * @param request The HTTP request
     * @return The locale from Accept-Language or null if not specified
     */
    private String getAcceptLanguageLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        if (acceptLanguage == null || acceptLanguage.trim().isEmpty()) {
            return null;
        }
        
        // Parse Accept-Language header and find the best match
        String[] languages = acceptLanguage.split(",");
        for (String language : languages) {
            String[] parts = language.split(";");
            String locale = parts[0].trim();
            
            // Convert simple locale to full locale if needed
            if (locale.equals("lt") || locale.startsWith("lt-")) {
                return "lt-LT";
            } else if (locale.equals("en") || locale.startsWith("en-")) {
                return "en-US";
            }
        }
        
        return null;
    }
    
    /**
     * Get default locale based on IP country.
     * 
     * @param request The HTTP request
     * @return The default locale based on country
     */
    private String getDefaultLocaleByCountry(HttpServletRequest request) {
        // Check Cloudflare IP country header
        String cfCountry = request.getHeader("CF-IPCountry");
        if (LITHUANIA_COUNTRY_CODE.equals(cfCountry)) {
            return "lt-LT";
        }
        
        // Check X-Country header (for development/testing)
        String xCountry = request.getHeader("X-Country");
        if (LITHUANIA_COUNTRY_CODE.equals(xCountry)) {
            return "lt-LT";
        }
        
        // Default to English
        return DEFAULT_LOCALE;
    }
    
    /**
     * Check if a locale string is valid and supported.
     * 
     * @param locale The locale to validate
     * @return true if the locale is valid and supported
     */
    private boolean isValidLocale(String locale) {
        return locale != null && SUPPORTED_LOCALES.contains(locale);
    }
    
    /**
     * Get all supported locales.
     * 
     * @return Set of supported locale strings
     */
    public Set<String> getSupportedLocales() {
        return SUPPORTED_LOCALES;
    }
    
    /**
     * Check if a locale is supported.
     * 
     * @param locale The locale to check
     * @return true if the locale is supported
     */
    public boolean isSupportedLocale(String locale) {
        return locale != null && SUPPORTED_LOCALES.contains(locale);
    }
}
