package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

import java.time.LocalTime;

public class AtualizarRestauranteUseCase implements UseCase<AtualizarRestauranteInput, RestauranteOutput> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;
    private final UsuarioGatewayDomain usuarioGatewayDomain;

    public AtualizarRestauranteUseCase(
        RestauranteGatewayDomain restauranteGatewayDomain,
        UsuarioGatewayDomain usuarioGatewayDomain
    ) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
        this.usuarioGatewayDomain = usuarioGatewayDomain;
    }

    @Override
    public RestauranteOutput executar(AtualizarRestauranteInput input) {
        if (input == null) {
            throw new RegraDeNegocioException("Input é obrigatório");
        }
        if (input.id() == null) {
            throw new RegraDeNegocioException("Id do restaurante é obrigatório");
        }

        Restaurante restaurante = restauranteGatewayDomain
            .findByIdRestaurante(input.id())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", input.id()));

        // PATCH: se vier null/vazio, mantém o atual
        String nome = valorOuAtual(input.nome(), restaurante.getNome());
        String endereco = valorOuAtual(input.endereco(), restaurante.getEndereco());
        TypeCozinha typeCozinha = (input.typeCozinha() != null) ? input.typeCozinha() : restaurante.getTypeCozinha();
        LocalTime horaAbertura = (input.horaAbertura() != null) ? input.horaAbertura() : restaurante.getHoraAbertura();
        LocalTime horaFechamento = (input.horaFechamento() != null) ? input.horaFechamento() : restaurante.getHoraFechamento();

        // Dono: se vier donoId, busca; se não vier, mantém o atual
        Usuario dono = restaurante.getDono();
        if (input.donoId() != null) {
            dono = usuarioGatewayDomain.findByIdUsuario(input.donoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", input.donoId()));
        }

        restaurante.atualizarDados(
            nome,
            endereco,
            typeCozinha,
            horaAbertura,
            horaFechamento,
            dono
        );

        Restaurante atualizado = restauranteGatewayDomain.saveRestaurante(restaurante);
        return toOutput(atualizado);
    }

    private String valorOuAtual(String valor, String atual) {
        return (valor != null && !valor.isBlank()) ? valor : atual;
    }

    private RestauranteOutput toOutput(Restaurante restaurante) {
        Long donoId = (restaurante.getDono() == null) ? null : restaurante.getDono().getId();

        return new RestauranteOutput(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getEndereco(),
            restaurante.getTypeCozinha(),
            restaurante.getHoraAbertura(),
            restaurante.getHoraFechamento(),
            donoId,
            restaurante.isAtivo()
        );
    }
}
