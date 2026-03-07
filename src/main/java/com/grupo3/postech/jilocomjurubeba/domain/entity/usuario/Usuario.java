package com.grupo3.postech.jilocomjurubeba.domain.entity.usuario;

import java.util.ArrayList;
import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioResumoOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private Cpf cpf;
    private Email email;
    private String telefone;
    private TipoUsuario tipoUsuario;
    private String senha;
    private boolean ativo;
    private List<Restaurante> restaurantes = new ArrayList<>();

    public Usuario(
            Long id,
            String nome,
            Cpf cpf,
            Email email,
            String telefone,
            TipoUsuario tipoUsuario,
            boolean ativo,
            List<Restaurante> restaurantes,
            String senha
    ) {
        validarNome(nome);
        validarTipo(tipoUsuario);
        validarSenha(senha);
        validarCpf(cpf);
        validarEmail(email);

        this.id = id;
        this.nome = normalizarNome(nome);
        this.cpf = cpf;
        this.email = email;
        this.telefone = normalizarTelefone(telefone);
        this.tipoUsuario = tipoUsuario;
        this.ativo = ativo;
        this.senha = senha;
        this.restaurantes = restaurantes != null ? new ArrayList<>(restaurantes) : new ArrayList<>();
    }

    public Usuario(
            String nome,
            Cpf cpf,
            Email email,
            String telefone,
            TipoUsuario tipoUsuario,
            List<Restaurante> restaurantes,
            String senha
    ) {
        validarNome(nome);
        validarTipo(tipoUsuario);
        validarSenha(senha);
        validarCpf(cpf);
        validarEmail(email);

        this.nome = normalizarNome(nome);
        this.cpf = cpf;
        this.email = email;
        this.telefone = normalizarTelefone(telefone);
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
        this.senha = senha;
        this.restaurantes = restaurantes != null ? new ArrayList<>(restaurantes) : new ArrayList<>();
    }

    public void atualizarCadastro(
            String nome,
            Cpf cpf,
            Email email,
            String telefone,
            TipoUsuario tipoUsuario
    ) {
        validarNome(nome);
        validarTipo(tipoUsuario);
        validarCpf(cpf);
        validarEmail(email);

        this.nome = normalizarNome(nome);
        this.cpf = cpf;
        this.email = email;
        this.telefone = normalizarTelefone(telefone);
        this.tipoUsuario = tipoUsuario;
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

    public boolean eDonoDeRestaurante() {
        return tipoUsuario != null && tipoUsuario.temNome("DONO_RESTAURANTE");
    }

    public boolean eCliente() {
        return tipoUsuario != null && tipoUsuario.temNome("CLIENTE");
    }

    public boolean possuiMesmoIdQue(Long outroId) {
        return id != null && id.equals(outroId);
    }

    public boolean representaMesmoUsuarioQue(Usuario outro) {
        return outro != null && this.id != null && this.id.equals(outro.id);
    }

    public boolean usaCpf(String cpfComparacao) {
        return cpfComparacao != null && cpf != null && cpfComparacao.equals(cpf.getNumero());
    }

    public boolean usaEmail(String emailComparacao) {
        return emailComparacao != null
                && email != null
                && emailComparacao.equalsIgnoreCase(email.getEmail());
    }

    public UsuarioSnapshot snapshot() {
        TipoUsuario.TipoUsuarioSnapshot tipoSnapshot = tipoUsuario != null ? tipoUsuario.snapshot() : null;

        return new UsuarioSnapshot(
                id,
                nome,
                cpf != null ? cpf.getNumero() : null,
                email != null ? email.getEmail() : null,
                telefone,
                tipoSnapshot != null ? tipoSnapshot.id() : null,
                tipoSnapshot != null ? tipoSnapshot.nome() : null,
                ativo,
                senha
        );
    }

    public UsuarioOutput paraOutput() {
        UsuarioSnapshot dados = snapshot();
        return new UsuarioOutput(
                dados.id(),
                dados.nome(),
                dados.cpf(),
                dados.email(),
                dados.telefone(),
                dados.tipoUsuarioId(),
                dados.tipoUsuarioNome(),
                dados.ativo()
        );
    }

    public UsuarioResumoOutput paraResumo() {
        UsuarioSnapshot dados = snapshot();
        return new UsuarioResumoOutput(
                dados.id(),
                dados.nome(),
                dados.email()
        );
    }

    public record UsuarioSnapshot(
            Long id,
            String nome,
            String cpf,
            String email,
            String telefone,
            Long tipoUsuarioId,
            String tipoUsuarioNome,
            boolean ativo,
            String senhaHash
    ) {}

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

    private void validarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new RegraDeNegocioException("Senha é obrigatória");
        }
    }

    private void validarCpf(Cpf cpf) {
        if (cpf == null) {
            throw new RegraDeNegocioException("CPF é obrigatório");
        }
    }

    private void validarEmail(Email email) {
        if (email == null) {
            throw new RegraDeNegocioException("Email é obrigatório");
        }
    }

    private String normalizarNome(String nome) {
        return nome.trim().toUpperCase();
    }

    private String normalizarTelefone(String telefone) {
        return telefone == null || telefone.isBlank() ? null : telefone.trim();
    }
}