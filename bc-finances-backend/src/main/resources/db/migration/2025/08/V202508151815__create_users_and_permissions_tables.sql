-- Create users, permissions and user_permissions tables for authentication
-- Date: 2025-08-15 18:15
-- Author: Bruno César

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(150) NOT NULL
);

CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(100) NOT NULL
);

CREATE TABLE user_permissions (
    user_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

INSERT INTO users (name, email, password) VALUES 
('Administrador', 'admin@algamoney.com', '$2a$10$6v9JTwJNt1gngxGTy51ecON5Sx.m8aJ2HZPz.i2moVeP8.2oUAZAO'),
('Maria Silva', 'maria@algamoney.com', '$2a$10$gYakxlN5Ldbh5GbEjuq/WuxstGQBtIQ2U95q1jsa6mgQcyFyRXtOG');

INSERT INTO permissions (description) VALUES
('ROLE_CREATE_CATEGORY'),
('ROLE_SEARCH_CATEGORY'),
('ROLE_CREATE_PERSON'),
('ROLE_REMOVE_PERSON'),
('ROLE_SEARCH_PERSON'),
('ROLE_CREATE_TRANSACTION'),
('ROLE_REMOVE_TRANSACTION'),
('ROLE_SEARCH_TRANSACTION');

-- admin permissions
INSERT INTO user_permissions (user_id, permission_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8);

-- maria permissions
INSERT INTO user_permissions (user_id, permission_id) VALUES 
(2, 2), (2, 5), (2, 8);

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS user_permissions;
-- DROP TABLE IF EXISTS permissions;
-- DROP TABLE IF EXISTS users;
-- DELETE FROM flyway_schema_history WHERE version = '202508151815';