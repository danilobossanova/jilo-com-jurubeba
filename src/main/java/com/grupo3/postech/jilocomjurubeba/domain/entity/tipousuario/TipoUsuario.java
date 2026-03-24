package com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario;

import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

/**
 * Entidade de dominio que representa um Tipo de Usuario no sistema.
 *
 * <p>Cada usuario do sistema possui um TipoUsuario que define seu papel: MASTER (administrador do
 * sistema), DONO_RESTAURANTE (proprietario de restaurante) ou CLIENTE (consumidor).
 *
 * <p>Esta entidade e um POJO puro, sem dependencias de framework. Protege seus invariantes atraves
 * de validacoes no construtor e nos metodos de mutacao.
 *
 * <p>Suporta soft delete atraves do campo {@code ativo}.
 *
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class TipoUsuario {

    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;

    /**
     * Construtor para criacao de novo TipoUsuario (sem id, ativo por padrao).
     *
     * @param nome nome unico do tipo (ex: "MASTER", "DONO_RESTAURANTE", "CLIENTE")
     * @param descricao descricao legivel do tipo
     * @throws ValidacaoException se nome ou descricao forem nulos ou vazios
     */
    public TipoUsuario(String nome, String descricao) {
        validarCamposObrigatorios(nome, descricao);
        this.nome = nome.trim().toUpperCase();
        this.descricao = descricao.trim();
        this.ativo = true;
    }

    /**
     * Construtor completo para reconstituicao a partir da persistencia.
     *
     * @param id identificador unico
     * @param nome nome unico do tipo
     * @param descricao descricao legivel do tipo
     * @param ativo indica se o tipo esta ativo
     * @throws ValidacaoException se nome ou descricao forem nulos ou vazios
     */
    public TipoUsuario(Long id, String nome, String descricao, boolean ativo) {
        validarCamposObrigatorios(nome, descricao);
        this.id = id;
        this.nome = nome.trim().toUpperCase();
        this.descricao = descricao.trim();
        this.ativo = ativo;
    }

    /**
     * Atualiza os dados do tipo de usuario com validacao.
     *
     * @param nome novo nome
     * @param descricao nova descricao
     * @throws ValidacaoException se nome ou descricao forem nulos ou vazios
     */
    public void atualizarDados(String nome, String descricao) {
        validarCamposObrigatorios(nome, descricao);
        this.nome = nome.trim().toUpperCase();
        this.descricao = descricao.trim();
    }

    /** Desativa o tipo de usuario (soft delete). */
    public void desativar() {
        this.ativo = false;
    }

    /** Reativa o tipo de usuario. */
    public void ativar() {
        this.ativo = true;
    }

    /**
     * Retorna o identificador unico do tipo de usuario.
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
     * Retorna o nome do tipo de usuario.
     *
     * <p>O nome e sempre armazenado em UPPERCASE e sem espacos nas extremidades, resultado da
     * normalizacao aplicada no construtor e em {@link #atualizarDados(String, String)}.
     *
     * @return nome do tipo de usuario em UPPERCASE (ex: {@code "MASTER"}, {@code "CLIENTE"})
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descricao legivel do tipo de usuario.
     *
     * @return descricao do tipo de usuario (ex: {@code "Administrador do sistema"})
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Verifica se o tipo de usuario esta ativo.
     *
     * <p>Tipos de usuario inativos sao resultado de soft delete via {@link #desativar()}.
     *
     * @return {@code true} se o tipo de usuario estiver ativo, {@code false} caso contrario
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Verifica se o nome deste tipo de usuario corresponde ao nome informado (case-insensitive).
     *
     * @param outroNome nome para comparacao
     * @return {@code true} se os nomes forem iguais (ignorando maiusculas/minusculas)
     */
    public boolean temNome(String outroNome) {
        return nome != null && nome.equalsIgnoreCase(outroNome);
    }

    private void validarCamposObrigatorios(String nome, String descricao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do tipo de usuario e obrigatorio");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new ValidacaoException("Descricao do tipo de usuario e obrigatoria");
        }
    }

    /**
     * Compara dois tipos de usuario pela identidade (campo {@code id}).
     *
     * <p>Duas instancias de {@code TipoUsuario} sao consideradas iguais se possuirem o mesmo {@code
     * id}. Campos como {@code nome} e {@code descricao} nao participam da comparacao de igualdade.
     *
     * @param o objeto a ser comparado
     * @return {@code true} se o objeto for um {@code TipoUsuario} com o mesmo id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoUsuario that = (TipoUsuario) o;
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
     * Retorna uma representacao textual do tipo de usuario para fins de debug e log.
     *
     * @return string no formato {@code "TipoUsuario{id=1, nome='MASTER', ativo=true}"}
     */
    @Override
    public String toString() {
        return "TipoUsuario{" + "id=" + id + ", nome='" + nome + '\'' + ", ativo=" + ativo + '}';
    }
}
