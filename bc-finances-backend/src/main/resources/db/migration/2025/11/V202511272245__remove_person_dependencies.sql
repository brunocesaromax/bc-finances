-- Remove person relationship and related permissions

TRUNCATE TABLE transactions CASCADE;

ALTER TABLE transactions DROP CONSTRAINT IF EXISTS fk_transactions_person;
ALTER TABLE transactions DROP COLUMN IF EXISTS person_id;

DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS persons;

DELETE FROM user_permissions
WHERE permission_id IN (
    SELECT id FROM permissions WHERE description IN ('ROLE_CREATE_PERSON', 'ROLE_REMOVE_PERSON', 'ROLE_SEARCH_PERSON')
);

DELETE FROM permissions WHERE description IN ('ROLE_CREATE_PERSON', 'ROLE_REMOVE_PERSON', 'ROLE_SEARCH_PERSON');

-- ROLLBACK (SQL to undo this migration):
-- ALTER TABLE transactions ADD COLUMN person_id BIGINT;
-- ALTER TABLE transactions ADD CONSTRAINT fk_transactions_person FOREIGN KEY (person_id) REFERENCES persons(id);
-- CREATE TABLE persons (
--     id BIGSERIAL PRIMARY KEY,
--     name VARCHAR(50) NOT NULL,
--     active BOOLEAN NOT NULL,
--     street VARCHAR(50),
--     number VARCHAR(5),
--     complement VARCHAR(50),
--     neighborhood VARCHAR(50),
--     zip_code VARCHAR(10),
--     city_id BIGINT,
--     state_id BIGINT
-- );
-- CREATE TABLE contacts (
--     id BIGSERIAL PRIMARY KEY,
--     person_id BIGINT NOT NULL REFERENCES persons(id),
--     name VARCHAR(50) NOT NULL,
--     email VARCHAR(100) NOT NULL,
--     phone VARCHAR(20) NOT NULL
-- );
-- INSERT INTO permissions (description)
-- VALUES ('ROLE_CREATE_PERSON'), ('ROLE_REMOVE_PERSON'), ('ROLE_SEARCH_PERSON');
