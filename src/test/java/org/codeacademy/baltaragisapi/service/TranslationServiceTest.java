package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.entity.Translation;
import org.codeacademy.baltaragisapi.exception.ValidationException;
import org.codeacademy.baltaragisapi.repository.TranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test for TranslationService.
 */
@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {
    
    @Mock
    private TranslationRepository translationRepository;
    
    @Mock
    private LocaleResolverService localeResolverService;
    
    private TranslationService translationService;
    
    @BeforeEach
    void setUp() {
        translationService = new TranslationService(translationRepository, localeResolverService);
    }
    
    @Test
    void testGetTranslationsForLocale_Success() {
        // Given
        String locale = "en-US";
        List<Object[]> keyValuePairs = Arrays.asList(
            new Object[]{"common.add_to_cart", "Add to Cart"},
            new Object[]{"common.loading", "Loading..."}
        );
        
        when(translationRepository.findKeyValuePairsByLocale(locale)).thenReturn(keyValuePairs);
        when(localeResolverService.isSupportedLocale(locale)).thenReturn(true);
        
        // When
        Map<String, String> translations = translationService.getTranslationsForLocale(locale);
        
        // Then
        assertEquals(2, translations.size());
        assertEquals("Add to Cart", translations.get("common.add_to_cart"));
        assertEquals("Loading...", translations.get("common.loading"));
        
        verify(translationRepository).findKeyValuePairsByLocale(locale);
    }
    
    @Test
    void testGetTranslationsForLocale_UnsupportedLocale() {
        // Given
        String locale = "de-DE";
        when(localeResolverService.isSupportedLocale(locale)).thenReturn(false);
        
        // When & Then
        assertThrows(ValidationException.class, () -> 
            translationService.getTranslationsForLocale(locale)
        );
        
        verify(translationRepository, never()).findKeyValuePairsByLocale(any());
    }
    
    @Test
    void testGetTranslation_Success() {
        // Given
        String key = "common.add_to_cart";
        String locale = "en-US";
        Translation translation = new Translation();
        translation.setValue("Add to Cart");
        
        when(translationRepository.findByKeyAndLocale(key, locale)).thenReturn(Optional.of(translation));
        when(localeResolverService.isSupportedLocale(locale)).thenReturn(true);
        
        // When
        String result = translationService.getTranslation(key, locale);
        
        // Then
        assertEquals("Add to Cart", result);
        verify(translationRepository).findByKeyAndLocale(key, locale);
    }
    
    @Test
    void testGetTranslation_NotFound() {
        // Given
        String key = "common.nonexistent";
        String locale = "en-US";
        
        when(translationRepository.findByKeyAndLocale(key, locale)).thenReturn(Optional.empty());
        when(localeResolverService.isSupportedLocale(locale)).thenReturn(true);
        
        // When
        String result = translationService.getTranslation(key, locale);
        
        // Then: Should fallback to key
        assertEquals(key, result);
    }
    
    @Test
    void testGetTranslation_CurrentLocale() {
        // Given
        String key = "common.add_to_cart";
        String currentLocale = "lt-LT";
        Translation translation = new Translation();
        translation.setValue("Pridėti į krepšelį");
        
        when(localeResolverService.resolveLocale()).thenReturn(currentLocale);
        when(translationRepository.findByKeyAndLocale(key, currentLocale)).thenReturn(Optional.of(translation));
        when(localeResolverService.isSupportedLocale(currentLocale)).thenReturn(true);
        
        // When
        String result = translationService.getTranslation(key);
        
        // Then
        assertEquals("Pridėti į krepšelį", result);
        verify(localeResolverService).resolveLocale();
    }
    
    @Test
    void testUpsertTranslation_CreateNew() {
        // Given
        String key = "common.new_key";
        String locale = "en-US";
        String value = "New Value";
        
        Translation savedTranslation = new Translation();
        savedTranslation.setId(1L);
        savedTranslation.setKey(key);
        savedTranslation.setLocale(locale);
        savedTranslation.setValue(value);
        savedTranslation.setUpdatedAt(OffsetDateTime.now());
        
        when(localeResolverService.isSupportedLocale(locale)).thenReturn(true);
        when(translationRepository.findByKeyAndLocale(key, locale)).thenReturn(Optional.empty());
        when(translationRepository.save(any(Translation.class))).thenReturn(savedTranslation);
        
        // When
        Translation result = translationService.upsertTranslation(key, locale, value);
        
        // Then
        assertNotNull(result);
        assertEquals(key, result.getKey());
        assertEquals(locale, result.getLocale());
        assertEquals(value, result.getValue());
        
        verify(translationRepository).save(any(Translation.class));
    }
    
    @Test
    void testUpsertTranslation_UpdateExisting() {
        // Given
        String key = "common.existing_key";
        String locale = "en-US";
        String newValue = "Updated Value";
        
        Translation existingTranslation = new Translation();
        existingTranslation.setId(1L);
        existingTranslation.setKey(key);
        existingTranslation.setLocale(locale);
        existingTranslation.setValue("Old Value");
        
        when(localeResolverService.isSupportedLocale(locale)).thenReturn(true);
        when(translationRepository.findByKeyAndLocale(key, locale)).thenReturn(Optional.of(existingTranslation));
        when(translationRepository.save(any(Translation.class))).thenReturn(existingTranslation);
        
        // When
        Translation result = translationService.upsertTranslation(key, locale, newValue);
        
        // Then
        assertNotNull(result);
        assertEquals(newValue, result.getValue());
        
        verify(translationRepository).save(existingTranslation);
    }
    
    @Test
    void testUpsertTranslation_ValidationErrors() {
        // Given
        when(localeResolverService.isSupportedLocale("en-US")).thenReturn(true);
        when(localeResolverService.isSupportedLocale("invalid")).thenReturn(false);
        
        // When & Then
        assertThrows(ValidationException.class, () -> 
            translationService.upsertTranslation("", "en-US", "value")
        );
        
        assertThrows(ValidationException.class, () -> 
            translationService.upsertTranslation("key", "invalid", "value")
        );
        
        assertThrows(ValidationException.class, () -> 
            translationService.upsertTranslation("key", "en-US", "")
        );
        
        verify(translationRepository, never()).save(any());
    }
    
    @Test
    void testBulkUpsertTranslations_Success() {
        // Given
        Map<String, Map<String, String>> translations = new HashMap<>();
        translations.put("en-US", Map.of("key1", "value1", "key2", "value2"));
        translations.put("lt-LT", Map.of("key1", "vertimas1", "key2", "vertimas2"));
        
        when(localeResolverService.isSupportedLocale("en-US")).thenReturn(true);
        when(localeResolverService.isSupportedLocale("lt-LT")).thenReturn(true);
        when(translationRepository.findByKeyAndLocale(anyString(), anyString())).thenReturn(Optional.empty());
        when(translationRepository.save(any(Translation.class))).thenAnswer(invocation -> {
            Translation t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });
        
        // When
        List<Translation> results = translationService.bulkUpsertTranslations(translations);
        
        // Then
        assertEquals(4, results.size());
        verify(translationRepository, times(4)).save(any(Translation.class));
    }
    
    @Test
    void testGetCurrentLocale() {
        // Given
        String expectedLocale = "lt-LT";
        when(localeResolverService.resolveLocale()).thenReturn(expectedLocale);
        
        // When
        String result = translationService.getCurrentLocale();
        
        // Then
        assertEquals(expectedLocale, result);
        verify(localeResolverService).resolveLocale();
    }
}
