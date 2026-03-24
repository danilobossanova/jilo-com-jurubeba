package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.CriarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.CriptografiaSenhaGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

/**
 * Caso de uso para criacao de um novo usuario no sistema.
 *
 * <p>Este caso de uso orquestra o fluxo completo de criacao de usuario:
 *
 * <ol>
 *   <li>Verifica se ja existe usuario com o CPF informado
 *   <li>Verifica se ja existe usuario com o email informado
 *   <li>Busca o {@link TipoUsuario} pelo ID informado no input
 *   <li>Criptografa a senha via {@link CriptografiaSenhaGateway}
 *   <li>Cria a entidade de dominio {@link Usuario} (validacoes no construtor)
 *   <li>Persiste via {@link UsuarioGateway#salvar(Usuario)}
 *   <li>Retorna o {@link UsuarioOutput} com os dados do usuario criado
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
public class CriarUsuarioUseCase implements UseCase<CriarUsuarioInput, UsuarioOutput> {

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final CriptografiaSenhaGateway criptografiaSenhaGateway;

    /**
     * Construtor com injecao de dependencias dos gateways necessarios.
     *
     * @param usuarioGateway gateway de persistencia de usuarios
     * @param tipoUsuarioGateway gateway de persistencia de tipos de usuario
     * @param criptografiaSenhaGateway gateway de criptografia de senhas
     */
    public CriarUsuarioUseCase(
            UsuarioGateway usuarioGateway,
            TipoUsuarioGateway tipoUsuarioGateway,
            CriptografiaSenhaGateway criptografiaSenhaGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.criptografiaSenhaGateway = criptografiaSenhaGateway;
    }

    /**
     * Executa a criacao de um novo usuario.
     *
     * <p>Valida unicidade de CPF e email, busca o tipo de usuario associado, criptografa a senha e
     * persiste a entidade de dominio.
     *
     * @param input dados de entrada contendo nome, CPF, email, telefone, tipo de usuario e senha
     * @return {@link UsuarioOutput} com os dados do usuario criado
     * @throws RegraDeNegocioException se ja existir usuario com o CPF ou email informado
     * @throws EntidadeNaoEncontradaException se o tipo de usuario informado nao existir
     */
    @Override
    public UsuarioOutput executar(CriarUsuarioInput input) {

        if (usuarioGateway.existePorCpf(input.cpf())) {
            throw new RegraDeNegocioException("Ja existe um usuario com o CPF informado");
        }

        if (usuarioGateway.existePorEmail(input.email())) {
            throw new RegraDeNegocioException("Ja existe um usuario com o email informado");
        }

        TipoUsuario tipoUsuario =
                tipoUsuarioGateway
                        .buscarPorId(input.tipoUsuarioId())
                        .orElseThrow(
                                () ->
                                        new EntidadeNaoEncontradaException(
                                                "TipoUsuario", input.tipoUsuarioId()));

        String senhaHash = criptografiaSenhaGateway.criptografar(input.senha());

        Usuario usuario =
                new Usuario(
                        input.nome(),
                        input.cpf(),
                        input.email(),
                        input.telefone(),
                        tipoUsuario,
                        senhaHash);

        Usuario salvo = usuarioGateway.salvar(usuario);

        return toOutput(salvo);
    }

    /**
     * Converte uma entidade de dominio {@link Usuario} para o DTO de saida.
     *
     * @param usuario entidade de dominio a ser convertida
     * @return {@link UsuarioOutput} com os dados da entidade
     */
    private static UsuarioOutput toOutput(Usuario usuario) {
        return new UsuarioOutput(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf().getNumero(),
                usuario.getEmail().getEndereco(),
                usuario.getTelefone(),
                usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getNome() : null,
                usuario.isAtivo());
    }
}
