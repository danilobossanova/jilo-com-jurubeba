package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;

import java.time.LocalTime;

public record RestauranteOutput(Long id,
                                String nome,
                                String endereco,
                                TypeCozinha typeCozinha,
                                LocalTime horaAbertura,
                                LocalTime horaFechamento,
                                Usuario dono,
                                boolean ativo) {
}
