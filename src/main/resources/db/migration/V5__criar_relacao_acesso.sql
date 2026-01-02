CREATE TABLE IF NOT EXISTS acesso
(
    id_usuario INTEGER REFERENCES usuario(id) NOT NULL,
    id_permissao INTEGER REFERENCES permissao(id) NOT NULL,
    PRIMARY KEY (id_usuario, id_permissao)
)