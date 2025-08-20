-- Add comprehensive UI translation keys for complete e-commerce application coverage
INSERT INTO translation (translation_key, locale, translation_value, updated_at) VALUES
-- Products page keys - English
('products.title', 'en-US', 'Products', CURRENT_TIMESTAMP),
('products.subtitle', 'en-US', 'Discover our collection of unique artworks and accessories', CURRENT_TIMESTAMP),
('products.filter_by', 'en-US', 'Filter by', CURRENT_TIMESTAMP),
('products.sort_by', 'en-US', 'Sort by', CURRENT_TIMESTAMP),
('products.price_low_to_high', 'en-US', 'Price: Low to High', CURRENT_TIMESTAMP),
('products.price_high_to_low', 'en-US', 'Price: High to Low', CURRENT_TIMESTAMP),
('products.name_a_to_z', 'en-US', 'Name: A to Z', CURRENT_TIMESTAMP),
('products.name_z_to_a', 'en-US', 'Name: Z to A', CURRENT_TIMESTAMP),
('products.in_stock', 'en-US', 'In Stock', CURRENT_TIMESTAMP),
('products.out_of_stock', 'en-US', 'Out of Stock', CURRENT_TIMESTAMP),
('products.add_to_cart', 'en-US', 'Add to Cart', CURRENT_TIMESTAMP),
('products.view_details', 'en-US', 'View Details', CURRENT_TIMESTAMP),
('products.back_to_products', 'en-US', 'Back to Products', CURRENT_TIMESTAMP),

-- Products page keys - Lithuanian
('products.title', 'lt-LT', 'Darbai', CURRENT_TIMESTAMP),
('products.subtitle', 'lt-LT', 'Atraskite mūsų unikalių meno kūrinių ir aksesuarų kolekciją', CURRENT_TIMESTAMP),
('products.filter_by', 'lt-LT', 'Filtruoti pagal', CURRENT_TIMESTAMP),
('products.sort_by', 'lt-LT', 'Rūšiuoti pagal', CURRENT_TIMESTAMP),
('products.price_low_to_high', 'lt-LT', 'Kaina: nuo mažiausios iki didžiausios', CURRENT_TIMESTAMP),
('products.price_high_to_low', 'lt-LT', 'Kaina: nuo didžiausios iki mažiausios', CURRENT_TIMESTAMP),
('products.name_a_to_z', 'lt-LT', 'Pavadinimas: A-Ž', CURRENT_TIMESTAMP),
('products.name_z_to_a', 'lt-LT', 'Pavadinimas: Ž-A', CURRENT_TIMESTAMP),
('products.in_stock', 'lt-LT', 'Turima sandėlyje', CURRENT_TIMESTAMP),
('products.out_of_stock', 'lt-LT', 'Nėra sandėlyje', CURRENT_TIMESTAMP),
('products.add_to_cart', 'lt-LT', 'Pridėti į krepšelį', CURRENT_TIMESTAMP),
('products.view_details', 'lt-LT', 'Peržiūrėti detales', CURRENT_TIMESTAMP),
('products.back_to_products', 'lt-LT', 'Grįžti į darbus', CURRENT_TIMESTAMP),

-- Cart and checkout keys - English
('cart.title', 'en-US', 'Shopping Cart', CURRENT_TIMESTAMP),
('cart.empty', 'en-US', 'Your cart is empty', CURRENT_TIMESTAMP),
('cart.continue_shopping', 'en-US', 'Continue Shopping', CURRENT_TIMESTAMP),
('cart.checkout', 'en-US', 'Checkout', CURRENT_TIMESTAMP),
('cart.remove', 'en-US', 'Remove', CURRENT_TIMESTAMP),
('cart.quantity', 'en-US', 'Quantity', CURRENT_TIMESTAMP),
('cart.subtotal', 'en-US', 'Subtotal', CURRENT_TIMESTAMP),
('cart.total', 'en-US', 'Total', CURRENT_TIMESTAMP),
('cart.shipping', 'en-US', 'Shipping', CURRENT_TIMESTAMP),
('cart.tax', 'en-US', 'Tax', CURRENT_TIMESTAMP),

-- Cart and checkout keys - Lithuanian
('cart.title', 'lt-LT', 'Pirkinių krepšelis', CURRENT_TIMESTAMP),
('cart.empty', 'lt-LT', 'Jūsų krepšelis tuščias', CURRENT_TIMESTAMP),
('cart.continue_shopping', 'lt-LT', 'Tęsti apsipirkimą', CURRENT_TIMESTAMP),
('cart.checkout', 'lt-LT', 'Atsiskaityti', CURRENT_TIMESTAMP),
('cart.remove', 'lt-LT', 'Pašalinti', CURRENT_TIMESTAMP),
('cart.quantity', 'lt-LT', 'Kiekis', CURRENT_TIMESTAMP),
('cart.subtotal', 'lt-LT', 'Tarpinė suma', CURRENT_TIMESTAMP),
('cart.total', 'lt-LT', 'Iš viso', CURRENT_TIMESTAMP),
('cart.shipping', 'lt-LT', 'Pristatymas', CURRENT_TIMESTAMP),
('cart.tax', 'lt-LT', 'Mokestis', CURRENT_TIMESTAMP),

-- Footer and general UI keys - English
('footer.copyright', 'en-US', '© 2024 Baltaragis. All rights reserved.', CURRENT_TIMESTAMP),
('footer.contact', 'en-US', 'Contact', CURRENT_TIMESTAMP),
('footer.privacy', 'en-US', 'Privacy Policy', CURRENT_TIMESTAMP),
('footer.terms', 'en-US', 'Terms of Service', CURRENT_TIMESTAMP),
('footer.shipping', 'en-US', 'Shipping Info', CURRENT_TIMESTAMP),
('footer.returns', 'en-US', 'Returns', CURRENT_TIMESTAMP),

-- Footer and general UI keys - Lithuanian
('footer.copyright', 'lt-LT', '© 2024 Baltaragis. Visos teisės saugomos.', CURRENT_TIMESTAMP),
('footer.contact', 'lt-LT', 'Kontaktai', CURRENT_TIMESTAMP),
('footer.privacy', 'lt-LT', 'Privatumo politika', CURRENT_TIMESTAMP),
('footer.terms', 'lt-LT', 'Naudojimo sąlygos', CURRENT_TIMESTAMP),
('footer.shipping', 'lt-LT', 'Pristatymo informacija', CURRENT_TIMESTAMP),
('footer.returns', 'lt-LT', 'Grąžinimas', CURRENT_TIMESTAMP);
