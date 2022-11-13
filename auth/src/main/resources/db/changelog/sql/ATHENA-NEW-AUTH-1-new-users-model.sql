-- DROP TABLE users;
-- DROP TABLE user_lock;

CREATE TABLE lock
(
    id     BIGSERIAL PRIMARY KEY,
    locked BOOLEAN NOT NULL,
    reason TEXT    NOT NULL
);

CREATE TABLE credentials
(
    id              BIGSERIAL PRIMARY KEY,
    value           VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP    NOT NULL
);

CREATE TABLE session
(
    id              BIGSERIAL PRIMARY KEY,
    session_id      VARCHAR(255) NOT NULL,
    last_login_date TIMESTAMP    NOT NULL
);

CREATE TABLE athena_users
(
    id             BIGSERIAL PRIMARY KEY,
    login          VARCHAR(15)                        NOT NULL,
    role           VARCHAR(15)                        NOT NULL,
    lock_id        BIGINT REFERENCES lock (id)        NOT NULL,
    credentials_id BIGINT REFERENCES credentials (id) NOT NULL,
    session_id     BIGINT REFERENCES session (id)
);