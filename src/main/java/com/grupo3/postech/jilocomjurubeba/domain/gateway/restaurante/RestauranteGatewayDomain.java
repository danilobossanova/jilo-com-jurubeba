package com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteGatewayDomain {

    Restaurante saveRestaurante(Restaurante restaurante);

    Optional<Restaurante> findByIdRestaurante(Long id);

    List<Restaurante> findAllRestaurante();

    void deleteRestaurante(Long id);

    Optional<Restaurante> findByNome(String nome);
}



