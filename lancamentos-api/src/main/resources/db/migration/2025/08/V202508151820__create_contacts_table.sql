-- Create contacts table for person contact information
-- Date: 2025-08-15 18:20
-- Author: Bruno César

CREATE TABLE contacts (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons(id)
);

INSERT INTO contacts (person_id, name, email, phone)
VALUES (1, 'Marcos Henrique', 'marcos@algamoney.com', '00 0000-0000');

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS contacts;
-- DELETE FROM flyway_schema_history WHERE version = '202508151820';