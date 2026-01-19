# Parceiro Ferramentas API
[![CI](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
[![CI Parceiro Ferramentas API](https://github.com/C-Viana/parceiro-ferramentas-api/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/C-Viana/parceiro-ferramentas-api/actions/workflows/maven-publish.yml)

API RESTful para locação e venda de ferramentas profissionais.

# Tecnologias:
- Java 25
- Spring Boot 4.0.0
- PostgreSQL 18
- Docker/Docker Compose
- Spring Data JPA + JSONB
- Swagger (OpenAPI)

Documentação: http://localhost:8080/swagger-ui.html



# Estrutura do projeto
parceiro-ferramentas-api/<br>
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
