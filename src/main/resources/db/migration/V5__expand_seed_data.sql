-- Expand seed data to include 9 products with variety for UI testing

-- First, ensure we have the basic seed data (artist profile and pages)
INSERT INTO artist_profile (name, bio, hero_image_url, socials, created_at, updated_at)
SELECT 'Baltaragis', 'Contemporary artist blending traditional forms.', 'https://example.com/hero.jpg',
       '{"instagram":"https://instagram.com/baltaragis","twitter":"https://x.com/baltaragis"}',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM artist_profile WHERE name = 'Baltaragis');

INSERT INTO page (title, slug, content_md, is_published, created_at, updated_at)
SELECT 'Home', 'home', '# Welcome to Baltaragis', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM page WHERE slug = 'home');

INSERT INTO page (title, slug, content_md, is_published, created_at, updated_at)
SELECT 'About', 'about', 'About the artist...', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM page WHERE slug = 'about');

-- Update existing products to ensure they have proper data
UPDATE product SET 
  short_desc = 'A3 giclée print',
  long_desc = 'High-quality giclée print of a sunset.',
  price_cents = 4500,
  currency = 'EUR',
  quantity = 10,
  is_published = TRUE
WHERE slug = 'sunset-print';

UPDATE product SET 
  short_desc = 'Graphite on paper',
  long_desc = 'Original graphite sketch of a forest scene.',
  price_cents = 12000,
  currency = 'EUR',
  quantity = 0,
  is_published = TRUE
WHERE slug = 'forest-sketch';

UPDATE product SET 
  short_desc = 'Ink on paper',
  long_desc = 'Study of ocean waves in ink.',
  price_cents = 8000,
  currency = 'EUR',
  quantity = 3,
  is_published = TRUE
WHERE slug = 'ocean-study';

-- Add new products if they don't exist
INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
SELECT 'Leather Wallet', 'leather-wallet', 'Handcrafted leather wallet', 'Premium leather wallet with multiple card slots and coin pocket.', 8500, 'EUR', 5, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM product WHERE slug = 'leather-wallet');

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
SELECT 'Cardholder', 'cardholder', 'Minimalist cardholder', 'Slim leather cardholder perfect for everyday use.', 3500, 'EUR', 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM product WHERE slug = 'cardholder');

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
SELECT 'Key Fob', 'key-fob', 'Artistic key fob', 'Handmade key fob with unique design.', 2500, 'EUR', 15, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM product WHERE slug = 'key-fob');

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
SELECT 'Leather Belt', 'leather-belt', 'Classic leather belt', 'Timeless leather belt with brass buckle.', 12000, 'EUR', 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM product WHERE slug = 'leather-belt');

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
SELECT 'Phone Case', 'phone-case', 'Custom phone case', 'Personalized phone case with artistic design.', 5500, 'EUR', 8, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM product WHERE slug = 'phone-case');

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
SELECT 'Notebook', 'notebook', 'Artisan notebook', 'Hand-bound notebook with quality paper.', 4000, 'EUR', 12, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM product WHERE slug = 'notebook');

-- Clear existing photos and re-insert them
DELETE FROM product_photo;

-- Insert photos for all products
INSERT INTO product_photo (product_id, url, alt, sort_order, width, height)
SELECT p.id, 'https://example.com/photos/sunset1.jpg', 'Sunset Print main', 1, 1200, 800
FROM product p WHERE p.slug = 'sunset-print'
UNION ALL
SELECT p.id, 'https://example.com/photos/sunset2.jpg', 'Sunset Print detail', 2, 1200, 800
FROM product p WHERE p.slug = 'sunset-print'
UNION ALL
SELECT p.id, 'https://example.com/photos/sunset3.jpg', 'Sunset Print framed', 3, 1200, 800
FROM product p WHERE p.slug = 'sunset-print'

UNION ALL

SELECT p.id, 'https://example.com/photos/forest1.jpg', 'Forest Sketch main', 1, 1200, 800
FROM product p WHERE p.slug = 'forest-sketch'
UNION ALL
SELECT p.id, 'https://example.com/photos/forest2.jpg', 'Forest Sketch detail', 2, 1200, 800
FROM product p WHERE p.slug = 'forest-sketch'

UNION ALL

SELECT p.id, 'https://example.com/photos/ocean1.jpg', 'Ocean Study main', 1, 1200, 800
FROM product p WHERE p.slug = 'ocean-study'
UNION ALL
SELECT p.id, 'https://example.com/photos/ocean2.jpg', 'Ocean Study detail', 2, 1200, 800
FROM product p WHERE p.slug = 'ocean-study'

UNION ALL

SELECT p.id, 'https://example.com/photos/wallet1.jpg', 'Leather Wallet front', 1, 1200, 800
FROM product p WHERE p.slug = 'leather-wallet'
UNION ALL
SELECT p.id, 'https://example.com/photos/wallet2.jpg', 'Leather Wallet back', 2, 1200, 800
FROM product p WHERE p.slug = 'leather-wallet'
UNION ALL
SELECT p.id, 'https://example.com/photos/wallet3.jpg', 'Leather Wallet open', 3, 1200, 800
FROM product p WHERE p.slug = 'leather-wallet'

UNION ALL

SELECT p.id, 'https://example.com/photos/cardholder1.jpg', 'Cardholder front', 1, 1200, 800
FROM product p WHERE p.slug = 'cardholder'
UNION ALL
SELECT p.id, 'https://example.com/photos/cardholder2.jpg', 'Cardholder back', 2, 1200, 800
FROM product p WHERE p.slug = 'cardholder'

UNION ALL

SELECT p.id, 'https://example.com/photos/keyfob1.jpg', 'Key Fob main', 1, 1200, 800
FROM product p WHERE p.slug = 'key-fob'
UNION ALL
SELECT p.id, 'https://example.com/photos/keyfob2.jpg', 'Key Fob detail', 2, 1200, 800
FROM product p WHERE p.slug = 'key-fob'

UNION ALL

SELECT p.id, 'https://example.com/photos/belt1.jpg', 'Leather Belt full', 1, 1200, 800
FROM product p WHERE p.slug = 'leather-belt'
UNION ALL
SELECT p.id, 'https://example.com/photos/belt2.jpg', 'Leather Belt buckle', 2, 1200, 800
FROM product p WHERE p.slug = 'leather-belt'

UNION ALL

SELECT p.id, 'https://example.com/photos/phonecase1.jpg', 'Phone Case front', 1, 1200, 800
FROM product p WHERE p.slug = 'phone-case'
UNION ALL
SELECT p.id, 'https://example.com/photos/phonecase2.jpg', 'Phone Case back', 2, 1200, 800
FROM product p WHERE p.slug = 'phone-case'
UNION ALL
SELECT p.id, 'https://example.com/photos/phonecase3.jpg', 'Phone Case side', 3, 1200, 800
FROM product p WHERE p.slug = 'phone-case'

UNION ALL

SELECT p.id, 'https://example.com/photos/notebook1.jpg', 'Notebook cover', 1, 1200, 800
FROM product p WHERE p.slug = 'notebook'
UNION ALL
SELECT p.id, 'https://example.com/photos/notebook2.jpg', 'Notebook pages', 2, 1200, 800
FROM product p WHERE p.slug = 'notebook';
