-- Add tags and attachments support to transactions

ALTER TABLE transactions DROP COLUMN IF EXISTS attachment;

ALTER TABLE transactions DROP CONSTRAINT IF EXISTS ck_transactions_type;
ALTER TABLE transactions ADD CONSTRAINT ck_transactions_type CHECK (type IN ('RECIPE','EXPENSE'));

CREATE TABLE IF NOT EXISTS tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_tags_name_lower ON tags (LOWER(name));

CREATE TABLE IF NOT EXISTS transaction_tags (
    transaction_id BIGINT NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (transaction_id, tag_id)
);

CREATE TABLE IF NOT EXISTS transaction_attachments (
    id BIGSERIAL PRIMARY KEY,
    transaction_id BIGINT NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    object_key VARCHAR(255) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    size_bytes BIGINT,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_transaction_attachments_tx ON transaction_attachments(transaction_id);

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS transaction_attachments;
-- DROP TABLE IF EXISTS transaction_tags;
-- DROP TABLE IF EXISTS tags;
-- ALTER TABLE transactions DROP CONSTRAINT IF EXISTS ck_transactions_type;
-- ALTER TABLE transactions ADD COLUMN attachment VARCHAR(255);
