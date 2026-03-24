package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.TipoCozinha;

/**
 * Caso de uso para criacao de um novo restaurante no sistema.
 *
 * <p>Este caso de uso orquestra o fluxo de criacao de restaurante:
 *
 * <ol>
 *   <li>Busca o usuario dono pelo ID informado via {@link UsuarioGateway}
 *   <li>Converte o tipo de cozinha de String para enum {@link TipoCozinha}
 *   <li>Cria a entidade de dominio {@link Restaurante} (validacoes no construtor)
 *   <li>Persiste via {@link RestauranteGateway#salvar(Restaurante)}
 *   <li>Retorna o {@link RestauranteOutput} com os dados do restaurante criado
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
public class CriarRestauranteUseCase implements UseCase<CriarRestauranteInput, RestauranteOutput> {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;

    /**
     * Construtor com injecao dos gateways necessarios.
     *
     * @param restauranteGateway gateway de persistencia de restaurantes
     * @param usuarioGateway gateway de persistencia de usuarios (para buscar o dono)
     */
    public CriarRestauranteUseCase(
            RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * Executa a criacao de um novo restaurante.
     *
     * <p>Busca o dono pelo ID, converte o tipo de cozinha de String para enum, cria a entidade de
     * dominio e persiste no banco de dados.
     *
     * @param input dados de entrada contendo nome, endereco, tipo de cozinha, horarios e ID do dono
     * @return {@link RestauranteOutput} com os dados do restaurante criado
     * @throws EntidadeNaoEncontradaException se o usuario dono nao for encontrado
     * @throws RegraDeNegocioException se o tipo de cozinha for invalido ou nulo
     */
    @Override
    public RestauranteOutput executar(CriarRestauranteInput input) {

        Usuario dono =
                usuarioGateway
                        .buscarPorId(input.donoId())
                        .orElseThrow(
                                () ->
                                        new EntidadeNaoEncontradaException(
                                                "Usuario", input.donoId()));

        TipoCozinha tipoCozinha = converterTipoCozinha(input.tipoCozinha());

        Restaurante restaurante =
                new Restaurante(
                        input.nome(),
                        input.endereco(),
                        tipoCozinha,
                        input.horaAbertura(),
                        input.horaFechamento(),
                        dono);

        Restaurante salvo = restauranteGateway.salvar(restaurante);

        return toOutput(salvo);
    }

    /**
     * Converte uma String para o enum {@link TipoCozinha}.
     *
     * <p>Normaliza a entrada para UPPERCASE e trimmed antes da conversao.
     *
     * @param tipoCozinha nome do tipo de cozinha como String
     * @return valor correspondente do enum {@link TipoCozinha}
     * @throws RegraDeNegocioException se o valor for nulo, vazio ou invalido
     */
    private static TipoCozinha converterTipoCozinha(String tipoCozinha) {
        if (tipoCozinha == null || tipoCozinha.isBlank()) {
            throw new RegraDeNegocioException("Tipo de cozinha e obrigatorio");
        }
        try {
            return TipoCozinha.valueOf(tipoCozinha.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Tipo de cozinha invalido: '" + tipoCozinha + "'");
        }
    }

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
