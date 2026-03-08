package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.restaurante.RestauranteMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import java.time.LocalTime;

public class AtualizarRestauranteUseCase
    implements UseCase<AtualizarRestauranteInput, RestauranteOutput> {

  private final RestauranteGateway restauranteGateway;
  private final UsuarioGateway usuarioGateway;

  public AtualizarRestauranteUseCase(
      RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
    this.restauranteGateway = restauranteGateway;
    this.usuarioGateway = usuarioGateway;
  }

  @Override
  public RestauranteOutput executar(AtualizarRestauranteInput input) {

    if (input == null) {
      throw new RegraDeNegocioException("Input é obrigatório");
    }

    if (input.id() == null) {
      throw new RegraDeNegocioException("Id do restaurante é obrigatório");
    }

    Restaurante restaurante =
        restauranteGateway
            .findByIdRestaurante(input.id())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", input.id()));

    Restaurante.RestauranteSnapshot dadosAtuais = restaurante.snapshot();

    String nome = valorOuAtual(input.nome(), dadosAtuais.nome());
    String endereco = valorOuAtual(input.endereco(), dadosAtuais.endereco());
    TypeCozinha typeCozinha =
        input.typeCozinha() != null ? input.typeCozinha() : dadosAtuais.typeCozinha();
    LocalTime horaAbertura =
        input.horaAbertura() != null ? input.horaAbertura() : dadosAtuais.horaAbertura();
    LocalTime horaFechamento =
        input.horaFechamento() != null ? input.horaFechamento() : dadosAtuais.horaFechamento();

    Usuario dono;

    if (input.donoId() != null) {
      dono =
          usuarioGateway
              .findByIdUsuario(input.donoId())
              .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", input.donoId()));
    } else {
      dono =
          usuarioGateway
              .findByIdUsuario(dadosAtuais.donoId())
              .orElseThrow(
                  () -> new EntidadeNaoEncontradaException("Usuario", dadosAtuais.donoId()));
    }

    restaurante.atualizarCadastro(nome, endereco, typeCozinha, horaAbertura, horaFechamento, dono);

    Restaurante atualizado = restauranteGateway.saveRestaurante(restaurante);

    return RestauranteMapper.paraOutput(atualizado);
  }

  private String valorOuAtual(String valor, String atual) {
    return (valor != null && !valor.isBlank()) ? valor : atual;
  }
}
