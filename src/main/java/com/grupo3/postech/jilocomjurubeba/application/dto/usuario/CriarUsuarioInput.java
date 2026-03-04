package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;


public record CriarUsuarioInput(
    String nome,
    String cpf,
    String email,
    String telefone,
    Long tipoUsuarioId,
    String senha
) {}
