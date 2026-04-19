ALTER TABLE carrinho_itens DROP COLUMN usuario_id;

ALTER TABLE carrinho_itens 
ADD COLUMN usuario_id UUID DEFAULT (uuidv7());

UPDATE carrinho_itens SET usuario_id = '76ea733f-617a-4a17-b425-bba6cfd5a21f' WHERE id = 1;
UPDATE carrinho_itens SET usuario_id = '76ea733f-617a-4a17-b425-bba6cfd5a21f' WHERE id = 2;
UPDATE carrinho_itens SET usuario_id = '0f0b3ab2-cacb-47d9-a00c-68e2340da9c9' WHERE id = 3;