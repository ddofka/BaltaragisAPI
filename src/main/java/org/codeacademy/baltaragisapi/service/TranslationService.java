package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.entity.Translation;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.exception.ValidationException;
import org.codeacademy.baltaragisapi.repository.TranslationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing internationalized translations.
 */
@Service
@Transactional
public class TranslationService {
    
    private final TranslationRepository translationRepository;
    private final LocaleResolverService localeResolverService;
    
    public TranslationService(TranslationRepository translationRepository, 
                            LocaleResolverService localeResolverService) {
        this.translationRepository = translationRepository;
        this.localeResolverService = localeResolverService;
    }
    
    /**
     * Get all translations for a specific locale as a key-value map.
     * 
     * @param locale The locale to get translations for
     * @return Map of translation key to value
     */
    public Map<String, String> getTranslationsForLocale(String locale) {
        if (!localeResolverService.isSupportedLocale(locale)) {
            throw new ValidationException("Unsupported locale: " + locale, null);
        }
        
        List<Translation> translations = translationRepository.findByLocaleOrderByKeyAsc(locale);
        Map<String, String> result = new HashMap<>();
        
        for (Translation translation : translations) {
            result.put(translation.getKey(), translation.getValue());
        }
        
        return result;
    }
    
    /**
     * Get a specific translation by key and locale.
     * 
     * @param key The translation key
     * @param locale The locale
     * @return The translation value
     */
    public String getTranslation(String key, String locale) {
        if (!localeResolverService.isSupportedLocale(locale)) {
            throw new ValidationException("Unsupported locale: " + locale, null);
        }
        
        Optional<Translation> translation = translationRepository.findByKeyAndLocale(key, locale);
        return translation.map(Translation::getValue).orElse(key); // Fallback to key if not found
    }
    
    /**
     * Get a translation for the current request's locale.
     * 
     * @param key The translation key
     * @return The translation value for the current locale
     */
    public String getTranslation(String key) {
        String locale = localeResolverService.resolveLocale();
        return getTranslation(key, locale);
    }
    
    /**
     * Create or update a translation.
     * 
     * @param key The translation key
     * @param locale The locale
     * @param value The translation value
     * @return The created or updated translation
     */
    public Translation upsertTranslation(String key, String locale, String value) {
        if (!localeResolverService.isSupportedLocale(locale)) {
            throw new ValidationException("Unsupported locale: " + locale, null);
        }
        
        if (key == null || key.trim().isEmpty()) {
            throw new ValidationException("Translation key is required", null);
        }
        
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Translation value is required", null);
        }
        
        Optional<Translation> existing = translationRepository.findByKeyAndLocale(key, locale);
        
        if (existing.isPresent()) {
            Translation translation = existing.get();
            translation.setValue(value.trim());
            return translationRepository.save(translation);
        } else {
            Translation translation = new Translation();
            translation.setKey(key.trim());
            translation.setLocale(locale);
            translation.setValue(value.trim());
            return translationRepository.save(translation);
        }
    }
    
    /**
     * Delete a translation.
     * 
     * @param key The translation key
     * @param locale The locale
     */
    public void deleteTranslation(String key, String locale) {
        if (!localeResolverService.isSupportedLocale(locale)) {
            throw new ValidationException("Unsupported locale: " + locale, null);
        }
        
        Optional<Translation> translation = translationRepository.findByKeyAndLocale(key, locale);
        if (translation.isPresent()) {
            translationRepository.delete(translation.get());
        }
    }
    
    /**
     * Get all translations for a specific key across all locales.
     * 
     * @param key The translation key
     * @return List of translations for the key
     */
    public List<Translation> getTranslationsForKey(String key) {
        return translationRepository.findByKeyOrderByLocaleAsc(key);
    }
    
    /**
     * Get all available locales.
     * 
     * @return List of available locales
     */
    public List<String> getAvailableLocales() {
        return translationRepository.findAllLocales();
    }
    
    /**
     * Bulk upsert translations.
     * 
     * @param translations Map of locale to key-value pairs
     * @return List of created/updated translations
     */
    public List<Translation> bulkUpsertTranslations(Map<String, Map<String, String>> translations) {
        List<Translation> results = new java.util.ArrayList<>();
        
        for (Map.Entry<String, Map<String, String>> localeEntry : translations.entrySet()) {
            String locale = localeEntry.getKey();
            
            if (!localeResolverService.isSupportedLocale(locale)) {
                throw new ValidationException("Unsupported locale: " + locale, null);
            }
            
            Map<String, String> keyValuePairs = localeEntry.getValue();
            for (Map.Entry<String, String> keyValueEntry : keyValuePairs.entrySet()) {
                String key = keyValueEntry.getKey();
                String value = keyValueEntry.getValue();
                
                Translation translation = upsertTranslation(key, locale, value);
                results.add(translation);
            }
        }
        
        return results;
    }
    
    /**
     * Get the current request's locale.
     * 
     * @return The resolved locale string
     */
    public String getCurrentLocale() {
        return localeResolverService.resolveLocale();
    }
}
