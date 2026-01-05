CREATE TABLE IF NOT EXISTS carrinho
(
    id SERIAL PRIMARY KEY,
    itens JSONB,
    id_usuario INTEGER REFERENCES usuario(id) NOT NULL
);
