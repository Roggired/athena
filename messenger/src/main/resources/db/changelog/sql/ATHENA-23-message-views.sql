CREATE TABLE message_views
(
    message_id BIGINT REFERENCES message (id) ON DELETE CASCADE NOT NULL,
    user_id    BIGINT REFERENCES athena_users (id) ON DELETE CASCADE   NOT NULL
);