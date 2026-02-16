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
 * @author Danilo Fernando
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway
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

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    private void validarCamposObrigatorios(String nome, String descricao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do tipo de usuario e obrigatorio");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new ValidacaoException("Descricao do tipo de usuario e obrigatoria");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoUsuario that = (TipoUsuario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TipoUsuario{" + "id=" + id + ", nome='" + nome + '\'' + ", ativo=" + ativo + '}';
    }
}
