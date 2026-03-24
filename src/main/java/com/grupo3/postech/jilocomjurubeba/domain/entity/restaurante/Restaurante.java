package com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante;

import java.time.LocalTime;
import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.TipoCozinha;

/**
 * Entidade de dominio que representa um restaurante.
 *
 * <p>POJO puro sem dependencias de framework. Possui dois construtores: um de criacao (sem id,
 * ativo=true) e um de reconstituicao (todos os campos, usado pelo PersistenceMapper).
 *
 * <p>Um restaurante e o ponto central do sistema de gestao. Cada restaurante pertence a um unico
 * {@link Usuario} do tipo DONO_RESTAURANTE e possui um {@link TipoCozinha} que classifica sua
 * culinaria. O dono do restaurante e definido na criacao e nao pode ser alterado pelo metodo {@link
 * #atualizarDados(String, String, TipoCozinha, LocalTime, LocalTime)}.
 *
 * <p>Regras de negocio:
 *
 * <ul>
 *   <li>Nome e obrigatorio e normalizado para UPPERCASE
 *   <li>Endereco e obrigatorio (normalizado com trim)
 *   <li>Tipo de cozinha e obrigatorio
 *   <li>Horarios de abertura e fechamento sao obrigatorios
 *   <li>Dono (Usuario) e obrigatorio e imutavel apos criacao
 *   <li>Soft delete via {@link #desativar()}
 * </ul>
 *
 * @see Usuario
 * @see TipoCozinha
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class Restaurante {

    private Long id;
    private String nome;
    private String endereco;
    private TipoCozinha tipoCozinha;
    private LocalTime horaAbertura;
    private LocalTime horaFechamento;
    private Usuario dono;
    private boolean ativo;

    /**
     * Construtor de criacao — usado para criar um novo restaurante.
     *
     * <p>O id e gerado pela camada de persistencia. O campo ativo inicia como {@code true}.
     *
     * @param nome nome do restaurante (obrigatorio)
     * @param endereco endereco do restaurante (obrigatorio)
     * @param tipoCozinha tipo de cozinha (obrigatorio)
     * @param horaAbertura horario de abertura (obrigatorio)
     * @param horaFechamento horario de fechamento (obrigatorio)
     * @param dono usuario dono do restaurante (obrigatorio)
     * @throws ValidacaoException se algum campo obrigatorio for nulo ou vazio
     */
    public Restaurante(
            String nome,
            String endereco,
            TipoCozinha tipoCozinha,
            LocalTime horaAbertura,
            LocalTime horaFechamento,
            Usuario dono) {
        validarCamposObrigatorios(nome, endereco, tipoCozinha, horaAbertura, horaFechamento, dono);

        this.nome = normalizarNome(nome);
        this.endereco = normalizarEndereco(endereco);
        this.tipoCozinha = tipoCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.dono = dono;
        this.ativo = true;
    }

    /**
     * Construtor de reconstituicao — usado pelo PersistenceMapper para recriar a entidade a partir
     * do banco de dados.
     *
     * @param id identificador unico
     * @param nome nome do restaurante
     * @param endereco endereco do restaurante
     * @param tipoCozinha tipo de cozinha
     * @param horaAbertura horario de abertura
     * @param horaFechamento horario de fechamento
     * @param dono usuario dono do restaurante
     * @param ativo status de ativacao
     * @throws ValidacaoException se algum campo obrigatorio for nulo ou vazio
     */
    public Restaurante(
            Long id,
            String nome,
            String endereco,
            TipoCozinha tipoCozinha,
            LocalTime horaAbertura,
            LocalTime horaFechamento,
            Usuario dono,
            boolean ativo) {
        validarCamposObrigatorios(nome, endereco, tipoCozinha, horaAbertura, horaFechamento, dono);

        this.id = id;
        this.nome = normalizarNome(nome);
        this.endereco = normalizarEndereco(endereco);
        this.tipoCozinha = tipoCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.dono = dono;
        this.ativo = ativo;
    }

    /**
     * Atualiza os dados do restaurante (sem alterar o dono).
     *
     * @param nome novo nome (obrigatorio)
     * @param endereco novo endereco (obrigatorio)
     * @param tipoCozinha novo tipo de cozinha (obrigatorio)
     * @param horaAbertura novo horario de abertura (obrigatorio)
     * @param horaFechamento novo horario de fechamento (obrigatorio)
     * @throws ValidacaoException se algum campo obrigatorio for nulo ou vazio
     */
    public void atualizarDados(
            String nome,
            String endereco,
            TipoCozinha tipoCozinha,
            LocalTime horaAbertura,
            LocalTime horaFechamento) {
        validarNome(nome);
        validarEndereco(endereco);
        validarTipoCozinha(tipoCozinha);
        validarHorarios(horaAbertura, horaFechamento);

        this.nome = normalizarNome(nome);
        this.endereco = normalizarEndereco(endereco);
        this.tipoCozinha = tipoCozinha;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
    }

    /** Ativa o restaurante (soft undelete). */
    public void ativar() {
        this.ativo = true;
    }

    /** Desativa o restaurante (soft delete). */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Verifica se o restaurante esta ativo.
     *
     * @return {@code true} se o restaurante estiver ativo
     */
    public boolean estaAtivo() {
        return ativo;
    }

    /**
     * Verifica se o restaurante pertence ao dono com o id informado.
     *
     * @param donoId id do dono para comparacao
     * @return {@code true} se o restaurante pertencer ao dono informado
     */
    public boolean pertenceAoDono(Long donoId) {
        return dono != null && dono.getId() != null && dono.getId().equals(donoId);
    }

    /**
     * Verifica se o id informado corresponde ao id deste restaurante.
     *
     * @param outroId id para comparacao
     * @return {@code true} se os ids forem iguais
     */
    public boolean possuiMesmoIdQue(Long outroId) {
        return id != null && id.equals(outroId);
    }

    // ========== Getters (sem setters!) ==========

    /**
     * Retorna o identificador unico do restaurante.
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
     * Retorna o nome do restaurante.
     *
     * <p>O nome e sempre armazenado em UPPERCASE e sem espacos nas extremidades, resultado da
     * normalizacao aplicada no construtor e em {@link #atualizarDados(String, String, TipoCozinha,
     * LocalTime, LocalTime)}.
     *
     * @return nome do restaurante em UPPERCASE
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o endereco do restaurante.
     *
     * <p>O endereco e armazenado sem espacos nas extremidades (trim aplicado).
     *
     * @return endereco do restaurante
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Retorna o tipo de cozinha do restaurante.
     *
     * <p>Classifica o restaurante por tipo de culinaria (ex: BRASILEIRA, ITALIANA, JAPONESA).
     *
     * @return tipo de cozinha do restaurante
     * @see TipoCozinha
     */
    public TipoCozinha getTipoCozinha() {
        return tipoCozinha;
    }

    /**
     * Retorna o horario de abertura do restaurante.
     *
     * @return horario de abertura como {@link LocalTime}
     */
    public LocalTime getHoraAbertura() {
        return horaAbertura;
    }

    /**
     * Retorna o horario de fechamento do restaurante.
     *
     * @return horario de fechamento como {@link LocalTime}
     */
    public LocalTime getHoraFechamento() {
        return horaFechamento;
    }

    /**
     * Retorna o usuario dono do restaurante.
     *
     * <p>O dono e definido na criacao do restaurante e nao pode ser alterado pelo metodo {@link
     * #atualizarDados(String, String, TipoCozinha, LocalTime, LocalTime)}.
     *
     * @return usuario dono do restaurante
     */
    public Usuario getDono() {
        return dono;
    }

    /**
     * Verifica se o restaurante esta ativo no sistema.
     *
     * <p>Restaurantes inativos sao resultado de soft delete via {@link #desativar()}.
     *
     * @return {@code true} se o restaurante estiver ativo, {@code false} caso contrario
     */
    public boolean isAtivo() {
        return ativo;
    }

    // ========== Validacoes ==========

    private void validarCamposObrigatorios(
            String nome,
            String endereco,
            TipoCozinha tipoCozinha,
            LocalTime horaAbertura,
            LocalTime horaFechamento,
            Usuario dono) {
        validarNome(nome);
        validarEndereco(endereco);
        validarTipoCozinha(tipoCozinha);
        validarHorarios(horaAbertura, horaFechamento);
        validarDono(dono);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do restaurante e obrigatorio");
        }
    }

    private void validarEndereco(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new ValidacaoException("Endereco do restaurante e obrigatorio");
        }
    }

    private void validarTipoCozinha(TipoCozinha tipoCozinha) {
        if (tipoCozinha == null) {
            throw new ValidacaoException("Tipo de cozinha e obrigatorio");
        }
    }

    private void validarHorarios(LocalTime horaAbertura, LocalTime horaFechamento) {
        if (horaAbertura == null) {
            throw new ValidacaoException("Hora de abertura e obrigatoria");
        }
        if (horaFechamento == null) {
            throw new ValidacaoException("Hora de fechamento e obrigatoria");
        }
    }

    private void validarDono(Usuario dono) {
        if (dono == null) {
            throw new ValidacaoException("Dono do restaurante e obrigatorio");
        }
    }

    // ========== Normalizacao ==========

    private String normalizarNome(String nome) {
        return nome.trim().toUpperCase();
    }

    private String normalizarEndereco(String endereco) {
        return endereco.trim();
    }

    // ========== equals / hashCode / toString ==========

    /**
     * Compara dois restaurantes pela identidade (campo {@code id}).
     *
     * <p>Duas instancias de {@code Restaurante} sao consideradas iguais se possuirem o mesmo {@code
     * id}. Demais campos como nome e endereco nao participam da comparacao de igualdade.
     *
     * @param o objeto a ser comparado
     * @return {@code true} se o objeto for um {@code Restaurante} com o mesmo id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurante that)) return false;
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
     * Retorna uma representacao textual do restaurante para fins de debug e log.
     *
     * <p>Inclui id, nome, endereco, tipo de cozinha, horarios e status de ativacao. O dono nao e
     * incluido para evitar referencia circular na saida.
     *
     * @return string descritiva do restaurante
     */
    @Override
    public String toString() {
        return "Restaurante{id="
                + id
                + ", nome='"
                + nome
                + "', endereco='"
                + endereco
                + "', tipoCozinha="
                + tipoCozinha
                + ", horaAbertura="
                + horaAbertura
                + ", horaFechamento="
                + horaFechamento
                + ", ativo="
                + ativo
                + "}";
    }
}
