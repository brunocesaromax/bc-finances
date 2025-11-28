-- Add transaction_type to categories and seed base data for receitas/despesas

ALTER TABLE categories ADD COLUMN IF NOT EXISTS transaction_type VARCHAR(20);
UPDATE categories SET transaction_type = 'EXPENSE' WHERE transaction_type IS NULL;
ALTER TABLE categories ALTER COLUMN transaction_type SET NOT NULL;

ALTER TABLE categories DROP CONSTRAINT IF EXISTS ck_categories_transaction_type;
ALTER TABLE categories ADD CONSTRAINT ck_categories_transaction_type CHECK (transaction_type IN ('RECIPE','EXPENSE'));

TRUNCATE TABLE categories RESTART IDENTITY CASCADE;

INSERT INTO categories (name, transaction_type) VALUES
('Assinaturas', 'RECIPE'),
('Empréstimos', 'RECIPE'),
('Outros', 'RECIPE'),
('Presente', 'RECIPE'),
('Salário', 'RECIPE'),
('Alimentação', 'EXPENSE'),
('Assinaturas e serviços', 'EXPENSE'),
('Cartão Emprestado', 'EXPENSE'),
('Casa', 'EXPENSE'),
('Compras', 'EXPENSE'),
('Cuidados Pessoais', 'EXPENSE'),
('Dívidas e Empréstimos', 'EXPENSE'),
('Educação', 'EXPENSE'),
('Faturas de cartão', 'EXPENSE'),
('Gastos nas Férias', 'EXPENSE'),
('Gastos na Fé', 'EXPENSE'),
('Impostos e Taxas', 'EXPENSE'),
('Lazer e Hobbies', 'EXPENSE'),
('Momento casal', 'EXPENSE'),
('Outros', 'EXPENSE'),
('Pets', 'EXPENSE'),
('Presentes e Doações', 'EXPENSE'),
('Saúde', 'EXPENSE'),
('Veículo', 'EXPENSE'),
('Viagens', 'EXPENSE');

CREATE UNIQUE INDEX IF NOT EXISTS uk_categories_name_type ON categories (LOWER(name), transaction_type);

-- ROLLBACK (SQL to undo this migration):
-- DROP INDEX IF EXISTS uk_categories_name_type;
-- ALTER TABLE categories DROP CONSTRAINT IF EXISTS ck_categories_transaction_type;
-- ALTER TABLE categories DROP COLUMN IF EXISTS transaction_type;
-- DELETE FROM categories;
