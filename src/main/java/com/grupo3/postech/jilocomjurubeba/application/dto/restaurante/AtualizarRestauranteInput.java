package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import java.time.LocalTime;

public record AtualizarRestauranteInput(
    Long id,
    String nome,
    String endereco,
    TypeCozinha typeCozinha,
    LocalTime horaAbertura,
    LocalTime horaFechamento,
    Long donoId) {}
