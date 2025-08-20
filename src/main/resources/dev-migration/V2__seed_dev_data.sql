-- Seed dev data: artist profile, pages, products, photos

INSERT INTO artist_profile (name, bio, hero_image_url, socials)
VALUES ('Baltaragis', 'Contemporary artist blending traditional forms.', 'https://example.com/hero.jpg',
        '{"instagram":"https://instagram.com/baltaragis","twitter":"https://x.com/baltaragis"}');

INSERT INTO page (title, slug, content_md, is_published)
VALUES ('Home', 'home', '# Welcome to Baltaragis', TRUE),
       ('About', 'about', 'About the artist...', TRUE);

INSERT INTO product (name, slug, short_desc, long_desc, price_cents, currency, quantity, is_published)
VALUES
  ('Sunset Print', 'sunset-print', 'A3 giclée print', 'High-quality giclée print of a sunset.', 4500, 'EUR', 10, TRUE),
  ('Forest Sketch', 'forest-sketch', 'Graphite on paper', 'Original graphite sketch of a forest scene.', 12000, 'EUR', 0, TRUE),
  ('Ocean Study', 'ocean-study', 'Ink on paper', 'Study of ocean waves in ink.', 8000, 'EUR', 3, TRUE);

-- Photos for products (assume id 1..3 for the inserts above)
INSERT INTO product_photo (product_id, url, alt, sort_order, width, height)
VALUES
  (1, 'https://example.com/photos/sunset1.jpg', 'Sunset Print main', 1, 1200, 800),
  (1, 'https://example.com/photos/sunset2.jpg', 'Sunset Print detail', 2, 1200, 800),
  (2, 'https://example.com/photos/forest1.jpg', 'Forest Sketch main', 1, 1200, 800),
  (3, 'https://example.com/photos/ocean1.jpg', 'Ocean Study main', 1, 1200, 800);


