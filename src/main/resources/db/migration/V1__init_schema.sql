-- Core schema for products, orders, pages, waitlist, and artist profiles

CREATE TABLE artist_profile (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  bio TEXT,
  hero_image_url VARCHAR(1024),
  socials TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  slug VARCHAR(255) NOT NULL,
  short_desc TEXT,
  long_desc TEXT,
  price_cents INT NOT NULL,
  currency CHAR(3) NOT NULL DEFAULT 'EUR',
  quantity INT NOT NULL DEFAULT 0,
  is_published BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_product_slug ON product(slug);
CREATE INDEX idx_product_published ON product(is_published);

CREATE TABLE product_photo (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  url VARCHAR(1024) NOT NULL,
  alt VARCHAR(255),
  sort_order INT,
  width INT,
  height INT,
  CONSTRAINT fk_product_photo_product FOREIGN KEY (product_id) REFERENCES product(id)
);
CREATE INDEX idx_product_photo_product ON product_photo(product_id);

CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  total_cents INT NOT NULL,
  currency CHAR(3) NOT NULL DEFAULT 'EUR',
  status VARCHAR(16) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  qty INT NOT NULL,
  price_cents INT NOT NULL,
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product(id)
);
CREATE INDEX idx_order_item_order ON order_item(order_id);

CREATE TABLE stock_waitlist (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  email VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  notified_at TIMESTAMP NULL,
  CONSTRAINT fk_waitlist_product FOREIGN KEY (product_id) REFERENCES product(id),
  CONSTRAINT uk_waitlist_product_email UNIQUE (product_id, email)
);
CREATE INDEX idx_waitlist_product ON stock_waitlist(product_id);

CREATE TABLE page (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  slug VARCHAR(255) NOT NULL,
  content_md TEXT,
  is_published BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_page_slug ON page(slug);
CREATE INDEX idx_page_published ON page(is_published);


