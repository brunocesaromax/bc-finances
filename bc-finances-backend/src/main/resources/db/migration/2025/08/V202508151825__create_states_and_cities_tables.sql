-- Create states and cities tables for address management
-- Date: 2025-08-15 18:25
-- Author: Bruno César

CREATE TABLE states (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO states (id, name) VALUES 
(1, 'Acre'), (2, 'Alagoas'), (3, 'Amapá'), (4, 'Amazonas'), (5, 'Bahia'),
(6, 'Ceará'), (7, 'Distrito Federal'), (8, 'Espírito Santo'), (9, 'Goiás'),
(10, 'Maranhão'), (11, 'Mato Grosso'), (12, 'Mato Grosso do Sul'),
(13, 'Minas Gerais'), (14, 'Pará'), (15, 'Paraíba'), (16, 'Paraná'),
(17, 'Pernambuco'), (18, 'Piauí'), (19, 'Rio de Janeiro'),
(20, 'Rio Grande do Norte'), (21, 'Rio Grande do Sul'), (22, 'Rondônia'),
(23, 'Roraima'), (24, 'Santa Catarina'), (25, 'São Paulo'),
(26, 'Sergipe'), (27, 'Tocantins');

CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    state_id BIGINT NOT NULL,
    FOREIGN KEY (state_id) REFERENCES states(id)
);

-- Sample cities for Acre (state_id = 1)
INSERT INTO cities (id, name, state_id) VALUES 
(1, 'Acrelândia', 1), (2, 'Assis Brasil', 1), (3, 'Brasiléia', 1),
(4, 'Bujari', 1), (5, 'Capixaba', 1), (6, 'Cruzeiro do Sul', 1),
(7, 'Epitaciolândia', 1), (8, 'Feijó', 1), (9, 'Jordão', 1),
(10, 'Mâncio Lima', 1);

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS cities;
-- DROP TABLE IF EXISTS states;
-- DELETE FROM flyway_schema_history WHERE version = '202508151825';