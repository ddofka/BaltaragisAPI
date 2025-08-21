-- Fix currency column types for MySQL compatibility
-- Change CHAR(3) to VARCHAR(3) to resolve schema validation issues

ALTER TABLE product MODIFY COLUMN currency VARCHAR(3) NOT NULL DEFAULT 'EUR';
ALTER TABLE orders MODIFY COLUMN currency VARCHAR(3) NOT NULL DEFAULT 'EUR';
