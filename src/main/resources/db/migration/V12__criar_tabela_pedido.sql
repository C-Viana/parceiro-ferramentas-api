CREATE TABLE IF NOT EXISTS pedido
(
    id SERIAL PRIMARY KEY,
    situacao TEXT NOT NULL,
    preco_total NUMERIC(10,2) NOT NULL,
    itens JSONB NOT NULL,
    tipo TEXT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_final TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    finalizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INTEGER REFERENCES usuario(id) NOT NULL
);

CREATE INDEX idx_pedido_situacao ON pedido(situacao);
CREATE INDEX idx_pedido_tipo ON pedido(tipo);
CREATE INDEX idx_pedido_criado_em ON pedido(criado_em);
CREATE INDEX idx_pedido_data_final ON pedido(data_final);
CREATE INDEX idx_pedido_finalizado_em ON pedido(finalizado_em);