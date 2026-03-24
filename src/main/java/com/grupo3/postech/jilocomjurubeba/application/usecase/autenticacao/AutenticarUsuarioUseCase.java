package com.grupo3.postech.jilocomjurubeba.application.usecase.autenticacao;

import com.grupo3.postech.jilocomjurubeba.application.dto.autenticacao.AutenticarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.autenticacao.AutenticarUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.AutenticacaoGateway;

/**
 * Caso de uso para autenticacao de usuario no sistema.
 *
 * <p>Este caso de uso orquestra o fluxo de autenticacao:
 *
 * <ol>
 *   <li>Recebe as credenciais (email e senha) do usuario via {@link AutenticarUsuarioInput}
 *   <li>Delega a validacao das credenciais e geracao de token para o {@link AutenticacaoGateway}
 *   <li>Retorna o {@link AutenticarUsuarioOutput} contendo o token de acesso gerado
 * </ol>
 *
 * <p>A camada Application nao conhece detalhes de implementacao de autenticacao (JWT, OAuth, etc).
 * Toda essa logica fica encapsulada na implementacao do {@link AutenticacaoGateway} na camada
 * Infrastructure.
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
public class AutenticarUsuarioUseCase
        implements UseCase<AutenticarUsuarioInput, AutenticarUsuarioOutput> {

    private final AutenticacaoGateway autenticacaoGateway;

    /**
     * Construtor com injecao do gateway de autenticacao.
     *
     * @param autenticacaoGateway gateway responsavel pela validacao de credenciais e geracao de
     *     token
     */
    public AutenticarUsuarioUseCase(AutenticacaoGateway autenticacaoGateway) {
        this.autenticacaoGateway = autenticacaoGateway;
    }

    /**
     * Executa a autenticacao do usuario.
     *
     * <p>Extrai email e senha do input, delega a autenticacao para o gateway e encapsula o token
     * gerado em um DTO de saida.
     *
     * @param input dados de entrada contendo email e senha do usuario
     * @return {@link AutenticarUsuarioOutput} contendo o token de acesso gerado
     */
    @Override
    public AutenticarUsuarioOutput executar(AutenticarUsuarioInput input) {
        String token = autenticacaoGateway.autenticar(input.email(), input.senha());
        return new AutenticarUsuarioOutput(token);
    }
}
