package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import java.time.LocalTime;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.TipoCozinha;

/**
 * Caso de uso para atualizacao de dados de um restaurante existente.
 *
 * <p>Este caso de uso orquestra o fluxo de atualizacao de restaurante:
 *
 * <ol>
 *   <li>Busca o restaurante pelo ID informado no input
 *   <li>Verifica se o usuario solicitante e o dono do restaurante
 *   <li>Aplica atualizacao parcial: campos nulos no input preservam os valores atuais
 *   <li>Converte o tipo de cozinha de String para enum {@link TipoCozinha} se informado
 *   <li>Delega a atualizacao para o metodo {@code atualizarDados()} da entidade de dominio
 *   <li>Persiste a entidade atualizada via {@link RestauranteGateway#salvar(Restaurante)}
 *   <li>Retorna o {@link RestauranteOutput} com os dados atualizados
 * </ol>
 *
 * <p>Na Clean Architecture, este caso de uso pertence a camada Application e depende apenas de
 * interfaces do Domain (gateways). Nao possui dependencia de framework.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class AtualizarRestauranteUseCase
        implements UseCase<AtualizarRestauranteInput, RestauranteOutput> {

    private final RestauranteGateway restauranteGateway;

    /**
     * Construtor com injecao do gateway de restaurantes.
     *
     * @param restauranteGateway gateway de persistencia de restaurantes
     */
    public AtualizarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    /**
     * Executa a atualizacao parcial de um restaurante existente.
     *
     * <p>Valida a propriedade do restaurante (apenas o dono pode atualizar) e aplica atualizacao
     * parcial: campos {@code null} no input preservam os valores atuais.
     *
     * @param input dados de entrada contendo o ID do restaurante, ID do dono e campos a atualizar
     * @return {@link RestauranteOutput} com os dados do restaurante apos a atualizacao
     * @throws EntidadeNaoEncontradaException se nenhum restaurante for encontrado com o ID
     *     informado
     * @throws RegraDeNegocioException se o usuario nao for o dono do restaurante ou tipo de cozinha
     *     invalido
     */
    @Override
    public RestauranteOutput executar(AtualizarRestauranteInput input) {

        Restaurante restaurante =
                restauranteGateway
                        .buscarPorId(input.id())
                        .orElseThrow(
                                () ->
                                        new EntidadeNaoEncontradaException(
                                                "Restaurante", input.id()));

        if (!restaurante.pertenceAoDono(input.donoId())) {
            throw new RegraDeNegocioException("Apenas o dono pode atualizar o restaurante");
        }

        String nome = input.nome() != null ? input.nome() : restaurante.getNome();
        String endereco = input.endereco() != null ? input.endereco() : restaurante.getEndereco();
        TipoCozinha tipoCozinha =
                input.tipoCozinha() != null
                        ? converterTipoCozinha(input.tipoCozinha())
                        : restaurante.getTipoCozinha();
        LocalTime horaAbertura =
                input.horaAbertura() != null ? input.horaAbertura() : restaurante.getHoraAbertura();
        LocalTime horaFechamento =
                input.horaFechamento() != null
                        ? input.horaFechamento()
                        : restaurante.getHoraFechamento();

        restaurante.atualizarDados(nome, endereco, tipoCozinha, horaAbertura, horaFechamento);

        Restaurante atualizado = restauranteGateway.salvar(restaurante);

        return toOutput(atualizado);
    }

    /**
     * Converte uma String para o enum {@link TipoCozinha}.
     *
     * <p>Normaliza a entrada para UPPERCASE e trimmed antes da conversao.
     *
     * @param tipoCozinha nome do tipo de cozinha como String
     * @return valor correspondente do enum {@link TipoCozinha}
     * @throws RegraDeNegocioException se o valor for invalido
     */
    private static TipoCozinha converterTipoCozinha(String tipoCozinha) {
        try {
            return TipoCozinha.valueOf(tipoCozinha.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Tipo de cozinha invalido: '" + tipoCozinha + "'");
        }
    }

    /**
     * Converte uma entidade de dominio {@link Restaurante} para o DTO de saida.
     *
     * @param restaurante entidade de dominio a ser convertida
     * @return {@link RestauranteOutput} com os dados da entidade
     */
    private static RestauranteOutput toOutput(Restaurante restaurante) {
        return new RestauranteOutput(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getEndereco(),
                restaurante.getTipoCozinha() != null ? restaurante.getTipoCozinha().name() : null,
                restaurante.getHoraAbertura(),
                restaurante.getHoraFechamento(),
                restaurante.getDono() != null ? restaurante.getDono().getId() : null,
                restaurante.isAtivo());
    }
}
