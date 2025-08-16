-- Create persons table for financial entities and contacts
-- Date: 2025-08-15 18:05
-- Author: Bruno César

CREATE TABLE persons (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL,
    street VARCHAR(50),
    number VARCHAR(5),
    complement VARCHAR(50),
    neighborhood VARCHAR(50),
    zip_code VARCHAR(10),
    city_id BIGINT REFERENCES cities(id),
    state_id BIGINT REFERENCES states(id)
);

INSERT INTO persons (name, active, street, number, complement, neighborhood, zip_code, city_id, state_id)
VALUES 
('Bruno', true, 'Rua 1', 'SN', 'qd 5 lt 44', 'Jardim Itaipu', '74355509', 1, 1),
('Jessica', false, 'Rua 4', 'SN', 'qd 10 lt 89', 'Jardim Itaipu', '74355510', 2, 1),
('Marcos', true, 'Rua 5', 'SN', 'qd 90 lt 02', 'Cidade Jardim', '7897949', 1, 1),
('Carlos Lacerda', true, 'Rua 1', 'SN', 'qd 5 lt 44', 'Jardim Itaipu', '74355509', 1, 1),
('Danila', true, 'Rua 1', 'SN', 'qd 89 lt 34', 'Jardim Itaipu', '71325509', 1, 1),
('Geisson', true, 'Rua 9', 'SN', 'qd 3 lt 20', 'Centro', '4567982', 1, 1),
('Valdevan', true, 'Rua 56', 'SN', 'qd 1 lt 02', 'Residencial Itaipu', '74355521', 1, 1),
('Mel', true, 'Rua 67', 'SN', 'qd 3 lt 14', 'Setor Oeste', '1234567', 1, 1);

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS persons;
-- DELETE FROM flyway_schema_history WHERE version = '202508151805';