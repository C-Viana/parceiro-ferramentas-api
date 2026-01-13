CREATE TYPE STATUS_PEDIDO AS ENUM (
    'PENDENTE', 
    'APROVADO', 
    'FINALIZADO', 
    'CANCELADO', 
    'DEVOLVIDO', 
    'ESTRAVIADO', 
    'ATRASADO',
    'REEMBOLSADO', 
    'EM_ROTA', 
    'PAGAMENTO_RECUSADO', 
    'RECUSADO_CLIENTE', 
    'RETIDO', 
    'RESERVADO', 
    'ALUGADO', 
    'BLOQUEADO', 
    'PENDENCIA_CLIENTE'
);

CREATE TYPE TIPO_PEDIDO AS ENUM ('COMPRA', 'ALUGUEL');

CREATE TABLE IF NOT EXISTS pedido
(
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario(id) ON DELETE RESTRICT,
    endereco_id BIGINT REFERENCES endereco(id) ON DELETE SET NULL,
    tipo TIPO_PEDIDO NOT NULL,
    situacao STATUS_PEDIDO NOT NULL DEFAULT 'PENDENTE',
    valor_total NUMERIC(10,2) NOT NULL CHECK (valor_total >= 0),
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_fim TIMESTAMPTZ,

    CONSTRAINT chk_valor_positivo CHECK (valor_total > 0)
);

CREATE INDEX idx_pedido_usuario_id ON pedido(usuario_id);
CREATE INDEX idx_pedido_situacao ON pedido(situacao);
CREATE INDEX idx_pedido_tipo ON pedido(tipo);
CREATE INDEX idx_pedido_data_criacao ON pedido(data_criacao);
CREATE INDEX idx_pedido_data_atualizacao ON pedido(data_atualizacao);
CREATE INDEX idx_pedido_data_fim ON pedido(data_fim);