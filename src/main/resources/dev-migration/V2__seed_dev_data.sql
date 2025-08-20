-- Seed dev data: artist profile, pages, products, photos

INSERT INTO artist_profile (name, bio, hero_image_url, socials, created_at, updated_at)
VALUES ('Baltaragis', 'Contemporary artist blending traditional forms.', 'https://example.com/hero.jpg',
        '{"instagram":"https://instagram.com/baltaragis","twitter":"https://x.com/baltaragis"}',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO page (title, slug, content_md, is_published, created_at, updated_at)
VALUES ('Home', 'home', '# Welcome to Baltaragis', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('About', 'about', 'About the artist...', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published, created_at, updated_at)
VALUES
  ('Sunset Print', 'sunset-print', 'A3 giclée print', 'High-quality giclée print of a sunset.', 4500, 'EUR', 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Forest Sketch', 'forest-sketch', 'Graphite on paper', 'Original graphite sketch of a forest scene.', 12000, 'EUR', 0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Ocean Study', 'ocean-study', 'Ink on paper', 'Study of ocean waves in ink.', 8000, 'EUR', 3, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Photos for products (assume id 1..3 for the inserts above)
INSERT INTO product_photo (product_id, url, alt, sort_order, width, height)
VALUES
  (1, 'https://example.com/photos/sunset1.jpg', 'Sunset Print main', 1, 1200, 800),
  (1, 'https://example.com/photos/sunset2.jpg', 'Sunset Print detail', 2, 1200, 800),
  (2, 'https://example.com/photos/forest1.jpg', 'Forest Sketch main', 1, 1200, 800),
  (3, 'https://example.com/photos/ocean1.jpg', 'Ocean Study main', 1, 1200, 800);


