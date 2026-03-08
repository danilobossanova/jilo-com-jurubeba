package com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante;

import java.util.List;
import java.util.Optional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;

public interface RestauranteGateway {

    Restaurante saveRestaurante(Restaurante restaurante);

    Optional<Restaurante> findByIdRestaurante(Long id);

    List<Restaurante> findAllRestaurante();

    void deleteRestaurante(Long id);

    Optional<Restaurante> findByNome(String nome);
}
