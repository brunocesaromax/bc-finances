# Padrões para Migrations (Flyway)

## Estrutura de Diretórios

```
src/main/resources/db/migration/
├── 2025/
│   ├── 08/
│   │   ├── 202508142205__create_categories_table.sql
│   │   ├── 202508142210__create_users_table.sql
│   │   └── 202508142215__create_transactions_table.sql
│   └── 09/
│       └── 202509011200__add_index_to_users.sql
└── 2026/
    └── 01/
        └── 202601151030__new_feature_migration.sql
```

- Organize migrations por ano/mês para facilitar rastreabilidade.

## Convenção de Nomes

- Formato obrigatório: `YYYYMMDDHHMM__descricao.sql`.
- A descrição deve usar snake_case, ser curta e descritiva.
- Exemplo: `202508142210__add_email_index_to_users.sql`.

## Padrões de SQL

- Palavras-chave SQL em **UPPERCASE** (`CREATE`, `SELECT`, `WHERE`).
- Tabelas sempre no plural (`users`, `transactions`).
- Colunas em snake_case minúsculo (`user_id`, `created_at`).
- Constraints com nomes claros (`fk_transactions_user_id`, `idx_users_email`).

## Padrões de Tabelas

- Tabelas com chaves primárias `id BIGSERIAL PRIMARY KEY`.
- Chaves estrangeiras no formato `<entidade>_id BIGINT NOT NULL`.
- Timestamps padrões: `created_at TIMESTAMP DEFAULT NOW()` e `updated_at TIMESTAMP DEFAULT NOW()`.
- Utilize índices auxiliares conforme necessário (`CREATE INDEX idx_users_email ON users(email);`).

## Estrutura Recomendada

```sql
-- Create users table with basic authentication fields
-- Date: 2025-08-14
-- Author: [Developer name]

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);

INSERT INTO users (name, email, password) VALUES 
('Administrador', 'admin@algamoney.com', 'admin'),
('Maria Silva', 'maria@algamoney.com', 'maria');

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS users;
-- DELETE FROM flyway_schema_history WHERE version = '202508142210';
```

## Comentários de Reversão

- Toda migration deve terminar com instruções de reversão comentadas em inglês.
- Inclua comandos completos para desfazer alterações e remover o registro correspondente da `flyway_schema_history`.
- Se múltiplas estruturas forem criadas, ordene os comandos de rollback na sequência inversa.

## Regras Críticas

1. Nunca modifique migrations já executadas em produção.
2. Teste a reversão em ambiente de desenvolvimento antes de entregar.
3. Uma migration deve ter responsabilidade única (criar tabela, adicionar coluna, etc.).
4. Utilize transações (`BEGIN; ... COMMIT;`) quando necessário.
5. Valide dados antes de executar alterações destrutivas.

## Validação Automatizada

- Utilize o script `bc-finances-backend/script/validate-migrations.sh` para validar nomenclatura, diretórios e duplicidade de timestamps.
- Execute a validação antes de abrir PRs que incluam novas migrations.

## Exemplos de Rollbacks

```sql
-- For CREATE TABLE:
-- DROP TABLE IF EXISTS table_name;

-- For ALTER TABLE ADD COLUMN:
-- ALTER TABLE table_name DROP COLUMN IF EXISTS column_name;

-- For INSERT:
-- DELETE FROM table_name WHERE condition;

-- For CREATE INDEX:
-- DROP INDEX IF EXISTS index_name;
```
