package com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio;

import java.math.BigDecimal;
import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

/**
 * Entidade de dominio que representa um item do cardapio de um restaurante.
 *
 * <p>POJO puro sem dependencias de framework. Possui dois construtores: um de criacao (sem id,
 * ativo=true) e um de reconstituicao (todos os campos, usado pelo PersistenceMapper).
 *
 * <p>Cada item do cardapio pertence a um unico {@link Restaurante} e representa um prato ou produto
 * oferecido pelo restaurante. O restaurante e definido na criacao e nao pode ser alterado pelo
 * metodo {@link #atualizarDados(String, String, BigDecimal, boolean, String)}. Itens podem ser
 * marcados como {@code apenasNoLocal} para indicar disponibilidade restrita ao consumo presencial.
 *
 * <p>Regras de negocio:
 *
 * <ul>
 *   <li>Nome e obrigatorio e normalizado para UPPERCASE
 *   <li>Descricao e obrigatoria (normalizada com trim)
 *   <li>Preco e obrigatorio e deve ser maior que zero ({@link BigDecimal})
 *   <li>Restaurante e obrigatorio e imutavel apos criacao
 *   <li>Caminho da foto e opcional (normalizado com trim ou {@code null} se vazio)
 *   <li>Soft delete via {@link #desativar()}
 * </ul>
 *
 * @see Restaurante
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class Cardapio {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean apenasNoLocal;
    private String caminhoFoto;
    private Restaurante restaurante;
    private boolean ativo;

    /**
     * Construtor de criacao — usado para criar um novo item do cardapio.
     *
     * <p>O id e gerado pela camada de persistencia. O campo ativo inicia como {@code true}.
     *
     * @param nome nome do item (obrigatorio)
     * @param descricao descricao do item (obrigatoria)
     * @param preco preco do item (obrigatorio, maior que zero)
     * @param apenasNoLocal indica se o item esta disponivel apenas no local
     * @param caminhoFoto caminho da foto do item (opcional)
     * @param restaurante restaurante ao qual o item pertence (obrigatorio)
     * @throws ValidacaoException se algum campo obrigatorio for nulo, vazio ou invalido
     */
    public Cardapio(
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Restaurante restaurante) {
        validarCamposObrigatorios(nome, descricao, preco, restaurante);

        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.preco = preco;
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = normalizarCaminhoFoto(caminhoFoto);
        this.restaurante = restaurante;
        this.ativo = true;
    }

    /**
     * Construtor de reconstituicao — usado pelo PersistenceMapper para recriar a entidade a partir
     * do banco de dados.
     *
     * @param id identificador unico
     * @param nome nome do item
     * @param descricao descricao do item
     * @param preco preco do item
     * @param apenasNoLocal indica se o item esta disponivel apenas no local
     * @param caminhoFoto caminho da foto do item
     * @param restaurante restaurante ao qual o item pertence
     * @param ativo status de ativacao
     * @throws ValidacaoException se algum campo obrigatorio for nulo, vazio ou invalido
     */
    public Cardapio(
            Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Restaurante restaurante,
            boolean ativo) {
        validarCamposObrigatorios(nome, descricao, preco, restaurante);

        this.id = id;
        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.preco = preco;
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = normalizarCaminhoFoto(caminhoFoto);
        this.restaurante = restaurante;
        this.ativo = ativo;
    }

    /**
     * Atualiza os dados do item do cardapio (sem alterar o restaurante).
     *
     * @param nome novo nome (obrigatorio)
     * @param descricao nova descricao (obrigatoria)
     * @param preco novo preco (obrigatorio, maior que zero)
     * @param apenasNoLocal nova disponibilidade local
     * @param caminhoFoto novo caminho da foto (opcional)
     * @throws ValidacaoException se algum campo obrigatorio for nulo, vazio ou invalido
     */
    public void atualizarDados(
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto) {
        validarNome(nome);
        validarDescricao(descricao);
        validarPreco(preco);

        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.preco = preco;
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = normalizarCaminhoFoto(caminhoFoto);
    }

    /** Ativa o item do cardapio (soft undelete). */
    public void ativar() {
        this.ativo = true;
    }

    /** Desativa o item do cardapio (soft delete). */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Verifica se o item do cardapio esta ativo.
     *
     * @return {@code true} se o item estiver ativo
     */
    public boolean estaAtivo() {
        return ativo;
    }

    /**
     * Verifica se o id informado corresponde ao id deste item.
     *
     * @param outroId id para comparacao
     * @return {@code true} se os ids forem iguais
     */
    public boolean possuiMesmoIdQue(Long outroId) {
        return id != null && id.equals(outroId);
    }

    // ========== Getters (sem setters!) ==========

    /**
     * Retorna o identificador unico do item do cardapio.
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
     * Retorna o nome do item do cardapio.
     *
     * <p>O nome e sempre armazenado em UPPERCASE e sem espacos nas extremidades, resultado da
     * normalizacao aplicada no construtor e em {@link #atualizarDados(String, String, BigDecimal,
     * boolean, String)}.
     *
     * @return nome do item em UPPERCASE (ex: {@code "FEIJOADA COMPLETA"})
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descricao do item do cardapio.
     *
     * <p>A descricao e armazenada sem espacos nas extremidades (trim aplicado).
     *
     * @return descricao do item
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o preco do item do cardapio.
     *
     * <p>O preco e sempre maior que zero, conforme validado na criacao e atualizacao. Utiliza
     * {@link BigDecimal} para garantir precisao monetaria.
     *
     * @return preco do item como {@link BigDecimal} (sempre maior que zero)
     */
    public BigDecimal getPreco() {
        return preco;
    }

    /**
     * Verifica se o item esta disponivel apenas para consumo no local.
     *
     * <p>Quando {@code true}, indica que o item nao pode ser pedido para delivery ou retirada,
     * sendo servido exclusivamente no restaurante.
     *
     * @return {@code true} se o item esta disponivel apenas no local
     */
    public boolean isApenasNoLocal() {
        return apenasNoLocal;
    }

    /**
     * Retorna o caminho da foto do item do cardapio.
     *
     * <p>Campo opcional. Retorna {@code null} se nenhuma foto foi associada ao item ou se o caminho
     * informado era vazio/em branco.
     *
     * @return caminho da foto ou {@code null} se nao informado
     */
    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    /**
     * Retorna o restaurante ao qual o item do cardapio pertence.
     *
     * <p>O restaurante e definido na criacao do item e nao pode ser alterado pelo metodo {@link
     * #atualizarDados(String, String, BigDecimal, boolean, String)}.
     *
     * @return restaurante proprietario do item
     */
    public Restaurante getRestaurante() {
        return restaurante;
    }

    /**
     * Verifica se o item do cardapio esta ativo no sistema.
     *
     * <p>Itens inativos sao resultado de soft delete via {@link #desativar()}.
     *
     * @return {@code true} se o item estiver ativo, {@code false} caso contrario
     */
    public boolean isAtivo() {
        return ativo;
    }

    // ========== Validacoes ==========

    private void validarCamposObrigatorios(
            String nome, String descricao, BigDecimal preco, Restaurante restaurante) {
        validarNome(nome);
        validarDescricao(descricao);
        validarPreco(preco);
        validarRestaurante(restaurante);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do item do cardapio e obrigatorio");
        }
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new ValidacaoException("Descricao do item do cardapio e obrigatoria");
        }
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null) {
            throw new ValidacaoException("Preco do item do cardapio e obrigatorio");
        }
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("Preco do item do cardapio deve ser maior que zero");
        }
    }

    private void validarRestaurante(Restaurante restaurante) {
        if (restaurante == null) {
            throw new ValidacaoException("Item do cardapio deve pertencer a um restaurante");
        }
    }

    // ========== Normalizacao ==========

    private String normalizarNome(String nome) {
        return nome.trim().toUpperCase();
    }

    private String normalizarDescricao(String descricao) {
        return descricao.trim();
    }

    private String normalizarCaminhoFoto(String caminhoFoto) {
        return caminhoFoto == null || caminhoFoto.trim().isEmpty() ? null : caminhoFoto.trim();
    }

    // ========== equals / hashCode / toString ==========

    /**
     * Compara dois itens de cardapio pela identidade (campo {@code id}).
     *
     * <p>Duas instancias de {@code Cardapio} sao consideradas iguais se possuirem o mesmo {@code
     * id}. Demais campos como nome, descricao e preco nao participam da comparacao de igualdade.
     *
     * @param o objeto a ser comparado
     * @return {@code true} se o objeto for um {@code Cardapio} com o mesmo id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cardapio that)) return false;
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
     * Retorna uma representacao textual do item do cardapio para fins de debug e log.
     *
     * <p>Inclui id, nome, descricao, preco, disponibilidade local e status de ativacao. O
     * restaurante nao e incluido para evitar referencia circular na saida.
     *
     * @return string descritiva do item do cardapio
     */
    @Override
    public String toString() {
        return "Cardapio{id="
                + id
                + ", nome='"
                + nome
                + "', descricao='"
                + descricao
                + "', preco="
                + preco
                + ", apenasNoLocal="
                + apenasNoLocal
                + ", ativo="
                + ativo
                + "}";
    }
}
