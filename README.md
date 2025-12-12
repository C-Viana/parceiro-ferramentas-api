# Parceiro Ferramentas API

API RESTful para locação e venda de ferramentas profissionais.

Tecnologias:
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
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── config/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── controller/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── dto/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── exception/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── model/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── repository/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── service/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── Startup.java/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;└── resources/<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── application.yml<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── application-dev.yml<br>
│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;└── application-prod.yml<br>
├── docker/<br>
│&emsp;&emsp;&emsp;└── Dockerfile<br>
├── docker-compose.yml<br>
├── pom.xml (ou build.gradle)<br>
└── README.md ← com badge de build, Swagger, etc.<br>