package com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante;

import java.time.LocalTime;
import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    // Construtor para RECONSTRUÇÃO
    public Restaurante(
        Long id,
        String nome,
        String endereco,
        TypeCozinha typeCozinha,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Usuario dono,
        boolean ativo) {

        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.typeCozinha = typeCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.dono = dono;
        this.ativo = ativo;
    }

    // Construtor para CRIAÇÃO
    public Restaurante(
        String nome,
        String endereco,
        TypeCozinha typeCozinha,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Usuario dono) {

        validarNome(nome);
        validarEndereco(endereco);
        validarTipoCozinha(typeCozinha);
        validarHorario(horaAbertura, horaFechamento);
        validarDono(dono);

        this.nome = nome;
        this.endereco = endereco;
        this.typeCozinha = typeCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.dono = dono;
        this.ativo = true;
    }

    public void atualizarDados(
        String nome,
        String endereco,
        TypeCozinha typeCozinha,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Usuario dono) {

        validarNome(nome);
        validarEndereco(endereco);
        validarTipoCozinha(typeCozinha);
        validarHorario(horaAbertura, horaFechamento);
        validarDono(dono);

        this.nome = nome;
        this.endereco = endereco;
        this.typeCozinha = typeCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.dono = dono;
    }

    public void desativar() { this.ativo = false; }
    public void ativar() { this.ativo = true; }

    public void alterarEndereco(String novoEndereco) {
        validarEndereco(novoEndereco);
        this.endereco = novoEndereco;
    }

    public void alterarHorario(LocalTime novaAbertura, LocalTime novoFechamento) {
        validarHorario(novaAbertura, novoFechamento);
        this.horaAbertura = novaAbertura;
        this.horaFechamento = novoFechamento;
    }

    public boolean estaAberto(LocalTime horarioAtual) {
        if (horaAbertura.isBefore(horaFechamento)) {
            return !horarioAtual.isBefore(horaAbertura) && !horarioAtual.isAfter(horaFechamento);
        }
        // funcionamento pela madrugada (ex: 18:00 às 02:00)
        return !horarioAtual.isBefore(horaAbertura) || !horarioAtual.isAfter(horaFechamento);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) throw new RegraDeNegocioException("Nome é obrigatório");
    }

    private void validarEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) throw new RegraDeNegocioException("Endereço é obrigatório");
    }

    private void validarTipoCozinha(TypeCozinha typeCozinha) {
        if (typeCozinha == null) throw new RegraDeNegocioException("Tipo de cozinha é obrigatório");
    }

    private void validarHorario(LocalTime abertura, LocalTime fechamento) {
        if (abertura == null || fechamento == null) throw new RegraDeNegocioException("Horários são obrigatórios");
        // ✅ permite madrugada (fechamento antes da abertura), mas não permite iguais
        if (abertura.equals(fechamento)) throw new RegraDeNegocioException("Abertura e fechamento não podem ser iguais");
    }

    private void validarDono(Usuario dono) {
        if (Objects.isNull(dono)) throw new RegraDeNegocioException("Restaurante deve possuir um dono");
    }
}
