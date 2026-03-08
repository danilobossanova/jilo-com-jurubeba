package com.grupo3.postech.jilocomjurubeba.application.mapper.restaurante;

import static org.assertj.core.api.Assertions.assertThat;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestauranteMapperTest {
  @Test
  @DisplayName("Deve mapear Restaurante para RestauranteOutput com dono")
  void deveMapearComDono() {

    Usuario dono = new Usuario();
    dono.setId(10L);

    Restaurante restaurante = new Restaurante();
    restaurante.setId(1L);
    restaurante.setNome("Restaurante Bom Sabor");
    restaurante.setEndereco("Rua A, 123");
    restaurante.setTypeCozinha(TypeCozinha.BRASILEIRA);
    restaurante.setHoraAbertura(LocalTime.of(9, 0));
    restaurante.setHoraFechamento(LocalTime.of(22, 0));
    restaurante.setDono(dono);
    restaurante.setAtivo(true);

    RestauranteOutput output = RestauranteMapper.paraOutput(restaurante);

    assertThat(output.id()).isEqualTo(1L);
    assertThat(output.nome()).isEqualTo("Restaurante Bom Sabor");
    assertThat(output.endereco()).isEqualTo("Rua A, 123");
    assertThat(output.typeCozinha()).isEqualTo(TypeCozinha.BRASILEIRA);
    assertThat(output.horaAbertura()).isEqualTo(LocalTime.of(9, 0));
    assertThat(output.horaFechamento()).isEqualTo(LocalTime.of(22, 0));
    assertThat(output.donoId()).isEqualTo(10L);
    assertThat(output.ativo()).isTrue();
  }

  @Test
  @DisplayName("Deve mapear Restaurante para RestauranteOutput sem dono")
  void deveMapearSemDono() {

    Restaurante restaurante = new Restaurante();
    restaurante.setId(2L);
    restaurante.setNome("Sem Dono Lanches");
    restaurante.setEndereco("Av. Central, 500");
    restaurante.setTypeCozinha(TypeCozinha.FAST_FOOD);
    restaurante.setHoraAbertura(LocalTime.of(10, 0));
    restaurante.setHoraFechamento(LocalTime.of(20, 0));
    restaurante.setDono(null);
    restaurante.setAtivo(false);

    RestauranteOutput output = RestauranteMapper.paraOutput(restaurante);

    assertThat(output.id()).isEqualTo(2L);
    assertThat(output.nome()).isEqualTo("Sem Dono Lanches");
    assertThat(output.endereco()).isEqualTo("Av. Central, 500");
    assertThat(output.typeCozinha()).isEqualTo(TypeCozinha.FAST_FOOD);
    assertThat(output.horaAbertura()).isEqualTo(LocalTime.of(10, 0));
    assertThat(output.horaFechamento()).isEqualTo(LocalTime.of(20, 0));
    assertThat(output.donoId()).isNull();
    assertThat(output.ativo()).isFalse();
  }
}
