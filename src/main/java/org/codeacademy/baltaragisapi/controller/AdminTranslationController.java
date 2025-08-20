package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.dto.admin.BulkTranslationRequest;
import org.codeacademy.baltaragisapi.dto.admin.CreateTranslationRequest;
import org.codeacademy.baltaragisapi.dto.admin.TranslationResponse;
import org.codeacademy.baltaragisapi.entity.Translation;
import org.codeacademy.baltaragisapi.service.TranslationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin controller for managing internationalized translations.
 */
@RestController
@RequestMapping("/api/v1/admin/translations")
@Tag(name = "Admin: Translations", description = "Admin endpoints for managing translations")
public class AdminTranslationController {
    
    private final TranslationService translationService;
    
    public AdminTranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }
    
    /**
     * Create or update a translation.
     * 
     * @param request The translation request
     * @return The created or updated translation
     */
    @PostMapping
    @Operation(
        summary = "Create or update a translation",
        description = "Create a new translation or update an existing one. " +
                    "If a translation with the same key and locale exists, it will be updated."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Translation created/updated successfully",
            content = @Content(schema = @Schema(implementation = TranslationResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
        )
    })
    public ResponseEntity<TranslationResponse> upsertTranslation(
            @RequestBody CreateTranslationRequest request) {
        
        Translation translation = translationService.upsertTranslation(
            request.getKey(), request.getLocale(), request.getValue()
        );
        
        TranslationResponse response = TranslationResponse.builder()
                .id(translation.getId())
                .key(translation.getKey())
                .locale(translation.getLocale())
                .value(translation.getValue())
                .updatedAt(translation.getUpdatedAt())
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get all translations for a specific locale.
     * 
     * @param locale The locale to get translations for
     * @return List of translations
     */
    @GetMapping("/locale/{locale}")
    @Operation(
        summary = "Get translations by locale",
        description = "Retrieve all translations for a specific locale."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Translations retrieved successfully",
            content = @Content(schema = @Schema(implementation = TranslationResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Unsupported locale"
        )
    })
    public ResponseEntity<List<TranslationResponse>> getTranslationsByLocale(
            @Parameter(description = "Locale code (en-US or lt-LT)", example = "en-US")
            @PathVariable String locale) {
        
        List<Translation> translations = translationService.getTranslationsForLocale(locale)
                .entrySet().stream()
                .map(entry -> {
                    // Create a temporary Translation object for the response
                    Translation temp = new Translation();
                    temp.setKey(entry.getKey());
                    temp.setValue(entry.getValue());
                    temp.setLocale(locale);
                    return temp;
                })
                .collect(Collectors.toList());
        
        List<TranslationResponse> responses = translations.stream()
                .map(t -> TranslationResponse.builder()
                        .key(t.getKey())
                        .locale(t.getLocale())
                        .value(t.getValue())
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Get all translations for a specific key across all locales.
     * 
     * @param key The translation key
     * @return List of translations for the key
     */
    @GetMapping("/key/{key}")
    @Operation(
        summary = "Get translations by key",
        description = "Retrieve all translations for a specific key across all locales."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Translations retrieved successfully",
            content = @Content(schema = @Schema(implementation = TranslationResponse.class))
        )
    })
    public ResponseEntity<List<TranslationResponse>> getTranslationsByKey(
            @Parameter(description = "Translation key", example = "common.add_to_cart")
            @PathVariable String key) {
        
        List<Translation> translations = translationService.getTranslationsForKey(key);
        
        List<TranslationResponse> responses = translations.stream()
                .map(t -> TranslationResponse.builder()
                        .id(t.getId())
                        .key(t.getKey())
                        .locale(t.getLocale())
                        .value(t.getValue())
                        .updatedAt(t.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Delete a translation.
     * 
     * @param key The translation key
     * @param locale The locale
     * @return No content response
     */
    @DeleteMapping("/{key}/{locale}")
    @Operation(
        summary = "Delete a translation",
        description = "Delete a specific translation by key and locale."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Translation deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Unsupported locale"
        )
    })
    public ResponseEntity<Void> deleteTranslation(
            @Parameter(description = "Translation key", example = "common.add_to_cart")
            @PathVariable String key,
            
            @Parameter(description = "Locale code (en-US or lt-LT)", example = "en-US")
            @PathVariable String locale) {
        
        translationService.deleteTranslation(key, locale);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Bulk upsert translations.
     * 
     * @param request The bulk translation request
     * @return List of created/updated translations
     */
    @PostMapping("/bulk")
    @Operation(
        summary = "Bulk upsert translations",
        description = "Create or update multiple translations in a single request. " +
                    "Useful for importing translation files or bulk updates."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Translations created/updated successfully",
            content = @Content(schema = @Schema(implementation = TranslationResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
        )
    })
    public ResponseEntity<List<TranslationResponse>> bulkUpsertTranslations(
            @RequestBody BulkTranslationRequest request) {
        
        List<Translation> translations = translationService.bulkUpsertTranslations(request.getTranslations());
        
        List<TranslationResponse> responses = translations.stream()
                .map(t -> TranslationResponse.builder()
                        .id(t.getId())
                        .key(t.getKey())
                        .locale(t.getLocale())
                        .value(t.getValue())
                        .updatedAt(t.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
    
    /**
     * Get all available locales.
     * 
     * @return List of available locales
     */
    @GetMapping("/locales")
    @Operation(
        summary = "Get available locales",
        description = "Retrieve all locales that have translations."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Locales retrieved successfully",
            content = @Content(schema = @Schema(example = "[\"en-US\", \"lt-LT\"]"))
        )
    })
    public ResponseEntity<List<String>> getAvailableLocales() {
        List<String> locales = translationService.getAvailableLocales();
        return ResponseEntity.ok(locales);
    }
}
