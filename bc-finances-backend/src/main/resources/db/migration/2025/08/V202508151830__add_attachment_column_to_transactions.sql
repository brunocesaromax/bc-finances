-- Add attachment column to transactions table for file uploads
-- Date: 2025-08-15 18:30
-- Author: Bruno César

ALTER TABLE transactions ADD COLUMN attachment VARCHAR(255);

-- ROLLBACK (SQL to undo this migration):
-- ALTER TABLE transactions DROP COLUMN IF EXISTS attachment;
-- DELETE FROM flyway_schema_history WHERE version = '202508151830';