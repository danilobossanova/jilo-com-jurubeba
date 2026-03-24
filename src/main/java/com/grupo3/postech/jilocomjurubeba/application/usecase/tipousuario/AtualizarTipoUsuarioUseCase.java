package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.AtualizarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para atualizacao de um TipoUsuario existente.
 *
 * <p>Este caso de uso orquestra o fluxo de atualizacao de tipo de usuario:
 *
 * <ol>
 *   <li>Busca o tipo de usuario pelo ID informado no input
 *   <li>Normaliza o novo nome para UPPERCASE e verifica unicidade (excluindo o proprio registro)
 *   <li>Delega a atualizacao para o metodo {@code atualizarDados()} da entidade de dominio
 *   <li>Persiste a entidade atualizada via {@link TipoUsuarioGateway#salvar(TipoUsuario)}
 *   <li>Retorna o {@link TipoUsuarioOutput} com os dados atualizados
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
public class AtualizarTipoUsuarioUseCase
        implements UseCase<AtualizarTipoUsuarioInput, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    /**
     * Construtor com injecao do gateway de tipos de usuario.
     *
     * @param tipoUsuarioGateway gateway de persistencia de tipos de usuario
     */
    public AtualizarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    /**
     * Executa a atualizacao de um tipo de usuario existente.
     *
     * <p>Verifica existencia do registro e unicidade do novo nome (normalizado para UPPERCASE)
     * antes de atualizar e persistir a entidade de dominio.
     *
     * @param input dados de entrada contendo ID, novo nome e nova descricao do tipo de usuario
     * @return {@link TipoUsuarioOutput} com os dados do tipo de usuario apos a atualizacao
     * @throws EntidadeNaoEncontradaException se nenhum tipo de usuario for encontrado com o ID
     *     informado
     * @throws RegraDeNegocioException se ja existir outro tipo de usuario com o nome informado
     */
    @Override
    public TipoUsuarioOutput executar(AtualizarTipoUsuarioInput input) {
        TipoUsuario tipoUsuario =
                tipoUsuarioGateway
                        .buscarPorId(input.id())
                        .orElseThrow(
                                () ->
                                        new EntidadeNaoEncontradaException(
                                                "TipoUsuario", input.id()));

        String nomeNormalizado = input.nome().trim().toUpperCase();
        if (tipoUsuarioGateway.existePorNomeEIdDiferente(nomeNormalizado, input.id())) {
            throw new RegraDeNegocioException(
                    "Ja existe outro tipo de usuario com o nome '" + input.nome() + "'");
        }

        tipoUsuario.atualizarDados(input.nome(), input.descricao());
        TipoUsuario atualizado = tipoUsuarioGateway.salvar(tipoUsuario);

        return new TipoUsuarioOutput(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getDescricao(),
                atualizado.isAtivo());
    }
}
