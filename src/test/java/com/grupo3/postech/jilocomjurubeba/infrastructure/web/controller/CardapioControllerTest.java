package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.*;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.*;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto.AtualizarCardapioRequest;
import com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto.CriarCardapioRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CardapioControllerTest {

  @Mock private CriarCardapioUseCase criar;
  @Mock private AtualizarCardapioUseCase atualizar;
  @Mock private DeletarCardapioUseCase deletar;
  @Mock private BuscarCardapioUseCase buscar;
  @Mock private ListarCardapioUseCase listar;

  @InjectMocks private CardapioController controller;

  // ---------------------------------------------------------
  // LISTAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("Deve listar todos os cardápios quando restauranteId for nulo")
  void deveListarTodos() {

    List<CardapioOutput> lista =
        List.of(
            new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, null, 10L, true),
            new CardapioOutput(2L, "Lasanha", "desc", BigDecimal.ONE, true, null, 20L, true));

    when(listar.executar()).thenReturn(lista);

    List<CardapioOutput> result = controller.listar(null);

    assertThat(result).hasSize(2);
    verify(listar).executar();
  }

  @Test
  @DisplayName("Deve listar cardápios filtrando por restauranteId")
  void deveListarFiltradoPorRestaurante() {

    List<CardapioOutput> lista =
        List.of(
            new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, null, 10L, true),
            new CardapioOutput(2L, "Lasanha", "desc", BigDecimal.ONE, true, null, 20L, true));

    when(listar.executar()).thenReturn(lista);

    List<CardapioOutput> result = controller.listar(10L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).id()).isEqualTo(1L);
  }

  // ---------------------------------------------------------
  // BUSCAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("Deve buscar cardápio sem restauranteId")
  void deveBuscarSemRestaurante() {

    CardapioOutput out =
        new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, null, 10L, true);

    when(buscar.executar(1L)).thenReturn(out);

    CardapioOutput result = controller.buscar(1L, null);

    assertThat(result.id()).isEqualTo(1L);
  }

  @Test
  @DisplayName("Deve lançar exceção quando restauranteId não corresponde ao cardápio")
  void deveLancarErroQuandoRestauranteNaoConfere() {

    CardapioOutput out =
        new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, null, 10L, true);

    when(buscar.executar(1L)).thenReturn(out);

    assertThatThrownBy(() -> controller.buscar(1L, 99L))
        .isInstanceOf(EntidadeNaoEncontradaException.class);
  }

  // ---------------------------------------------------------
  // CRIAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("Deve criar cardápio usando restauranteId do path")
  void deveCriarComRestauranteDoPath() {

    CriarCardapioRequest req =
        new CriarCardapioRequest("Pizza", "desc", BigDecimal.TEN, false, "/foto.png", null);

    CardapioOutput out =
        new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, "/foto.png", 10L, true);

    when(criar.executar(any())).thenReturn(out);

    ResponseEntity<CardapioOutput> response = controller.criar(10L, req);

    assertThat(response.getStatusCodeValue()).isEqualTo(201);
    assertThat(response.getBody().id()).isEqualTo(1L);
  }

  @Test
  @DisplayName("Deve criar cardápio usando restauranteId do body quando path é nulo")
  void deveCriarComRestauranteDoBody() {

    CriarCardapioRequest req =
        new CriarCardapioRequest("Pizza", "desc", BigDecimal.TEN, false, "/foto.png", 20L);

    CardapioOutput out =
        new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, "/foto.png", 20L, true);

    when(criar.executar(any())).thenReturn(out);

    ResponseEntity<CardapioOutput> response = controller.criar(null, req);

    assertThat(response.getStatusCodeValue()).isEqualTo(201);
    assertThat(response.getBody().restauranteId()).isEqualTo(20L);
  }

  // ---------------------------------------------------------
  // ATUALIZAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("Deve atualizar cardápio usando restauranteId do path")
  void deveAtualizarComRestauranteDoPath() {

    AtualizarCardapioRequest req =
        new AtualizarCardapioRequest("Pizza", "desc", BigDecimal.TEN, false, "/foto.png", null);

    CardapioOutput out =
        new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, "/foto.png", 10L, true);

    when(atualizar.executar(any())).thenReturn(out);

    CardapioOutput result = controller.atualizar(1L, 10L, req);

    assertThat(result.restauranteId()).isEqualTo(10L);
  }

  @Test
  @DisplayName("Deve atualizar cardápio usando restauranteId do body quando path é nulo")
  void deveAtualizarComRestauranteDoBody() {

    AtualizarCardapioRequest req =
        new AtualizarCardapioRequest("Pizza", "desc", BigDecimal.TEN, false, "/foto.png", 30L);

    CardapioOutput out =
        new CardapioOutput(1L, "Pizza", "desc", BigDecimal.TEN, false, "/foto.png", 30L, true);

    when(atualizar.executar(any())).thenReturn(out);

    CardapioOutput result = controller.atualizar(1L, null, req);

    assertThat(result.restauranteId()).isEqualTo(30L);
  }

  // ---------------------------------------------------------
  // DELETAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("Deve deletar cardápio sem retornar conteúdo")
  void deveDeletar() {

    doNothing().when(deletar).executar(1L);

    controller.deletar(1L);

    verify(deletar).executar(1L);
  }
}
