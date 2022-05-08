CREATE TABLE message_user
(
    message_id BIGINT REFERENCES message (id) ON DELETE RESTRICT,
    user_id    BIGINT REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (message_id, user_id)
);