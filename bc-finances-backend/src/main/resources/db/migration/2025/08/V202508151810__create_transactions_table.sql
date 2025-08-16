-- Create transactions table for financial transactions
-- Date: 2025-08-15 18:10
-- Author: Bruno César

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    due_day DATE NOT NULL,
    payday DATE,
    value DECIMAL(10,2) NOT NULL,
    observation VARCHAR(100),
    type VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_transactions_person FOREIGN KEY (person_id) REFERENCES persons(id)
);

INSERT INTO transactions (description, due_day, payday, value, observation, type, category_id, person_id)
VALUES 
('Salário Mensal', '2019-09-19', '2019-09-21', 150.88, null, 'EXPENSE', 5, 1),
('Lanche', '2018-02-19', '2019-09-21', 18.88, null, 'EXPENSE', 4, 3),
('Conta de luz', '2015-10-14', '2019-09-21', 180.00, null, 'EXPENSE', 2, 2),
('Internet fibra optica', '2020-03-12', '2030-09-21', 119.90, null, 'EXPENSE', 1, 5);

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS transactions;
-- DELETE FROM flyway_schema_history WHERE version = '202508151810';