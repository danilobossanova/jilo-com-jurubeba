package com.grupo3.postech.jilocomjurubeba.domain.enumroles;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TypeUsuarioTest {

  @Test
  @DisplayName("Deve identificar dono de restaurante")
  void deveIdentificarDonoRestaurante() {

    TypeUsuario tipo = TypeUsuario.DONO_RESTAURANTE;

    assertThat(tipo.isDono()).isTrue();
    assertThat(tipo.isCliente()).isFalse();
  }

  @Test
  @DisplayName("Deve identificar cliente")
  void deveIdentificarCliente() {

    TypeUsuario tipo = TypeUsuario.CLIENTE;

    assertThat(tipo.isCliente()).isTrue();
    assertThat(tipo.isDono()).isFalse();
  }

  @Test
  @DisplayName("Deve conter apenas dois tipos de usuario")
  void deveConterApenasDoisTipos() {

    TypeUsuario[] tipos = TypeUsuario.values();

    assertThat(tipos).hasSize(2);
    assertThat(tipos).containsExactlyInAnyOrder(TypeUsuario.CLIENTE, TypeUsuario.DONO_RESTAURANTE);
  }

  @Test
  @DisplayName("Deve converter enum para string corretamente")
  void deveConverterParaString() {

    assertThat(TypeUsuario.CLIENTE.name()).isEqualTo("CLIENTE");
    assertThat(TypeUsuario.DONO_RESTAURANTE.name()).isEqualTo("DONO_RESTAURANTE");
  }

  @Test
  @DisplayName("Deve converter string para enum")
  void deveConverterDeString() {

    TypeUsuario tipo = TypeUsuario.valueOf("CLIENTE");

    assertThat(tipo).isEqualTo(TypeUsuario.CLIENTE);
  }
}
