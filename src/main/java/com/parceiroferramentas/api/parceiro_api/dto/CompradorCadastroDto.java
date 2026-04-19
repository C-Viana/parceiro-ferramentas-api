package com.parceiroferramentas.api.parceiro_api.dto;

import java.time.LocalDate;
import java.util.List;

import org.intellij.lang.annotations.RegExp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.parceiroferramentas.api.parceiro_api.model.Endereco;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompradorCadastroDto(
    @NotBlank(message = "Documento não pode estar vazio")
    @NotNull(message = "Documento não pode ser nulo")
    @Pattern(regexp = "^(?:\\d{11}|\\d{14})$")
    String documento,

    @NotBlank(message = "Nome completo não pode estar vazio")
    @NotNull(message = "Nome completo não pode ser nulo")
    @Size(min = 6, max = 50, message = "A quantidade de caracteres informados não atende ao esperado para um nome completo")
    @RegExp(prefix = "^\\D+\\s{1}[\\D+\\s{1}]+\\D$")
    String nome,
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate nascimento,

    @NotBlank(message = "E-mail não pode estar vazio")
    @NotNull(message = "E-mail não pode ser nulo")
    @Email
    String email,
    
    @NotBlank(message = "Telefone de contato não pode estar vazio")
    @NotNull(message = "Telefone de contato não pode ser nulo")
    @Pattern(regexp = "\\+?[0-9]{10,13}")
    String phone,
    
    @NotBlank(message = "Senha não pode estar vazia")
    @NotNull(message = "Senha não pode ser nula")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    String senha,
    
    @NotNull(message = "Pelo menos um endereço deve ser informado")
    List<Endereco> enderecos
) {}
