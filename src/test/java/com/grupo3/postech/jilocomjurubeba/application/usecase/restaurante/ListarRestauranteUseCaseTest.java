package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListarRestauranteUseCaseTest {

  @Mock private RestauranteGateway restauranteGateway;

  private ListarRestauranteUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ListarRestauranteUseCase(restauranteGateway);
  }

  @Test
  @DisplayName("Deve listar restaurantes com sucesso")
  void deveListarComSucesso() {
    when(restauranteGateway.findAllRestaurante())
        .thenReturn(
            List.of(
                RestauranteTestFactory.criarRestauranteValido(),
                RestauranteTestFactory.criarRestauranteInativo()));

    List<RestauranteOutput> output = useCase.executar();

    assertThat(output).hasSize(2);
    assertThat(output.get(0).id()).isEqualTo(1L);
    assertThat(output.get(1).ativo()).isFalse();
  }
}
