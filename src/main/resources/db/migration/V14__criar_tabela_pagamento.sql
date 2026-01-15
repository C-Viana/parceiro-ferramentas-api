CREATE TABLE IF NOT EXISTS pagamento
(
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
    forma_pagamento TEXT NOT NULL,
    valor NUMERIC(10,2) NOT NULL CHECK (valor >= 0),
    situacao TEXT NOT NULL DEFAULT 'PENDENTE',
    data_criacao TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    codigo_transacao TEXT NOT NULL,
    detalhes JSONB,
    CONSTRAINT idx_pagamento_pedido_id UNIQUE (pedido_id)
);

CREATE INDEX idx_pagamento_situacao ON pagamento(situacao);
CREATE INDEX idx_pagamento_forma_pagamento ON pagamento(forma_pagamento);
CREATE INDEX idx_pagamento_data_criacao ON pagamento(data_criacao);