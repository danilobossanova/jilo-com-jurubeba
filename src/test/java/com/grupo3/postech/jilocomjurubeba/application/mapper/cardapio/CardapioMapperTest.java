package com.grupo3.postech.jilocomjurubeba.application.mapper.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CardapioMapperTest {

    @Test
    @DisplayName("Deve mapear Cardapio para CardapioOutput com restaurante")
    void deveMapearComRestaurante() {

        Restaurante restaurante = new Restaurante();
        restaurante.setId(5L);

        Cardapio cardapio = new Cardapio();
        cardapio.setId(1L);
        cardapio.setNome("Pizza Calabresa");
        cardapio.setDescricao("Pizza com calabresa e cebola");
        cardapio.setPreco(new BigDecimal("39.90"));
        cardapio.setApenasNoLocal(false);
        cardapio.setCaminhoFoto("/img/pizza.png");
        cardapio.setRestaurante(restaurante);
        cardapio.setAtivo(true);

        CardapioOutput output = CardapioMapper.toOutput(cardapio);

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("Pizza Calabresa");
        assertThat(output.descricao()).isEqualTo("Pizza com calabresa e cebola");
        assertThat(output.preco()).isEqualByComparingTo("39.90");
        assertThat(output.apenasNoLocal()).isFalse();
        assertThat(output.caminhoFoto()).isEqualTo("/img/pizza.png");
        assertThat(output.restauranteId()).isEqualTo(5L);
        assertThat(output.ativo()).isTrue();
    }

    @Test
    @DisplayName("Deve mapear Cardapio para CardapioOutput sem restaurante")
    void deveMapearSemRestaurante() {

        Cardapio cardapio = new Cardapio();
        cardapio.setId(2L);
        cardapio.setNome("Hamburguer");
        cardapio.setDescricao("Hamburguer artesanal");
        cardapio.setPreco(new BigDecimal("25.00"));
        cardapio.setApenasNoLocal(true);
        cardapio.setCaminhoFoto("/img/burger.png");
        cardapio.setRestaurante(null);
        cardapio.setAtivo(false);

        CardapioOutput output = CardapioMapper.toOutput(cardapio);

        assertThat(output.id()).isEqualTo(2L);
        assertThat(output.nome()).isEqualTo("Hamburguer");
        assertThat(output.descricao()).isEqualTo("Hamburguer artesanal");
        assertThat(output.preco()).isEqualByComparingTo("25.00");
        assertThat(output.apenasNoLocal()).isTrue();
        assertThat(output.caminhoFoto()).isEqualTo("/img/burger.png");
        assertThat(output.restauranteId()).isNull();
        assertThat(output.ativo()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar null quando Cardapio for null")
    void deveRetornarNullQuandoCardapioForNull() {

        CardapioOutput output = CardapioMapper.toOutput(null);

        assertThat(output).isNull();
    }
}
