package com.grupo3.postech.jilocomjurubeba.interfaces.rest.handler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.grupo3.postech.jilocomjurubeba.domain.exception.DominioException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

/**
 * Tratamento centralizado de exceções da API.
 *
 * <p>Converte exceções em respostas HTTP padronizadas seguindo RFC 7807 (Problem Detail). Todas as
 * respostas de erro seguem o mesmo formato para consistência.
 *
 * <p>Formato de resposta:
 *
 * <pre>
 * {
 *   "type": "https://api.jilo-com-jurubeba.com.br/erros/validacao",
 *   "title": "Erro de Validação",
 *   "status": 400,
 *   "detail": "Um ou mais campos são inválidos",
 *   "instance": "/v1/usuarios",
 *   "timestamp": "2024-01-15T10:30:00Z",
 *   "erros": { "email": "Email inválido" }
 * }
 * </pre>
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERRO_BASE_URI = "https://api.jilo-com-jurubeba.com.br/erros";
    private static final String PROPRIEDADE_TIMESTAMP = "timestamp";
    private static final String PROPRIEDADE_ERROS = "erros";

    /**
     * Trata excecoes de validacao do Bean Validation ({@code @Valid}).
     *
     * <p>Captura erros de {@link MethodArgumentNotValidException} lancados quando os campos de um
     * {@code @RequestBody} falham na validacao. Retorna um mapa de campo/mensagem no atributo
     * {@code erros} do {@link ProblemDetail}.
     *
     * @param ex excecao de validacao do Spring contendo os erros por campo
     * @return {@link ProblemDetail} com status 400 (Bad Request), tipo {@code /erros/validacao} e
     *     detalhes dos erros por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacaoSpring(MethodArgumentNotValidException ex) {
        log.warn("Erro de validação: {}", ex.getMessage());

        Map<String, String> erros = new HashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erros.put(erro.getField(), erro.getDefaultMessage());
        }

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create(ERRO_BASE_URI + "/validacao"));
        problemDetail.setTitle("Erro de Validação");
        problemDetail.setDetail("Um ou mais campos são inválidos");
        problemDetail.setProperty(PROPRIEDADE_TIMESTAMP, Instant.now());
        problemDetail.setProperty(PROPRIEDADE_ERROS, erros);

        return problemDetail;
    }

    /**
     * Trata excecoes de validacao originadas na camada de dominio.
     *
     * <p>Captura {@link ValidacaoException} lancadas quando entidades de dominio rejeitam dados
     * invalidos durante criacao ou atualizacao. Pode incluir erros por campo no atributo {@code
     * erros} quando disponivel.
     *
     * @param ex excecao de validacao do dominio com mensagem e possiveis erros por campo
     * @return {@link ProblemDetail} com status 400 (Bad Request), tipo {@code /erros/validacao} e
     *     detalhes da validacao
     */
    @ExceptionHandler(ValidacaoException.class)
    public ProblemDetail tratarValidacaoDominio(ValidacaoException ex) {
        log.warn("Erro de validação de domínio: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create(ERRO_BASE_URI + "/validacao"));
        problemDetail.setTitle("Erro de Validação");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty(PROPRIEDADE_TIMESTAMP, Instant.now());

        if (!ex.getErrosPorCampo().isEmpty()) {
            problemDetail.setProperty(PROPRIEDADE_ERROS, ex.getErrosPorCampo());
        }

        return problemDetail;
    }

    /**
     * Trata excecoes de entidade nao encontrada.
     *
     * <p>Captura {@link EntidadeNaoEncontradaException} lancadas quando uma busca por ID nao
     * encontra o registro solicitado. Inclui atributos {@code entidade} e {@code identificador} no
     * {@link ProblemDetail} para facilitar o diagnostico.
     *
     * @param ex excecao contendo o nome da entidade e o identificador buscado
     * @return {@link ProblemDetail} com status 404 (Not Found), tipo {@code /erros/nao-encontrado}
     *     e dados da entidade nao localizada
     */
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail tratarEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        log.warn("Entidade não encontrada: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create(ERRO_BASE_URI + "/nao-encontrado"));
        problemDetail.setTitle("Recurso Não Encontrado");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty(PROPRIEDADE_TIMESTAMP, Instant.now());
        problemDetail.setProperty("entidade", ex.getNomeEntidade());
        problemDetail.setProperty("identificador", ex.getIdentificador());

        return problemDetail;
    }

    /**
     * Trata excecoes de violacao de regra de negocio.
     *
     * <p>Captura {@link RegraDeNegocioException} lancadas quando uma operacao viola uma regra de
     * negocio (ex: nome de tipo de usuario ja existente, usuario nao e dono do restaurante,
     * credenciais invalidas).
     *
     * @param ex excecao de regra de negocio com mensagem descritiva
     * @return {@link ProblemDetail} com status 422 (Unprocessable Entity), tipo {@code
     *     /erros/regra-negocio} e descricao da violacao
     */
    @ExceptionHandler(RegraDeNegocioException.class)
    public ProblemDetail tratarRegraDeNegocio(RegraDeNegocioException ex) {
        log.warn("Violação de regra de negócio: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(ERRO_BASE_URI + "/regra-negocio"));
        problemDetail.setTitle("Regra de Negócio Violada");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty(PROPRIEDADE_TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /**
     * Trata excecoes genericas de dominio nao mapeadas pelos handlers especificos.
     *
     * <p>Funciona como fallback para subclasses de {@link DominioException} que nao sejam {@link
     * ValidacaoException}, {@link EntidadeNaoEncontradaException} ou {@link
     * RegraDeNegocioException}.
     *
     * @param ex excecao de dominio nao tratada por handler mais especifico
     * @return {@link ProblemDetail} com status 400 (Bad Request), tipo {@code /erros/dominio} e
     *     mensagem da excecao
     */
    @ExceptionHandler(DominioException.class)
    public ProblemDetail tratarDominioGenerico(DominioException ex) {
        log.error("Erro de domínio não tratado: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create(ERRO_BASE_URI + "/dominio"));
        problemDetail.setTitle("Erro de Domínio");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty(PROPRIEDADE_TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /**
     * Trata excecoes nao esperadas (fallback global).
     *
     * <p>Captura qualquer {@link Exception} nao tratada pelos handlers anteriores. Retorna uma
     * mensagem generica sem expor detalhes internos para evitar vazamento de informacoes sensiveis
     * em producao. O stack trace completo e registrado nos logs.
     *
     * @param ex excecao generica nao esperada
     * @return {@link ProblemDetail} com status 500 (Internal Server Error), tipo {@code
     *     /erros/erro-interno} e mensagem generica de erro
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarErroInterno(Exception ex) {
        log.error("Erro interno não esperado", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setType(URI.create(ERRO_BASE_URI + "/erro-interno"));
        problemDetail.setTitle("Erro Interno do Servidor");
        problemDetail.setDetail("Ocorreu um erro inesperado. Por favor, tente novamente.");
        problemDetail.setProperty(PROPRIEDADE_TIMESTAMP, Instant.now());

        // Não expor detalhes do erro interno em produção
        return problemDetail;
    }
}
