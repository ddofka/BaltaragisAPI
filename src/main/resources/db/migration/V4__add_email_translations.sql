-- Add email-related translations for waitlist notifications
INSERT INTO translation (translation_key, locale, translation_value, updated_at) VALUES
-- English email translations
('email.waitlist.subject', 'en-US', 'Product back in stock!', CURRENT_TIMESTAMP),
('email.waitlist.greeting', 'en-US', 'Hello!', CURRENT_TIMESTAMP),
('email.waitlist.product_back_in_stock', 'en-US', 'The product "{0}" is back in stock!', CURRENT_TIMESTAMP),
('email.waitlist.purchase_link', 'en-US', 'You can purchase it here: {0}', CURRENT_TIMESTAMP),
('email.waitlist.regards', 'en-US', 'Best regards,', CURRENT_TIMESTAMP),
('email.waitlist.team', 'en-US', 'Baltaragis team', CURRENT_TIMESTAMP),

-- Lithuanian email translations
('email.waitlist.subject', 'lt-LT', 'Prekė vėl turima sandėlyje!', CURRENT_TIMESTAMP),
('email.waitlist.greeting', 'lt-LT', 'Sveiki!', CURRENT_TIMESTAMP),
('email.waitlist.product_back_in_stock', 'lt-LT', 'Prekė "{0}" vėl turima sandėlyje!', CURRENT_TIMESTAMP),
('email.waitlist.purchase_link', 'lt-LT', 'Galite ją įsigyti čia: {0}', CURRENT_TIMESTAMP),
('email.waitlist.regards', 'lt-LT', 'Su pagarba,', CURRENT_TIMESTAMP),
('email.waitlist.team', 'lt-LT', 'Baltaragis komanda', CURRENT_TIMESTAMP);
