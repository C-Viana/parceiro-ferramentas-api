CREATE TABLE IF NOT EXISTS usuario
(
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    nome TEXT NOT NULL,
    senha TEXT NOT NULL,
    account_non_expired BOOLEAN DEFAULT FALSE,
    account_non_locked BOOLEAN DEFAULT FALSE,
    credentials_non_expired BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT FALSE,
    UNIQUE (username)
);

CREATE INDEX idx_usuario_username ON usuario(username);
CREATE INDEX idx_usuario_nome ON usuario(nome);