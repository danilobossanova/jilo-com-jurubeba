package com.grupo3.postech.jilocomjurubeba.domain.entity.usuario;

import java.util.ArrayList;
import java.util.List;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private Cpf cpf;
    private Email email;
    private String telefone;
    private TipoUsuario tipoUsuario;
    private String senha; // <--- Novo campo para persistência
    private boolean ativo;

    private List<Restaurante> restaurantes = new ArrayList<>();

    // Construtor Completo (Geralmente usado para reconstrução vinda do banco/Gateway)
    public Usuario(
        Long id,
        String nome,
        Cpf cpf,
        Email email,
        String telefone,
        TipoUsuario tipoUsuario,
        boolean ativo,
        List<Restaurante> restaurantes,
        String senha) {
        validarNome(nome);
        validarTipo(tipoUsuario);
        validarSenha(senha);

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.restaurantes = restaurantes;
        this.ativo = ativo;
        this.senha = senha;
    }

    // Construtor de Criação (Usado no CriarUsuarioUseCase)
    public Usuario(
        String nome,
        Cpf cpf,
        Email email,
        String telefone,
        TipoUsuario tipoUsuario,
        List<Restaurante> restaurantes,
        String senha) {
        validarNome(nome);
        validarTipo(tipoUsuario);
        validarSenha(senha);

        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
        this.restaurantes = restaurantes;
        this.senha = senha;
    }

    public void atualizarDados(
        String nome, Cpf cpf, Email email, String telefone, TipoUsuario tipoUsuario) {
        validarNome(nome);
        validarTipo(tipoUsuario);

        this.nome = nome.trim().toUpperCase();
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isDono() {
        return "DONO_RESTAURANTE".equalsIgnoreCase(tipoUsuario.getNome());
    }

    public boolean isCliente() {
        return "CLIENTE".equalsIgnoreCase(tipoUsuario.getNome());
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome é obrigatório");
        }
    }

    private void validarTipo(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            throw new RegraDeNegocioException("Precisa definir o tipo do usuário");
        }
    }

    // Validação de senha no domínio
    private void validarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new RegraDeNegocioException("Senha é obrigatória");
        }
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
}
