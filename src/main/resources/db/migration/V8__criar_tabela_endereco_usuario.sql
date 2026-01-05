CREATE TABLE IF NOT EXISTS endereco_usuario
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
    id_usuario INTEGER REFERENCES usuario(id) NOT NULL
);

CREATE INDEX idx_endereco_usuario_cidade ON endereco_usuario(cidade);
CREATE INDEX idx_endereco_usuario_bairro ON endereco_usuario(bairro);
CREATE INDEX idx_endereco_usuario_estado ON endereco_usuario(estado);
CREATE INDEX idx_endereco_usuario_uf ON endereco_usuario(uf);
CREATE INDEX idx_endereco_usuario_cep ON endereco_usuario(cep);