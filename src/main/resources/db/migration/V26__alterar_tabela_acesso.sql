ALTER TABLE acesso
DROP CONSTRAINT acesso_id_permissao_fkey;

ALTER TABLE acesso 
ALTER COLUMN id_usuario TYPE UUID USING (uuidv7());

TRUNCATE TABLE acesso RESTART IDENTITY CASCADE;

ALTER TABLE acesso
ADD CONSTRAINT acesso_id_permissao_fkey
FOREIGN KEY (id_usuario)
REFERENCES usuario(id);

INSERT INTO acesso (id_usuario, id_permissao) VALUES
	('33476ff5-27cf-4113-ab83-7486e9d7e2dd'::UUID, 1),
	('33476ff5-27cf-4113-ab83-7486e9d7e2dd'::UUID, 2),
	('33476ff5-27cf-4113-ab83-7486e9d7e2dd'::UUID, 3),
	('33476ff5-27cf-4113-ab83-7486e9d7e2dd'::UUID, 4),
	('3f3b406b-b6b2-4a13-afe6-35746410cd86'::UUID, 2),
	('cccdefd5-bbae-4b46-9ac5-425ea28fbf5f'::UUID, 3),
	('76ea733f-617a-4a17-b425-bba6cfd5a21f'::UUID, 4),
	('0f0b3ab2-cacb-47d9-a00c-68e2340da9c9'::UUID, 4);