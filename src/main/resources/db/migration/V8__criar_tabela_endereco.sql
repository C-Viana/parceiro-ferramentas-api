CREATE TABLE IF NOT EXISTS endereco
(
    id SERIAL PRIMARY KEY,
    logradouro TEXT NOT NULL,
    numero INTEGER NOT NULL,
    bairro TEXT,
    cidade TEXT NOT NULL,
    estado TEXT NOT NULL,
    uf TEXT NOT NULL,
    cep TEXT NOT NULL,
    referencia TEXT,
    principal BOOLEAN,
    usuario_id INTEGER REFERENCES usuario(id) NOT NULL
);

CREATE INDEX idx_endereco_cidade ON endereco(cidade);
CREATE INDEX idx_endereco_bairro ON endereco(bairro);
CREATE INDEX idx_endereco_estado ON endereco(estado);
CREATE INDEX idx_endereco_uf ON endereco(uf);
CREATE INDEX idx_endereco_cep ON endereco(cep);