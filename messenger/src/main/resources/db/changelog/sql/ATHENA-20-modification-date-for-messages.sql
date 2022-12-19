ALTER TABLE message
    ADD COLUMN modification_date TIMESTAMP;

ALTER TABLE message
    RENAME COLUMN date TO creation_date;

UPDATE message
SET modification_date = creation_date
WHERE modification_date IS NULL;

ALTER TABLE message
    ALTER COLUMN modification_date SET NOT NULL;