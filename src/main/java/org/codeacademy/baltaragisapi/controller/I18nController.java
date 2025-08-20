package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.service.TranslationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Public controller for serving internationalized translations.
 */
@RestController
@RequestMapping("/api/v1/i18n")
@Tag(name = "Internationalization", description = "Public endpoints for retrieving translations")
public class I18nController {
    
    private final TranslationService translationService;
    
    public I18nController(TranslationService translationService) {
        this.translationService = translationService;
    }
    
    /**
     * Get all translations for a specific locale.
     * 
     * @param locale The locale to get translations for
     * @return Map of translation key to value
     */
    @GetMapping("/{locale}")
    @Operation(
        summary = "Get translations for a locale",
        description = "Retrieve all translations for a specific locale as a key-value map. " +
                    "Frontend can use this to load all UI translations at once."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Translations retrieved successfully",
            content = @Content(schema = @Schema(example = "{\"common.add_to_cart\": \"Add to Cart\", \"common.loading\": \"Loading...\"}"))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Unsupported locale"
        )
    })
    public ResponseEntity<Map<String, String>> getTranslations(
            @Parameter(description = "Locale code (en-US or lt-LT)", example = "en-US")
            @PathVariable String locale) {
        
        Map<String, String> translations = translationService.getTranslationsForLocale(locale);
        return ResponseEntity.ok(translations);
    }
    
    /**
     * Get the current request's resolved locale.
     * 
     * @return The resolved locale string
     */
    @GetMapping("/current")
    @Operation(
        summary = "Get current locale",
        description = "Get the resolved locale for the current request based on IP country, " +
                    "Accept-Language header, and explicit overrides."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Current locale retrieved successfully",
            content = @Content(schema = @Schema(example = "en-US"))
        )
    })
    public ResponseEntity<String> getCurrentLocale() {
        String locale = translationService.getCurrentLocale();
        return ResponseEntity.ok(locale);
    }
    
    /**
     * Get all supported locales.
     * 
     * @return List of supported locales
     */
    @GetMapping("/locales")
    @Operation(
        summary = "Get supported locales",
        description = "Retrieve all supported locale codes."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Supported locales retrieved successfully",
            content = @Content(schema = @Schema(example = "[\"en-US\", \"lt-LT\"]"))
        )
    })
    public ResponseEntity<java.util.List<String>> getSupportedLocales() {
        java.util.List<String> locales = translationService.getAvailableLocales();
        return ResponseEntity.ok(locales);
    }
}
