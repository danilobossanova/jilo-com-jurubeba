package com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario;

import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

public class TipoUsuario {

    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;

    public TipoUsuario(String nome, String descricao) {
        validarCamposObrigatorios(nome, descricao);
        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.ativo = true;
    }

    public TipoUsuario(Long id, String nome, String descricao, boolean ativo) {
        validarCamposObrigatorios(nome, descricao);
        this.id = id;
        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.ativo = ativo;
    }

    public void atualizarCadastro(String nome, String descricao) {
        validarCamposObrigatorios(nome, descricao);
        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public boolean estaAtivo() {
        return ativo;
    }

    public boolean possuiMesmoIdQue(Long outroId) {
        return id != null && id.equals(outroId);
    }

    public boolean temNome(String outroNome) {
        return outroNome != null && nome.equalsIgnoreCase(outroNome.trim());
    }

    public TipoUsuarioSnapshot snapshot() {
        return new TipoUsuarioSnapshot(id, nome, descricao, ativo);
    }

    public TipoUsuarioOutput paraOutput() {
        TipoUsuarioSnapshot dados = snapshot();
        return new TipoUsuarioOutput(
                dados.id(),
                dados.nome(),
                dados.descricao(),
                dados.ativo()
        );
    }

    public record TipoUsuarioSnapshot(
            Long id,
            String nome,
            String descricao,
            boolean ativo
    ) {}

    private void validarCamposObrigatorios(String nome, String descricao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do tipo de usuario e obrigatorio");
        }

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new ValidacaoException("Descricao do tipo de usuario e obrigatoria");
        }
    }

    private String normalizarNome(String nome) {
        return nome.trim().toUpperCase();
    }

    private String normalizarDescricao(String descricao) {
        return descricao.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoUsuario that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}