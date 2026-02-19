package com.grupo3.postech.jilocomjurubeba.domain.entity.usuario;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private Cpf cpf;
    private Email email;
    private String telefone;
    private TypeUsuario typeUsuario;
    private boolean ativo;

    private List<Restaurante> restaurantes = new ArrayList<>();

    public Usuario(Long id, String nome, Cpf cpf, Email email, String telefone, TypeUsuario typeUsuario, boolean ativo, List<Restaurante> restaurantes) {
        validarNome(nome);
        validarTipo(typeUsuario);

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.typeUsuario = typeUsuario;
        this.restaurantes = restaurantes;
        this.ativo = ativo;
    }

    public Usuario(String nome, Cpf cpf, Email email, String telefone, TypeUsuario typeUsuario, List<Restaurante> restaurantes) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.typeUsuario = typeUsuario;
        this.ativo = true;
        this.restaurantes = restaurantes;
    }

    public void atualizarDados(String nome, Cpf cpf, Email email, String telefone, TypeUsuario typeUsuario) {
        this.nome = nome.trim().toUpperCase();
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.typeUsuario = typeUsuario;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome é obrigatório");
        }
    }

    private void validarTipo(TypeUsuario typeUsuario) {
        if (typeUsuario == null) {
            throw new RegraDeNegocioException("Precisa definir o tipo do usuário");
        }
    }

    public boolean isDono() {
        return typeUsuario.isDono();
    }

    public boolean isCliente() {
        return typeUsuario.isCliente();
    }

    public void adicionarRestaurante(Restaurante restaurante) {
        if (!isDono()) {
            throw new RegraDeNegocioException("Apenas donos podem possuir restaurantes");
        }

        restaurantes.add(restaurante);
    }

}
