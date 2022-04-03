CREATE SEQUENCE hibernate_sequence;

CREATE TABLE client (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN
);

CREATE SEQUENCE client_seq AS BIGINT;

CREATE TABLE client_permission (
    client_id BIGINT NOT NULL REFERENCES client(id) ON DELETE CASCADE,
    permission VARCHAR(255) NOT NULL,
    PRIMARY KEY(client_id, permission)
);

INSERT INTO client(id, name, active) VALUES(nextval('client_seq'), 'admin', true);
INSERT INTO client_permission(client_id, permission) VALUES(1, 'ADMIN');