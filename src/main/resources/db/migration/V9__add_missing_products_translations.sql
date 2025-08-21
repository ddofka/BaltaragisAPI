-- Add missing products page translation keys for both English and Lithuanian locales
INSERT INTO translation (translation_key, locale, translation_value, updated_at) VALUES
-- Products page specific keys - English
('products.intro', 'en-US', 'Explore our collection of unique artworks and handcrafted accessories, each piece telling its own story through traditional craftsmanship and contemporary design.', CURRENT_TIMESTAMP),
('products.sunset_print', 'en-US', 'Sunset Print', CURRENT_TIMESTAMP),
('products.a3_giclee', 'en-US', 'A3 Giclée Print', CURRENT_TIMESTAMP),
('products.more_coming_soon', 'en-US', 'More pieces coming soon', CURRENT_TIMESTAMP),
('products.additional_pieces', 'en-US', 'Additional pieces are being prepared and will be available shortly.', CURRENT_TIMESTAMP),
('products.cta_text', 'en-US', 'Interested in a custom piece? Contact us to discuss your vision.', CURRENT_TIMESTAMP),
('products.page_title', 'en-US', 'Products', CURRENT_TIMESTAMP),

-- Products page specific keys - Lithuanian
('products.intro', 'lt-LT', 'Atraskite mūsų unikalių meno kūrinių ir rankomis pagamintų aksesuarų kolekciją, kur kiekvienas kūrinys pasakoja savo istoriją per tradicinį meistriškumą ir šiuolaikinį dizainą.', CURRENT_TIMESTAMP),
('products.sunset_print', 'lt-LT', 'Saulėlydžio Spaudinys', CURRENT_TIMESTAMP),
('products.a3_giclee', 'lt-LT', 'A3 Giclée Spaudinys', CURRENT_TIMESTAMP),
('products.more_coming_soon', 'lt-LT', 'Daugiau kūrinių netrukus', CURRENT_TIMESTAMP),
('products.additional_pieces', 'lt-LT', 'Papildomi kūriniai ruošiami ir bus prieinami netrukus.', CURRENT_TIMESTAMP),
('products.cta_text', 'lt-LT', 'Domina individualus kūrinys? Susisiekite su mumis, kad aptartumėte savo viziją.', CURRENT_TIMESTAMP),
('products.page_title', 'lt-LT', 'Darbai', CURRENT_TIMESTAMP);

