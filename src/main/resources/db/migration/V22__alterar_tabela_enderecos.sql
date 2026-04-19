ALTER TABLE endereco DROP COLUMN usuario_id;

ALTER TABLE endereco 
ADD COLUMN usuario_id UUID DEFAULT (uuidv7());

UPDATE endereco SET usuario_id = '33476ff5-27cf-4113-ab83-7486e9d7e2dd' WHERE id = 1;
UPDATE endereco SET usuario_id = '3f3b406b-b6b2-4a13-afe6-35746410cd86' WHERE id = 2;
UPDATE endereco SET usuario_id = 'cccdefd5-bbae-4b46-9ac5-425ea28fbf5f' WHERE id = 3;
UPDATE endereco SET usuario_id = '76ea733f-617a-4a17-b425-bba6cfd5a21f' WHERE id = 4;
UPDATE endereco SET usuario_id = '0f0b3ab2-cacb-47d9-a00c-68e2340da9c9' WHERE id = 5;
UPDATE endereco SET usuario_id = '0f0b3ab2-cacb-47d9-a00c-68e2340da9c9' WHERE id = 6;