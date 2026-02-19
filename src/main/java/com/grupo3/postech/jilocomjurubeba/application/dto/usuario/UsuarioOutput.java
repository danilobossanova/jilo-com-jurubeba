package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;


import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

import java.util.List;

public record UsuarioOutput(Long id,
                            String nome,
                            Cpf cpf,
                            Email email,
                            String telefone,
                            TypeUsuario typeUsuario,
                            List<Restaurante> restaurante,
                            boolean ativo) { }
