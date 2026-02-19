package com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import lombok.*;

import java.time.LocalTime;
import java.util.Objects;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Restaurante {

    private Long id;
    private String nome;
    private String endereco;
    private TypeCozinha typeCozinha;
    private LocalTime horaAbertura;
    private LocalTime horaFechamento;
    private Usuario dono;
    private boolean ativo;

    public Restaurante(Long id, String nome, String endereco, TypeCozinha typeCozinha, LocalTime horaAbertura, LocalTime horaFechamento, Usuario dono, boolean ativo) {

        validarNome(nome);
        validarEndereco(endereco);
        validarTipoCozinha(typeCozinha);
        validarHorario(horaAbertura, horaFechamento);
        validarDono(dono);

        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.typeCozinha = typeCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.ativo = ativo;
        this.dono = dono;
    }

    public Restaurante(String nome, String endereco, TypeCozinha typeCozinha, LocalTime horaAbertura, LocalTime horaFechamento, Usuario dono) {
        this.nome = nome;
        this.endereco = endereco;
        this.typeCozinha = typeCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.dono = dono;
        this.ativo = true;
    }

    public void atualizarDados(String nome,  String endereco, TypeCozinha typeCozinha, LocalTime horaAbertura, LocalTime horaFechamento, Usuario dono) {
        this.nome = nome.trim().toUpperCase();
        this.endereco = endereco;
        this.typeCozinha = typeCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.dono = dono;

    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome do restaurante é obrigatório");
        }
    }

    private void validarEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) {
            throw new RegraDeNegocioException("Endereço é obrigatório");
        }
    }

    private void validarTipoCozinha(TypeCozinha typeCozinha) {
        if (typeCozinha == null) {
            throw new RegraDeNegocioException("Tipo de cozinha é obrigatório");
        }
    }

    private void validarHorario(LocalTime abertura, LocalTime fechamento) {
        if (abertura == null || fechamento == null) {
            throw new RegraDeNegocioException("Horário de funcionamento é obrigatório");
        }

        if (fechamento.isBefore(abertura)) {
            throw new RegraDeNegocioException("Hora de fechamento deve ser após a abertura");
        }
    }

    private void validarDono(Usuario dono) {
        if (Objects.isNull(dono)) {
            throw new RegraDeNegocioException("Restaurante deve possuir um dono");
        }
    }

    public boolean estaAberto(LocalTime horarioAtual) {
        return !horarioAtual.isBefore(horaAbertura)
            && !horarioAtual.isAfter(horaFechamento);
    }

    public void alterarHorario(LocalTime novaAbertura, LocalTime novoFechamento) {
        validarHorario(novaAbertura, novoFechamento);
        this.horaAbertura = novaAbertura;
        this.horaFechamento = novoFechamento;
    }

    public void alterarEndereco(String novoEndereco) {
        validarEndereco(novoEndereco);
        this.endereco = novoEndereco;
    }

}
