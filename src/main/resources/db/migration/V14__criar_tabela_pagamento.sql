CREATE TYPE STATUS_PAGAMENTO AS ENUM (
    'PENDENTE',
    'APROVADO',
    'RECUSADO',
    'CANCELADO',
    'REEMBOLSADO'
);

CREATE TYPE TIPO_PAGAMENTO AS ENUM (
    'PIX', 
    'CARTAO_CREDITO', 
    'BOLETO', 
    'DINHEIRO', 
    'DEBITO'
);

CREATE TABLE IF NOT EXISTS pagamento
(
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
    forma_pagamento TIPO_PAGAMENTO NOT NULL,
    situacao STATUS_PAGAMENTO NOT NULL DEFAULT 'PENDENTE',
    data_criacao TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    codigo_transacao TEXT NOT NULL,
    detalhes JSONB,
    CONSTRAINT idx_pagamento_pedido_id UNIQUE (pedido_id)
);

CREATE INDEX idx_pagamento_situacao ON pagamento(situacao);
CREATE INDEX idx_pagamento_forma_pagamento ON pagamento(forma_pagamento);
CREATE INDEX idx_pagamento_data_criacao ON pagamento(data_criacao);