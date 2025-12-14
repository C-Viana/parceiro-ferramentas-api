package com.parceiroferramentas.api.parceiro_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	info = @Info(
		title = "Parceiro Ferramentas API",
		version = "0.0.1-SNAPSHOT",
		description = "Documentação da API da loja Parceiro Ferramentas para venda e aluguel"
	)
)
@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}

}
