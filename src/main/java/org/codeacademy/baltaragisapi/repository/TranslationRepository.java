package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository for Translation entities.
 */
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    
    /**
     * Find all translations for a specific locale.
     * 
     * @param locale The locale to find translations for
     * @return List of translations for the locale
     */
    List<Translation> findByLocaleOrderByKeyAsc(String locale);
    
    /**
     * Find a specific translation by key and locale.
     * 
     * @param key The translation key
     * @param locale The locale
     * @return Optional containing the translation if found
     */
    Optional<Translation> findByKeyAndLocale(String key, String locale);
    
    /**
     * Check if a translation exists for a key and locale.
     * 
     * @param key The translation key
     * @param locale The locale
     * @return true if translation exists
     */
    boolean existsByKeyAndLocale(String key, String locale);
    
    /**
     * Find all translations for a specific key across all locales.
     * 
     * @param key The translation key
     * @return List of translations for the key
     */
    List<Translation> findByKeyOrderByLocaleAsc(String key);
    
    /**
     * Get all available locales.
     * 
     * @return List of unique locales
     */
    @Query("SELECT DISTINCT t.locale FROM Translation t ORDER BY t.locale")
    List<String> findAllLocales();
    
    /**
     * Get translations as a key-value map for a specific locale.
     * 
     * @param locale The locale
     * @return Map of translation key to value
     */
    @Query("SELECT t.key, t.value FROM Translation t WHERE t.locale = :locale ORDER BY t.key")
    List<Object[]> findKeyValuePairsByLocale(@Param("locale") String locale);
}
