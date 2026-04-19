CREATE TABLE IF NOT EXISTS comprador (
    id UUID PRIMARY KEY,
    cpf TEXT NOT NULL,
    nome TEXT NOT NULL,
    nascimento TIMESTAMP NOT NULL,
    email TEXT NOT NULL,
    phone TEXT NOT NULL
);

-- Índices importantes para e-commerce/marketplace
CREATE INDEX idx_comprador_cpf ON comprador(cpf);
CREATE INDEX idx_comprador_email ON comprador(email);
CREATE INDEX idx_comprador_phone ON comprador(phone);