INSERT INTO ferramentas(
	nome,
    modelo,
    fabricante,
    tipo,
    descricao,
	caracteristicas,
    itens_inclusos,
	lista_imagens,
    disponibilidade,
    preco_aluguel,
    preco_venda
) VALUES 
-- 1. Motosserra
('Motosserra à Gasolina', 'FORTGPRO-FG9122', 'FortG', 'Motosserra', 
	'Motosserra à gasolina FG9122 com sabre de 20” é indicada para realizar cortes em árvores ou produtos de madeira. Ideal para uso profissional. Conta com motor 2T potente de 63 cilindradas permitindo maior eficácia e resistência', 
	'{
		"especificacoes_tecnicas": {
			"motor": {
				"tipo": "2 Tempos",
				"potencia": "2.4kW / 3HP",
				"cilindrada": "63.3cc",
				"rotacao": {
					"maxima_alta": "10000 rpm",
					"maxima_baixa": "2800 rpm"
				}
			},
			"corte": {
				"comprimento_maximo": "480mm",
				"sabre": {
					"tipo": "Dentado",
					"tamanho_polegadas": "20\"",
					"comprimento_mm": "500mm"
				},
				"corrente": {
					"tipo": "21BP/K2",
					"roda_dentada": "7T x 0,325\""
				}
			},
			"combustivel_e_oleo": {
				  "combustivel": {
					"tipo": "Gasolina + Óleo 2T",
					"mistura": "25:1",
					"tanque_capacidade": "500ml"
				  },
					"oleo_corrente": {
						"tipo": "2T API-TC Mineral - SAE 90",
						"tanque_capacidade": "260ml",
						"alimentacao": "Bomba automática com ajuste"
					}
				},
				"ignicao_e_carburacao": {
					"carburacao": "Diafragma",
					"ignicao": "Bobina",
					"vela": "JBY 7D"
				},
				"peso_e_dimensoes": {
					"peso_com_sabre_e_corrente": "5.5kg",
					"dimensoes": "480 x 260 x 300mm"
				}
			}
		}'::jsonb, 
	'["Motosserra",
		"Chave vela combinada",
		"Chave de fenda",
		"Chave Hexagonal",
		"Medidor de combustível",
		"Cão de corte",
	"Protetor de Sabre"]'::jsonb, 
	'["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb, 
	true, 45.90, 608.78),
-- 2. Roçadeira Lateral
('Roçadeira à Gasolina', 'BC430B', 'Toyama', 'Roçadeira',
 'Roçadeira lateral 43cc com eixo rígido e carretel de nylon',
 '{"motor": {"potencia": "1.7HP", "cilindrada": "43cc"}, "corte": {"largura": "430mm"}}'::jsonb,
 '["Carretel nylon", "Lâmina 3 pontas", "Cinto dupla", "Óculos", "Protetor auditivo"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 38.00, 789.90),

-- 3. Furadeira de Impacto 1/2"
('Furadeira de Impacto', 'GSB 13 RE', 'Bosch', 'Furadeira',
 'Furadeira profissional 650W com reversão e controle de velocidade',
 '{"potencia": "650W", "mandril": "13mm", "impacto": true, "voltagem": "220V"}'::jsonb,
 '["Chave de mandril", "Punho auxiliar", "Limitador de profundidade", "Maleta"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 22.00, 329.90),

-- 4. Martelete Perfurador/Rompedor
('Martelete SDS-Plus', 'GBH 2-24 D', 'Bosch', 'Martelete',
 'Martelete 790W com 3 funções: furar, romper e talhar',
 '{"potencia": "790W", "energia_impacto": "2.7J", "sds_plus": true}'::jsonb,
 '["Ponteiro", "Talhadeira", "3 brocas", "Maleta"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 65.00, 1189.00),

-- 5. Esmerilhadeira Angular 4.1/2"
('Esmerilhadeira', 'GWS 9-125 S', 'Bosch', 'Esmerilhadeira',
 'Esmerilhadeira 900W com controle de rotação e proteção',
 '{"potencia": "900W", "disco": "115mm", "controle_velocidade": true}'::jsonb,
 '["Chave de aperto", "Flange", "Protetor de disco"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 25.00, 429.90),

-- 6. Serra Circular 7.1/4"
('Serra Circular', 'GKS 190', 'Bosch', 'Serra Circular',
 'Serra circular 1400W para cortes profundos em madeira',
 '{"potencia": "1400W", "disco": "184mm", "profundidade_corte": "67mm"}'::jsonb,
 '["Disco de serra", "Guia paralela", "Chave allen"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 35.00, 589.90),

-- 7. Motosserra Elétrica
('Motosserra Elétrica', 'UC4051A', 'Makita', 'Motosserra',
 'Motosserra elétrica 1800W com lubrificação automática',
 '{"potencia": "1800W", "sabre": "40cm", "tensao": "220V"}'::jsonb,
 '["Sabre", "Corrente", "Capa protetora"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 42.00, 989.90),

-- 8. Gerador a Gasolina 3kVA
('Gerador a Gasolina', 'TG3800CX', 'Toyama', 'Gerador',
 'Gerador portátil 4 tempos com partida manual',
 '{"potencia": "3000W", "motor": "4 tempos", "tanque": "15L"}'::jsonb,
 '["Rodas", "Manual", "Chave de vela"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 120.00, 2490.00),

-- 9. Lavadora Alta Pressão
('Lavadora de Alta Pressão', 'J7000', 'Jacto', 'Lavadora',
 'Lavadora 2200W com 170 bar de pressão',
 '{"potencia": "2200W", "pressao": "170 bar", "vazao": "420 L/h"}'::jsonb,
 '["Pistola", "Lança", "Mangueira 5m", "Bicos"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 55.00, 1190.00),

-- 10. Betoneira 400L
('Betoneira Elétrica', 'CS400', 'Menegotti', 'Betoneira',
 'Betoneira profissional com motor 2HP e cremalheira',
 '{"capacidade": "400L", "motor": "2HP", "voltagem": "220V"}'::jsonb,
 '["Manual", "Chave de montagem"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 180.00, 3890.00),

-- 11. Plaina Elétrica
('Plaina Desempenadeira', 'KP0800', 'Makita', 'Plaina',
 'Plaina 620W para acabamento fino em madeira',
 '{"potencia": "620W", "largura_corte": "82mm"}'::jsonb,
 '["2 lâminas", "Guia paralela", "Saco coletor"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 28.00, 689.90),

-- 12. Compressor de Ar 50L
('Compressor de Ar', 'CSI 8.5/50', 'Schulz', 'Compressor',
 'Compressor 2HP com tanque 50L e pressão máxima 10 bar',
 '{"potencia": "2HP", "tanque": "50L", "pressao": "10 bar"}'::jsonb,
 '["Mangueira", "Pistola de pintura", "Calibrador"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 85.00, 1690.00),

-- 13. Serra Mármore
('Serra Mármore', 'TC410', 'Makita', 'Serra Mármore',
 'Serra mármore 1300W para corte de pisos e porcelanato',
 '{"potencia": "1300W", "disco": "110mm", "refrigeracao": "água"}'::jsonb,
 '["Disco diamantado", "Chave", "Guia"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 32.00, 489.90),

-- 14. Parafusadeira/Furadeira 18V
('Parafusadeira Impacto 18V', 'DCD796', 'DeWalt', 'Parafusadeira',
 'Parafusadeira à bateria 18V com impacto e LED',
 '{"voltagem": "18V", "torque": "70Nm", "bateria": "2.0Ah"}'::jsonb,
 '["2 baterias", "Carregador", "Maleta", "Ponteiras"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 58.00, 1289.00),

-- 15. Nível a Laser
('Nível a Laser Verde', 'GLL 3-80', 'Bosch', 'Nível Laser',
 'Nível a laser verde 360° com 80m de alcance',
 '{"linhas": "3x360°", "cor": "verde", "alcance": "80m"}'::jsonb,
 '["Suporte", "Alvo", "Óculos", "Maleta"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 95.00, 2290.00),

-- 16. Politriz Angular
('Politriz 7"', 'GPO 12 CE', 'Bosch', 'Politriz',
 'Politriz profissional com controle de velocidade',
 '{"potencia": "1250W", "disco": "180mm"}'::jsonb,
 '["Boina", "Disco de apoio", "Chave"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 40.00, 789.90),

-- 17. Martelete Rompedor 15kg
('Rompedor Elétrico', 'GSH 11 E', 'Bosch', 'Rompedor',
 'Rompedor 1500W com 15kg de força',
 '{"potencia": "1500W", "energia_impacto": "25J"}'::jsonb,
 '["Ponteiro", "Talhadeira", "Maleta"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 180.00, 3890.00),

-- 18. Lixadeira Orbital
('Lixadeira Orbital', 'GSS 140', 'Bosch', 'Lixadeira',
 'Lixadeira orbital 220W com sistema micro-filtro',
 '{"potencia": "220W", "tamanho_base": "114x140mm"}'::jsonb,
 '["Lixa", "Saco coletor", "Adaptador aspiração"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 18.00, 329.90),

-- 19. Inversora de Solda
('Inversora de Solda', 'RIV 222', 'Vonder', 'Inversora',
 'Inversora 200A bivolt com display digital',
 '{"corrente": "200A", "eletrodo": "1.6 a 4.0mm"}'::jsonb,
 '["Garra terra", "Porta eletrodo", "Máscara", "Escova"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 65.00, 989.90),

-- 20. Cortadora de Grama Elétrica
('Cortador de Grama', 'CE-200', 'Tramontina', 'Cortador de Grama',
 'Cortador elétrico 2000W com recolhedor',
 '{"potencia": "2000W", "corte": "40cm", "recolhedor": "35L"}'::jsonb,
 '["Recolhedor", "Manual"]'::jsonb,
 '["https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"]'::jsonb,
 true, 38.00, 689.90);