CREATE TABLE user_lock(
    id BIGINT PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    reason VARCHAR(63) NOT NULL
);

CREATE TABLE users(
    id BIGINT PRIMARY KEY,
    name VARCHAR(63) NOT NULL,
    login VARCHAR(63) NOT NULL UNIQUE,
    lock_id BIGINT REFERENCES user_lock(id) ON DELETE CASCADE,
    allowed_device_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    activated BOOLEAN NOT NULL
);