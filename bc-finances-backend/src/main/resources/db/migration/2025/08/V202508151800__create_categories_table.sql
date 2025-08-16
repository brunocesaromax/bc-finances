-- Create categories table for financial transaction classification
-- Date: 2025-08-15 18:00
-- Author: Bruno César

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO categories (name) VALUES 
('Lazer'),
('Alimentação'),
('Supermecado'),
('Farmácia'),
('Outros');

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS categories;
-- DELETE FROM flyway_schema_history WHERE version = '202508151800';