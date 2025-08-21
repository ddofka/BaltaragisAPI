-- Add missing about page translation keys for both English and Lithuanian locales
INSERT INTO translation (translation_key, locale, translation_value, updated_at) VALUES
-- About page keys - English
('about.title', 'en-US', 'About', CURRENT_TIMESTAMP),
('about.page_title', 'en-US', 'About', CURRENT_TIMESTAMP),
('about.artist_statement', 'en-US', 'Artist Statement', CURRENT_TIMESTAMP),
('about.artist_statement_text', 'en-US', 'My work explores the intersection of traditional craftsmanship and contemporary design, creating pieces that honor heritage while embracing modern aesthetics.', CURRENT_TIMESTAMP),
('about.medium_style', 'en-US', 'Medium & Style', CURRENT_TIMESTAMP),
('about.medium_style_text', 'en-US', 'I work primarily with leather, combining traditional tanning techniques with innovative design approaches to create functional art pieces.', CURRENT_TIMESTAMP),
('about.contact', 'en-US', 'Contact', CURRENT_TIMESTAMP),
('about.contact_text', 'en-US', 'For inquiries about custom pieces, collaborations, or general questions, please reach out through the contact form or social media channels.', CURRENT_TIMESTAMP),

-- About page keys - Lithuanian
('about.title', 'lt-LT', 'Apie', CURRENT_TIMESTAMP),
('about.page_title', 'lt-LT', 'Apie', CURRENT_TIMESTAMP),
('about.artist_statement', 'lt-LT', 'Meno kūrėjo pareiškimas', CURRENT_TIMESTAMP),
('about.artist_statement_text', 'lt-LT', 'Mano darbai tyrinėja tradicinio meistriškumo ir šiuolaikinio dizaino sankirtą, kuriant kūrinius, kurie gerbia paveldą ir tuo pačiu priima modernią estetiką.', CURRENT_TIMESTAMP),
('about.medium_style', 'lt-LT', 'Medija ir stilius', CURRENT_TIMESTAMP),
('about.medium_style_text', 'lt-LT', 'Dirbu daugiausia su oda, derindamas tradicinius odos apdirbimo metodus su inovatyviais dizaino požiūriais, kad sukurkčiau funkcionalius meno kūrinius.', CURRENT_TIMESTAMP),
('about.contact', 'lt-LT', 'Kontaktai', CURRENT_TIMESTAMP),
('about.contact_text', 'lt-LT', 'Dėl individualių kūrinių užsakymų, bendradarbiavimo ar bendrų klausimų, prašome kreiptis per kontaktinę formą ar socialinės medijos kanalus.', CURRENT_TIMESTAMP);

