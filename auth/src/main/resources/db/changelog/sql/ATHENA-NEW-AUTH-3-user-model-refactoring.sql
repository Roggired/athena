DROP TABLE clients;

ALTER TABLE athena_users
    ADD COLUMN email VARCHAR(255) NOT NULL DEFAULT 'no-email';
ALTER TABLE credentials
    ADD COLUMN is_temporary BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE athena_users
    ALTER COLUMN login TYPE VARCHAR(63);

CREATE TABLE reset_password_codes
(
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP    NOT NULL,
    admin_id        BIGINT REFERENCES athena_users (id) ON DELETE CASCADE
);