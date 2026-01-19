# Parceiro Ferramentas API

API RESTful completa para **locação e venda de ferramentas** profissionais, desenvolvida como projeto de portfólio durante minha transição de QA para desenvolvedor backend Java.

![Badge de Status](https://github.com/C-Viana/parceiro-ferramentas-api/actions/workflows/maven-publish.yml/badge.svg)
![Badge de Java](https://img.shields.io/badge/Java-21-blue)
![Badge de Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-green)
![Badge de PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)

## Visão Geral

Sistema backend que permite:
- Cadastro e autenticação de usuários com JWT e roles (CLIENTE, GERENTE, ADMIN)
- Gerenciamento de ferramentas (com características técnicas flexíveis via JSONB)
- Carrinho de compras persistente
- Criação de pedidos de **compra** ou **aluguel**
- Simulação de pagamento (com estratégia extensível para PIX, cartão, boleto, etc.)
- Documentação interativa com Swagger/OpenAPI
- Testes automatizados (unitários e integração)
- Containerização com Docker e CI/CD no GitHub Actions

Deploy ao vivo:  
🔗 https://parceiro-ferramentas-api-production.up.railway.app/swagger-ui/index.html

## Tecnologias Principais

- **Backend**: Java 21, Spring Boot 4.0
- **Segurança**: Spring Security + JWT
- **Banco de dados**: PostgreSQL 18 (com JSONB para especificações flexíveis)
- **ORM**: Spring Data JPA + Hibernate
- **Testes**: JUnit 5, Mockito (unitário), RestAssured + Testcontainers (integração)
- **Documentação**: SpringDoc OpenAPI (Swagger UI)
- **Infraestrutura**: Docker, Docker Compose, GitHub Actions (CI)
- **Deploy**: Railway (free tier)

## Funcionalidades Implementadas

- Autenticação e autorização com JWT (login, refresh token, roles)
- CRUD completo de ferramentas (com JSONB para características e itens inclusos)
- Carrinho de compras persistente por usuário
- Criação de pedidos (compra ou aluguel) com validação de estoque/endereço
- Simulação de pagamento com Strategy Pattern
- Paginação e filtros case-insensitive
- Tratamento global de exceções
- Documentação Swagger completa e customizada

## Como Rodar Localmente

### Pré-requisitos
- Docker & Docker Compose
- Java 21 (ou use o Maven Wrapper)

### Passos
1. **Clone o repositório:**
   ```bash
   git clone https://github.com/C-Viana/parceiro-ferramentas-api.git
   cd parceiro-ferramentas-api
2. **Inicie os containers (PostgreSQL + API):**
    ```bash
    docker compose up -d --build
3. **Acesse:**
- Swagger: http://localhost:8080/swagger-ui/index.html
- Banco (pgAdmin ou DBeaver): localhost:5432 (user: parceiro_user, senha: no .env)
4. **Credenciais iniciais (criadas via migration ou script):**
- Admin: username 80690571, senha admin123
- Cliente: username CLIE0001, senha cliente123
5. **Variáveis de Ambiente (arquivo .env):**
<br>Faça as configurações do arquivo application.yml (parceiro-ferramentas-api\src\main\resources\application.yml)
- DB_HOST
- DB_PORT
- DB_NAME
- DB_USER
- DB_PASSWORD
- JWT_SECRET
6. **Estrutura do Projeto:**
    <br>parceiro-ferramentas-api/<br>
    ├── src/<br>
    │&emsp;&emsp;└── main/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;├── java/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;└── br/com/parceiroferramentas/api/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── auth/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── config/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── controller/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── dto/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── enums/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── exception/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── mapper/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── model/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── pagamento/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── pedido/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── repository/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── service/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── Startup.java<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;└── resources/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── db/migrations/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── application.yml<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── application-dev.yml<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;└── application-prod.yml<br>
    │&emsp;&emsp;└── test/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;├── java/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;└── br/com/parceiroferramentas/api/parceiro_api/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── config/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── data/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── integration/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── unit/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── repository/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── service/<br>
    │&emsp;&emsp;&emsp;&emsp;&emsp;└── resources/<br>
    ├── docker/<br>
    │&emsp;&emsp;&emsp;└── Dockerfile<br>
    ├── docker-compose.yml<br>
    ├── pom.xml (ou build.gradle)<br>
    └── README.md ← com badge de build, Swagger, etc.<br>

## Licença
    Feito com ☕ e persistência por Carlos Eduardo de Souza Viana
    LinkedIn: https://www.linkedin.com/in/carlos-eds-viana/
