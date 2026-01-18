INSERT INTO pagamento 
(pedido_id, forma_pagamento, valor, situacao, data_criacao, data_atualizacao, codigo_transacao, detalhes) 
VALUES 
(1, 'PIX', 1398.68, 'CANCELADO', 
    '2026-01-15T11:00:42.746847600Z'::timestamptz, 
    '2026-01-15T11:05:42.746847600Z'::timestamptz, 
    'a8385630-2c20-47c8-85d6-eb159711fa77', 
    '{ "nome": "Lindosvaldo Melo da Silva", "chave": "46854810201" }'::jsonb),
(2, 'PIX', 1398.68, 'APROVADO', 
    '2026-01-15T12:00:42.746847600Z'::timestamptz, 
    '2026-01-15T12:02:42.746847600Z'::timestamptz, 
    '75652094-6033-4a68-884a-c1340109a5db', 
    '{ "nome": "Lindosvaldo Melo da Silva", "chave": "46854810201" }'::jsonb),
(3, 'DEBITO', 760.00, 'APROVADO', 
    '2026-01-17T15:01:42.746847600Z'::timestamptz, 
    '2026-01-17T15:02:42.746847600Z'::timestamptz, 
    'a7549545-ff8a-47e5-9442-9b94fc85f11e', 
    '{ "forma_pagamento": "DEBITO", "valor": 134.00, "detalhes": { "nome": "Lindosvaldo Melo da Silva", "documento": "46854810201", "banco": "Nubank", "agencia": "00001", "conta": "10852432", "digito": "2" } }'::jsonb);
