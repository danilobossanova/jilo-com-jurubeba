package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.CriarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para criacao de um novo TipoUsuario no sistema.
 *
 * <p>Este caso de uso orquestra o fluxo de criacao de tipo de usuario:
 *
 * <ol>
 *   <li>Verifica se ja existe um tipo de usuario com o nome informado (normalizado para UPPERCASE)
 *   <li>Cria a entidade de dominio {@link TipoUsuario} (validacoes no construtor)
 *   <li>Persiste via {@link TipoUsuarioGateway#salvar(TipoUsuario)}
 *   <li>Retorna o {@link TipoUsuarioOutput} com os dados do tipo criado
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
public class CriarTipoUsuarioUseCase implements UseCase<CriarTipoUsuarioInput, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    /**
     * Construtor com injecao do gateway de tipos de usuario.
     *
     * @param tipoUsuarioGateway gateway de persistencia de tipos de usuario
     */
    public CriarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    /**
     * Executa a criacao de um novo tipo de usuario.
     *
     * <p>Verifica unicidade do nome (normalizado para UPPERCASE e trimmed) antes de criar e
     * persistir a entidade de dominio.
     *
     * @param input dados de entrada contendo nome e descricao do tipo de usuario
     * @return {@link TipoUsuarioOutput} com os dados do tipo de usuario criado
     * @throws RegraDeNegocioException se ja existir um tipo de usuario com o nome informado
     */
    @Override
    public TipoUsuarioOutput executar(CriarTipoUsuarioInput input) {
        if (tipoUsuarioGateway.existePorNome(input.nome().trim().toUpperCase())) {
            throw new RegraDeNegocioException(
                    "Ja existe um tipo de usuario com o nome '" + input.nome() + "'");
        }

        TipoUsuario tipoUsuario = new TipoUsuario(input.nome(), input.descricao());
        TipoUsuario salvo = tipoUsuarioGateway.salvar(tipoUsuario);

        return toOutput(salvo);
    }

    /**
     * Converte uma entidade de dominio {@link TipoUsuario} para o DTO de saida.
     *
     * @param tipoUsuario entidade de dominio a ser convertida
     * @return {@link TipoUsuarioOutput} com os dados da entidade
     */
    private TipoUsuarioOutput toOutput(TipoUsuario tipoUsuario) {
        return new TipoUsuarioOutput(
                tipoUsuario.getId(),
                tipoUsuario.getNome(),
                tipoUsuario.getDescricao(),
                tipoUsuario.isAtivo());
    }
}
