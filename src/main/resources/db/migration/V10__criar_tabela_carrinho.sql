CREATE TABLE IF NOT EXISTS carrinho_itens
(
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER REFERENCES usuario(id) NOT NULL,
    ferramenta_id INTEGER REFERENCES ferramentas(id) NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_aluguel_momento NUMERIC(10,2) NOT NULL,
    preco_venda_momento NUMERIC(10,2) NOT NULL,
    url_imagem TEXT,
    data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
