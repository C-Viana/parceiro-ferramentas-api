CREATE TABLE IF NOT EXISTS ferramentas (
    id SERIAL PRIMARY KEY,
    nome TEXT NOT NULL,
    modelo TEXT NOT NULL,
    fabricante TEXT NOT NULL,
    tipo TEXT NOT NULL,
    descricao TEXT,
	caracteristicas JSONB,
    itens_inclusos JSONB,
    disponibilidade BOOLEAN DEFAULT FALSE,
    preco_aluguel NUMERIC(10,2) NOT NULL,
    preco_venda NUMERIC(10,2) NOT NULL,
    lista_imagens JSONB,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices importantes para e-commerce/marketplace
CREATE INDEX idx_ferramentas_tipo ON ferramentas(tipo);
CREATE INDEX idx_ferramentas_fabricante ON ferramentas(fabricante);
CREATE INDEX idx_ferramentas_disponibilidade ON ferramentas(disponibilidade);
CREATE INDEX idx_ferramentas_preco_aluguel ON ferramentas(preco_aluguel);
CREATE INDEX idx_ferramentas_preco_venda ON ferramentas(preco_venda);
-- Índice GIN para buscas dentro do JSONB (muito usado em filtros avançados)
CREATE INDEX idx_ferramentas_caracteristicas_gin ON ferramentas USING GIN (caracteristicas);
CREATE INDEX idx_ferramentas_itens_inclusos_gin ON ferramentas USING GIN (itens_inclusos);