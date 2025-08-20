-- Add missing navigation and home page translations for both English and Lithuanian locales
INSERT INTO translation (translation_key, locale, translation_value, updated_at) VALUES
-- Navigation keys - English
('nav.home', 'en-US', 'Home', CURRENT_TIMESTAMP),
('nav.about', 'en-US', 'About', CURRENT_TIMESTAMP),
('nav.products', 'en-US', 'Products', CURRENT_TIMESTAMP),

-- Navigation keys - Lithuanian
('nav.home', 'lt-LT', 'Pradžia', CURRENT_TIMESTAMP),
('nav.about', 'lt-LT', 'Apie', CURRENT_TIMESTAMP),
('nav.products', 'lt-LT', 'Darbai', CURRENT_TIMESTAMP),

-- Home page keys - English
('home.welcome', 'en-US', 'Welcome to Baltaragis', CURRENT_TIMESTAMP),
('home.subtitle', 'en-US', 'Contemporary artist blending traditional forms with modern techniques', CURRENT_TIMESTAMP),
('home.view_artwork', 'en-US', 'View Artwork', CURRENT_TIMESTAMP),
('home.learn_more', 'en-US', 'Learn More', CURRENT_TIMESTAMP),
('home.featured_works', 'en-US', 'Featured Works', CURRENT_TIMESTAMP),
('home.featured_description', 'en-US', 'Discover our latest collection of contemporary art pieces', CURRENT_TIMESTAMP),
('home.browse_all', 'en-US', 'Browse All Works', CURRENT_TIMESTAMP),
('home.page_title', 'en-US', 'Home', CURRENT_TIMESTAMP),

-- Home page keys - Lithuanian
('home.welcome', 'lt-LT', 'Sveiki atvykę į Baltaragis', CURRENT_TIMESTAMP),
('home.subtitle', 'lt-LT', 'Šiuolaikinis menininkas, derinantis tradicines formas su moderniaisiais metodais', CURRENT_TIMESTAMP),
('home.view_artwork', 'lt-LT', 'Peržiūrėti darbus', CURRENT_TIMESTAMP),
('home.learn_more', 'lt-LT', 'Sužinoti daugiau', CURRENT_TIMESTAMP),
('home.featured_works', 'lt-LT', 'Išrinkti darbai', CURRENT_TIMESTAMP),
('home.featured_description', 'lt-LT', 'Atraskite mūsų naujausią šiuolaikinio meno kūrinių kolekciją', CURRENT_TIMESTAMP),
('home.browse_all', 'lt-LT', 'Peržiūrėti visus darbus', CURRENT_TIMESTAMP),
('home.page_title', 'lt-LT', 'Pradžia', CURRENT_TIMESTAMP);
