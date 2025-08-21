-- Seed dev data: artist profile, pages, products, photos

INSERT INTO artist_profile (name, bio, hero_image_url, socials, created_at, updated_at)
VALUES (
  'Baltaragis',
  'Contemporary artist blending traditional forms.',
  'https://example.com/hero.jpg',
  '{"instagram":"https://instagram.com/baltaragis","twitter":"https://x.com/baltaragis"}',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

INSERT INTO page (title, slug, content_md, is_published, created_at, updated_at)
VALUES
  ('Home', 'home', '# Welcome to Baltaragis', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('About', 'about', 'About the artist...', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
VALUES
  -- Original 3 products (keeping existing)
  ('Sunset Print', 'sunset-print', 'A3 giclée print', 'High-quality giclée print of a sunset.', 4500, 'EUR', 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Forest Sketch', 'forest-sketch', 'Graphite on paper', 'Original graphite sketch of a forest scene.', 12000, 'EUR', 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Ocean Study', 'ocean-study', 'Ink on paper', 'Study of ocean waves in ink.', 8000, 'EUR', 3, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  
  -- New products: 6 in-stock with variety
  ('Leather Wallet', 'leather-wallet', 'Handcrafted leather wallet', 'Premium leather wallet with multiple card slots and coin pocket.', 8500, 'EUR', 5, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Cardholder', 'cardholder', 'Minimalist cardholder', 'Slim leather cardholder perfect for everyday use.', 3500, 'EUR', 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Key Fob', 'key-fob', 'Artistic key fob', 'Handmade key fob with unique design.', 2500, 'EUR', 15, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Leather Belt', 'leather-belt', 'Classic leather belt', 'Timeless leather belt with brass buckle.', 12000, 'EUR', 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Phone Case', 'phone-case', 'Custom phone case', 'Personalized phone case with artistic design.', 5500, 'EUR', 8, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Notebook', 'notebook', 'Artisan notebook', 'Hand-bound notebook with quality paper.', 4000, 'EUR', 12, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Photos for products (lookup product ids by slug for stability)
INSERT INTO product_photo (product_id, url, alt, sort_order, width, height) VALUES
  ((SELECT id FROM product WHERE slug = 'sunset-print'), 'https://example.com/photos/sunset1.jpg', 'Sunset Print main', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'sunset-print'), 'https://example.com/photos/sunset2.jpg', 'Sunset Print detail', 2, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'sunset-print'), 'https://example.com/photos/sunset3.jpg', 'Sunset Print framed', 3, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'forest-sketch'), 'https://example.com/photos/forest1.jpg', 'Forest Sketch main', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'forest-sketch'), 'https://example.com/photos/forest2.jpg', 'Forest Sketch detail', 2, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'ocean-study'), 'https://example.com/photos/ocean1.jpg', 'Ocean Study main', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'ocean-study'), 'https://example.com/photos/ocean2.jpg', 'Ocean Study detail', 2, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'leather-wallet'), 'https://example.com/photos/wallet1.jpg', 'Leather Wallet front', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'leather-wallet'), 'https://example.com/photos/wallet2.jpg', 'Leather Wallet back', 2, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'leather-wallet'), 'https://example.com/photos/wallet3.jpg', 'Leather Wallet open', 3, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'cardholder'), 'https://example.com/photos/cardholder1.jpg', 'Cardholder front', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'cardholder'), 'https://example.com/photos/cardholder2.jpg', 'Cardholder back', 2, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'key-fob'), 'https://example.com/photos/keyfob1.jpg', 'Key Fob main', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'key-fob'), 'https://example.com/photos/keyfob2.jpg', 'Key Fob detail', 2, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'leather-belt'), 'https://example.com/photos/belt1.jpg', 'Leather Belt full', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'leather-belt'), 'https://example.com/photos/belt2.jpg', 'Leather Belt buckle', 2, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'phone-case'), 'https://example.com/photos/phonecase1.jpg', 'Phone Case front', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'phone-case'), 'https://example.com/photos/phonecase2.jpg', 'Phone Case back', 2, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'phone-case'), 'https://example.com/photos/phonecase3.jpg', 'Phone Case side', 3, 1200, 800),

  ((SELECT id FROM product WHERE slug = 'notebook'), 'https://example.com/photos/notebook1.jpg', 'Notebook cover', 1, 1200, 800),
  ((SELECT id FROM product WHERE slug = 'notebook'), 'https://example.com/photos/notebook2.jpg', 'Notebook pages', 2, 1200, 800);


