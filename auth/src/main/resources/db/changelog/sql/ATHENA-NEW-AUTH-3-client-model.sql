CREATE TABLE clients
(
    id           BIGSERIAL PRIMARY KEY,
    client_id    VARCHAR(255) NOT NULL UNIQUE,
    client_secret TEXT         NOT NULL,
    redirect_url VARCHAR(255)
);