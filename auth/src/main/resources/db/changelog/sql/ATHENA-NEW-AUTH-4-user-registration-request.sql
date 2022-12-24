CREATE TABLE user_registration_requests
(
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL,
    requested_at    TIMESTAMP    NOT NULL,
    is_approved     BOOLEAN      NOT NULL,
    approved_at     TIMESTAMP    NOT NULL,
    created_user_id BIGINT REFERENCES athena_users (id) ON DELETE CASCADE
);