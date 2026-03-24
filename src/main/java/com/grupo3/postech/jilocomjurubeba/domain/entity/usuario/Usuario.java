package com.grupo3.postech.jilocomjurubeba.domain.entity.usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

/**
 * Entidade de dominio que representa um usuario do sistema.
 *
 * <p>POJO puro sem dependencias de framework. Possui dois construtores: um de criacao (sem id,
 * ativo=true) e um de reconstituicao (todos os campos, usado pelo PersistenceMapper).
 *
 * <p>O usuario e a entidade central de autenticacao e autorizacao do sistema. Cada usuario possui
 * um {@link TipoUsuario} que define seu papel: MASTER (administrador), DONO_RESTAURANTE
 * (proprietario de restaurantes) ou CLIENTE (consumidor). Usuarios do tipo DONO_RESTAURANTE podem
 * ter uma lista de {@link Restaurante}s associados.
 *
 * <p>Regras de negocio:
 *
 * <ul>
 *   <li>Nome e obrigatorio e normalizado para UPPERCASE
 *   <li>CPF e Email sao obrigatorios (Value Objects com validacao propria)
 *   <li>TipoUsuario e obrigatorio
 *   <li>Senha e obrigatoria
 *   <li>Telefone e opcional (normalizado com trim ou {@code null} se vazio)
 *   <li>Soft delete via {@link #desativar()}
 *   <li>Lista de restaurantes e imutavel externamente (retornada via {@link
 *       Collections#unmodifiableList})
 * </ul>
 *
 * @see TipoUsuario
 * @see Cpf
 * @see Email
 * @see Restaurante
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class Usuario {

    private Long id;
    private String nome;
    private Cpf cpf;
    private Email email;
    private String telefone;
    private TipoUsuario tipoUsuario;
    private String senha;
    private boolean ativo;
    private List<Restaurante> restaurantes;

    /**
     * Construtor de criacao — usado para criar um novo usuario.
     *
     * <p>O id e gerado pela camada de persistencia. O campo ativo inicia como {@code true}. A lista
     * de restaurantes inicia vazia.
     *
     * @param nome nome do usuario (obrigatorio)
     * @param cpf CPF como string (sera convertido para Value Object Cpf)
     * @param email email como string (sera convertido para Value Object Email)
     * @param telefone telefone do usuario (opcional)
     * @param tipoUsuario tipo do usuario (obrigatorio)
     * @param senha senha do usuario (obrigatoria)
     * @throws ValidacaoException se algum campo obrigatorio for nulo ou vazio
     */
    public Usuario(
            String nome,
            String cpf,
            String email,
            String telefone,
            TipoUsuario tipoUsuario,
            String senha) {
        validarNome(nome);
        validarSenha(senha);

        this.nome = normalizarNome(nome);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
        this.telefone = normalizarTelefone(telefone);
        this.tipoUsuario = validarTipoUsuario(tipoUsuario);
        this.senha = senha;
        this.ativo = true;
        this.restaurantes = new ArrayList<>();
    }

    /**
     * Construtor de reconstituicao — usado pelo PersistenceMapper para recriar a entidade a partir
     * do banco de dados.
     *
     * @param id identificador unico
     * @param nome nome do usuario
     * @param cpf Value Object CPF
     * @param email Value Object Email
     * @param telefone telefone do usuario
     * @param tipoUsuario tipo do usuario
     * @param senha senha do usuario
     * @param ativo status de ativacao
     * @param restaurantes lista de restaurantes associados
     * @throws ValidacaoException se algum campo obrigatorio for nulo ou vazio
     */
    public Usuario(
            Long id,
            String nome,
            Cpf cpf,
            Email email,
            String telefone,
            TipoUsuario tipoUsuario,
            String senha,
            boolean ativo,
            List<Restaurante> restaurantes) {
        validarNome(nome);
        validarCpf(cpf);
        validarEmail(email);
        validarSenha(senha);

        this.id = id;
        this.nome = normalizarNome(nome);
        this.cpf = cpf;
        this.email = email;
        this.telefone = normalizarTelefone(telefone);
        this.tipoUsuario = validarTipoUsuario(tipoUsuario);
        this.senha = senha;
        this.ativo = ativo;
        this.restaurantes =
                restaurantes != null ? new ArrayList<>(restaurantes) : new ArrayList<>();
    }

    /**
     * Atualiza os dados mutaveis do usuario.
     *
     * @param nome novo nome (obrigatorio)
     * @param email novo email como string
     * @param telefone novo telefone (opcional)
     * @throws ValidacaoException se nome ou email forem invalidos
     */
    public void atualizarDados(String nome, String email, String telefone) {
        validarNome(nome);

        this.nome = normalizarNome(nome);
        this.email = new Email(email);
        this.telefone = normalizarTelefone(telefone);
    }

    /**
     * Atualiza a senha do usuario.
     *
     * @param novaSenha nova senha (obrigatoria)
     * @throws ValidacaoException se a nova senha for nula ou vazia
     */
    public void atualizarSenha(String novaSenha) {
        validarSenha(novaSenha);
        this.senha = novaSenha;
    }

    /** Ativa o usuario (soft undelete). */
    public void ativar() {
        this.ativo = true;
    }

    /** Desativa o usuario (soft delete). */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Verifica se o usuario esta ativo.
     *
     * @return {@code true} se o usuario estiver ativo
     */
    public boolean estaAtivo() {
        return ativo;
    }

    /**
     * Verifica se o usuario e dono de restaurante.
     *
     * @return {@code true} se o tipo do usuario for DONO_RESTAURANTE
     */
    public boolean eDonoDeRestaurante() {
        return tipoUsuario != null && tipoUsuario.temNome("DONO_RESTAURANTE");
    }

    /**
     * Verifica se o id informado corresponde ao id deste usuario.
     *
     * @param outroId id para comparacao
     * @return {@code true} se os ids forem iguais
     */
    public boolean possuiMesmoIdQue(Long outroId) {
        return id != null && id.equals(outroId);
    }

    // ========== Getters (sem setters!) ==========

    /**
     * Retorna o identificador unico do usuario.
     *
     * <p>O id e {@code null} quando a entidade ainda nao foi persistida (construtor de criacao).
     * Apos a persistencia, o id e gerado automaticamente pela camada de infrastructure.
     *
     * @return identificador unico ou {@code null} se ainda nao persistido
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna o nome do usuario.
     *
     * <p>O nome e sempre armazenado em UPPERCASE e sem espacos nas extremidades, resultado da
     * normalizacao aplicada no construtor e em {@link #atualizarDados(String, String, String)}.
     *
     * @return nome do usuario em UPPERCASE
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o CPF do usuario como Value Object.
     *
     * <p>O {@link Cpf} garante que o numero armazenado possui exatamente 11 digitos e nao contem
     * todos os digitos iguais.
     *
     * @return Value Object CPF do usuario
     */
    public Cpf getCpf() {
        return cpf;
    }

    /**
     * Retorna o email do usuario como Value Object.
     *
     * <p>O {@link Email} garante que o endereco e valido (contem "@" com texto antes e depois) e
     * esta normalizado em lowercase.
     *
     * @return Value Object Email do usuario
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Retorna o telefone do usuario.
     *
     * <p>Campo opcional. Retorna {@code null} se o telefone nao foi informado ou se foi informado
     * como string vazia/em branco.
     *
     * @return telefone do usuario ou {@code null} se nao informado
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Retorna o tipo de usuario associado.
     *
     * <p>O tipo define o papel do usuario no sistema: MASTER, DONO_RESTAURANTE ou CLIENTE.
     *
     * @return tipo de usuario associado
     */
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Retorna a senha do usuario.
     *
     * <p>A senha e armazenada em texto criptografado apos passar pelo {@link
     * com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.CriptografiaSenhaGateway}.
     *
     * @return senha do usuario (potencialmente criptografada)
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Verifica se o usuario esta ativo no sistema.
     *
     * <p>Usuarios inativos sao resultado de soft delete via {@link #desativar()}.
     *
     * @return {@code true} se o usuario estiver ativo, {@code false} caso contrario
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Retorna uma lista imutavel dos restaurantes associados ao usuario.
     *
     * @return lista imutavel de restaurantes
     */
    public List<Restaurante> getRestaurantes() {
        return Collections.unmodifiableList(restaurantes);
    }

    // ========== Validacoes ==========

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do usuario e obrigatorio");
        }
    }

    private void validarCpf(Cpf cpf) {
        if (cpf == null) {
            throw new ValidacaoException("CPF do usuario e obrigatorio");
        }
    }

    private void validarEmail(Email email) {
        if (email == null) {
            throw new ValidacaoException("Email do usuario e obrigatorio");
        }
    }

    private TipoUsuario validarTipoUsuario(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            throw new ValidacaoException("Tipo do usuario e obrigatorio");
        }
        return tipoUsuario;
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new ValidacaoException("Senha do usuario e obrigatoria");
        }
    }

    // ========== Normalizacao ==========

    private String normalizarNome(String nome) {
        return nome.trim().toUpperCase();
    }

    private String normalizarTelefone(String telefone) {
        return telefone == null || telefone.trim().isEmpty() ? null : telefone.trim();
    }

    // ========== equals / hashCode / toString ==========

    /**
     * Compara dois usuarios pela identidade (campo {@code id}).
     *
     * <p>Duas instancias de {@code Usuario} sao consideradas iguais se possuirem o mesmo {@code
     * id}. Demais campos como nome, CPF e email nao participam da comparacao de igualdade.
     *
     * @param o objeto a ser comparado
     * @return {@code true} se o objeto for um {@code Usuario} com o mesmo id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario that)) return false;
        return Objects.equals(id, that.id);
    }

    /**
     * Retorna o hash code baseado no campo {@code id}.
     *
     * @return hash code calculado a partir do identificador unico
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Retorna uma representacao textual do usuario para fins de debug e log.
     *
     * <p>Inclui id, nome, CPF, email, tipo de usuario e status de ativacao. A senha nunca e
     * incluida na representacao textual por seguranca.
     *
     * @return string descritiva do usuario
     */
    @Override
    public String toString() {
        return "Usuario{id="
                + id
                + ", nome='"
                + nome
                + "', cpf='"
                + cpf
                + "', email='"
                + email
                + "', tipoUsuario="
                + tipoUsuario
                + ", ativo="
                + ativo
                + "}";
    }
}
