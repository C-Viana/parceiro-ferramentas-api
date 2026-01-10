INSERT INTO permissao (descricao) VALUES
	('ADMIN'),
	('GERENTE'),
	('VENDEDOR'),
	('CLIENTE');

INSERT INTO usuario (username, nome, senha, account_non_expired, account_non_locked, credentials_non_expired, enabled) VALUES
	('80690571', 'Carlos Eduardo de Souza Viana', '$2a$10$Ur9TKcWfC2oVKBF9jOnaiOxl/FhGjHIdzmG158wxvzrGk9UDqxJsS', true, true, true, true),
	('GERE0001', 'Fernando Oliveira de Castro', '$2a$10$Ur9TKcWfC2oVKBF9jOnaiOxl/FhGjHIdzmG158wxvzrGk9UDqxJsS', true, true, true, true),
	('VEND0001', 'Maurício Matos Guttenberg', '$2a$10$Ur9TKcWfC2oVKBF9jOnaiOxl/FhGjHIdzmG158wxvzrGk9UDqxJsS', true, true, true, true),
	('CLIE0001', 'Lindosvaldo Melo da Silva', '$2a$10$Ur9TKcWfC2oVKBF9jOnaiOxl/FhGjHIdzmG158wxvzrGk9UDqxJsS', true, true, true, true),
	('CLIE0002', 'Silvana de Lima Ferreira', '$2a$10$Ur9TKcWfC2oVKBF9jOnaiOxl/FhGjHIdzmG158wxvzrGk9UDqxJsS', true, true, true, true);

INSERT INTO acesso (id_usuario, id_permissao) VALUES
	(1, 1),
	(1, 2),
	(1, 3),
	(1, 4),
	(2, 2),
	(3, 3),
	(4, 4),
	(5, 4);