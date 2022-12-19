CREATE TABLE user_last_online_record
(
    user_id     BIGINT    PRIMARY KEY,
    last_online TIMESTAMP NOT NULL
);

CREATE INDEX user_id_index ON user_last_online_record(user_id);