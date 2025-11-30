ALTER TABLE transactions ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_transactions_deleted_at ON transactions (deleted_at);

-- ROLLBACK (SQL to undo this migration):
-- DROP INDEX IF EXISTS idx_transactions_deleted_at;
-- ALTER TABLE transactions DROP COLUMN IF EXISTS deleted_at;
