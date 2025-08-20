-- Add translations table for internationalization
CREATE TABLE translation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    translation_key VARCHAR(255) NOT NULL,
    locale VARCHAR(10) NOT NULL,
    translation_value TEXT NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Ensure unique key-locale combinations
ALTER TABLE translation ADD CONSTRAINT uk_translation_key_locale UNIQUE (translation_key, locale);

-- Index for efficient locale-based queries
CREATE INDEX idx_translation_locale ON translation (locale);

-- Insert some initial translations for common UI elements
INSERT INTO translation (translation_key, locale, translation_value, updated_at) VALUES
-- English translations
('common.add_to_cart', 'en-US', 'Add to Cart', CURRENT_TIMESTAMP),
('common.loading', 'en-US', 'Loading...', CURRENT_TIMESTAMP),
('common.error', 'en-US', 'Error', CURRENT_TIMESTAMP),
('common.success', 'en-US', 'Success', CURRENT_TIMESTAMP),
('common.cancel', 'en-US', 'Cancel', CURRENT_TIMESTAMP),
('common.save', 'en-US', 'Save', CURRENT_TIMESTAMP),
('common.delete', 'en-US', 'Delete', CURRENT_TIMESTAMP),
('common.edit', 'en-US', 'Edit', CURRENT_TIMESTAMP),
('common.back', 'en-US', 'Back', CURRENT_TIMESTAMP),
('common.next', 'en-US', 'Next', CURRENT_TIMESTAMP),
('common.previous', 'en-US', 'Previous', CURRENT_TIMESTAMP),
('common.search', 'en-US', 'Search', CURRENT_TIMESTAMP),
('common.filter', 'en-US', 'Filter', CURRENT_TIMESTAMP),
('common.sort', 'en-US', 'Sort', CURRENT_TIMESTAMP),
('common.view', 'en-US', 'View', CURRENT_TIMESTAMP),

-- Lithuanian translations
('common.add_to_cart', 'lt-LT', 'Pridėti į krepšelį', CURRENT_TIMESTAMP),
('common.loading', 'lt-LT', 'Kraunama...', CURRENT_TIMESTAMP),
('common.error', 'lt-LT', 'Klaida', CURRENT_TIMESTAMP),
('common.success', 'lt-LT', 'Sėkmingai', CURRENT_TIMESTAMP),
('common.cancel', 'lt-LT', 'Atšaukti', CURRENT_TIMESTAMP),
('common.save', 'lt-LT', 'Išsaugoti', CURRENT_TIMESTAMP),
('common.delete', 'lt-LT', 'Ištrinti', CURRENT_TIMESTAMP),
('common.edit', 'lt-LT', 'Redaguoti', CURRENT_TIMESTAMP),
('common.back', 'lt-LT', 'Atgal', CURRENT_TIMESTAMP),
('common.next', 'lt-LT', 'Kitas', CURRENT_TIMESTAMP),
('common.previous', 'lt-LT', 'Ankstesnis', CURRENT_TIMESTAMP),
('common.search', 'lt-LT', 'Ieškoti', CURRENT_TIMESTAMP),
('common.filter', 'lt-LT', 'Filtruoti', CURRENT_TIMESTAMP),
('common.sort', 'lt-LT', 'Rūšiuoti', CURRENT_TIMESTAMP),
('common.view', 'lt-LT', 'Peržiūrėti', CURRENT_TIMESTAMP);
