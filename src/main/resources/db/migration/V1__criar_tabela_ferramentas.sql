CREATE SEQUENCE IF NOT EXISTS ferramentas_id_seq START WITH 1 INCREMENT BY 1;

-- Table: public.ferramentas
-- DROP TABLE IF EXISTS public.ferramentas;

CREATE TABLE IF NOT EXISTS public.ferramentas
(
    id integer NOT NULL DEFAULT nextval('ferramentas_id_seq'::regclass),
    nome text COLLATE pg_catalog."default" NOT NULL,
    modelo text COLLATE pg_catalog."default" NOT NULL,
    fabricante text COLLATE pg_catalog."default" NOT NULL,
    tipo text COLLATE pg_catalog."default" NOT NULL,
    descricao text COLLATE pg_catalog."default",
    caracteristicas jsonb,
    itens_inclusos jsonb,
    lista_imagens jsonb,
    disponibilidade boolean DEFAULT false,
    preco_aluguel numeric(10,2) NOT NULL,
    preco_venda numeric(10,2) NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ferramentas_pkey PRIMARY KEY (id)
)