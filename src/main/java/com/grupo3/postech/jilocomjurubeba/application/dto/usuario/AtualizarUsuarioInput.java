package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;

public record AtualizarUsuarioInput(
    Long id, String nome, String cpf, String email, String telefone, Long tipoUsuarioId) {}
