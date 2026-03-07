package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import java.time.LocalTime;

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

        Restaurante.RestauranteSnapshot dadosAtuais = restaurante.snapshot();

        String nome = valorOuAtual(input.nome(), dadosAtuais.nome());
        String endereco = valorOuAtual(input.endereco(), dadosAtuais.endereco());
        TypeCozinha typeCozinha = input.typeCozinha() != null ? input.typeCozinha() : dadosAtuais.typeCozinha();
        LocalTime horaAbertura = input.horaAbertura() != null ? input.horaAbertura() : dadosAtuais.horaAbertura();
        LocalTime horaFechamento = input.horaFechamento() != null ? input.horaFechamento() : dadosAtuais.horaFechamento();

        Usuario dono;
        if (input.donoId() != null) {
            dono = usuarioGatewayDomain.findByIdUsuario(input.donoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", input.donoId()));
        } else {
            dono = usuarioGatewayDomain.findByIdUsuario(dadosAtuais.donoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", dadosAtuais.donoId()));
        }

        restaurante.atualizarCadastro(
                nome,
                endereco,
                typeCozinha,
                horaAbertura,
                horaFechamento,
                dono
        );

        Restaurante atualizado = restauranteGatewayDomain.saveRestaurante(restaurante);
        return atualizado.paraOutput();
    }

    private String valorOuAtual(String valor, String atual) {
        return (valor != null && !valor.isBlank()) ? valor : atual;
    }
}