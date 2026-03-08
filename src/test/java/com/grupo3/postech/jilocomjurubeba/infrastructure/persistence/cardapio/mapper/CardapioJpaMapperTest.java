package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.factory.CardapioTestFactory;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CardapioJpaMapperTest {

  @Test
  @DisplayName("Deve converter Cardapio (domínio) para CardapioJpaEntity")
  void deveConverterParaJpa() {

    Cardapio cardapioDomain = CardapioTestFactory.criarCardapioValido();
    Restaurante restauranteDomain = cardapioDomain.getRestaurante();

    RestauranteJpaEntity restauranteJpa = new RestauranteJpaEntity();
    restauranteJpa.setId(restauranteDomain.getId());

    CardapioJpaEntity jpa = CardapioJpaMapper.toJpa(cardapioDomain, restauranteJpa);

    assertThat(jpa.getId()).isEqualTo(cardapioDomain.getId());
    assertThat(jpa.getNome()).isEqualTo(cardapioDomain.getNome());
    assertThat(jpa.getDescricao()).isEqualTo(cardapioDomain.getDescricao());
    assertThat(jpa.getPreco()).isEqualByComparingTo(cardapioDomain.getPreco());
    assertThat(jpa.isApenasNoLocal()).isEqualTo(cardapioDomain.isApenasNoLocal());
    assertThat(jpa.getCaminhoFoto()).isEqualTo(cardapioDomain.getCaminhoFoto());
    assertThat(jpa.isAtivo()).isEqualTo(cardapioDomain.isAtivo());
    assertThat(jpa.getRestaurante().getId()).isEqualTo(restauranteDomain.getId());
  }

  @Test
  @DisplayName("Deve converter CardapioJpaEntity para Cardapio (domínio)")
  void deveConverterParaDomain() {

    Restaurante restauranteDomain = RestauranteTestFactory.criarRestauranteValido();

    RestauranteJpaEntity restauranteJpa = new RestauranteJpaEntity();
    restauranteJpa.setId(restauranteDomain.getId());

    CardapioJpaEntity jpa = new CardapioJpaEntity();
    jpa.setId(2L);
    jpa.setNome("HAMBURGUER");
    jpa.setDescricao("HAMBURGUER ARTESANAL");
    jpa.setPreco(java.math.BigDecimal.valueOf(25));
    jpa.setApenasNoLocal(true);
    jpa.setCaminhoFoto("/img/burger.png");
    jpa.setAtivo(false);
    jpa.setRestaurante(restauranteJpa);

    Cardapio domain = CardapioJpaMapper.toDomain(jpa, restauranteDomain);

    assertThat(domain.getId()).isEqualTo(2L);
    assertThat(domain.getNome()).isEqualTo("HAMBURGUER");
    assertThat(domain.getDescricao()).isEqualTo("HAMBURGUER ARTESANAL");
    assertThat(domain.getPreco()).isEqualByComparingTo("25");
    assertThat(domain.isApenasNoLocal()).isTrue();
    assertThat(domain.getCaminhoFoto()).isEqualTo("/img/burger.png");
    assertThat(domain.isAtivo()).isFalse();
    assertThat(domain.getRestaurante()).isEqualTo(restauranteDomain);
  }
}
