CREATE TABLE chat
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE chat_user
(
    chat_id BIGINT REFERENCES chat (id) ON DELETE CASCADE NOT NULL,
    user_id BIGINT                                        NOT NULL,
    PRIMARY KEY (chat_id, user_id)
);

CREATE TABLE message
(
    id           BIGINT PRIMARY KEY,
    message_text TEXT                                          NOT NULL,
    sender_id    BIGINT                                        NOT NULL,
    chat_id      BIGINT REFERENCES chat (id) ON DELETE CASCADE NOT NULL,
    date         TIMESTAMP
);