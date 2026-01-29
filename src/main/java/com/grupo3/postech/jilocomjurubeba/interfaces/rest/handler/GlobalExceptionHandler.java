package com.grupo3.postech.jilocomjurubeba.interfaces.rest.handler;

import com.grupo3.postech.jilocomjurubeba.domain.exception.DominioException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Tratamento centralizado de exceções da API.
 *
 * Converte exceções em respostas HTTP padronizadas seguindo RFC 7807 (Problem Detail).
 * Todas as respostas de erro seguem o mesmo formato para consistência.
 *
 * Formato de resposta:
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
 * @author Danilo Fernando
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERRO_BASE_URI = "https://api.jilo-com-jurubeba.com.br/erros";
    private static final String PROPRIEDADE_TIMESTAMP = "timestamp";
    private static final String PROPRIEDADE_ERROS = "erros";

    /**
     * Trata exceções de validação do Bean Validation (@Valid).
     *
     * @param ex exceção de validação
     * @return ProblemDetail com status 400 e detalhes dos erros por campo
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
     * Trata exceções de validação do domínio.
     *
     * @param ex exceção de validação
     * @return ProblemDetail com status 400 e detalhes dos erros
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
     * Trata exceções de entidade não encontrada.
     *
     * @param ex exceção de entidade não encontrada
     * @return ProblemDetail com status 404
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
     * Trata exceções de regra de negócio.
     *
     * @param ex exceção de regra de negócio
     * @return ProblemDetail com status 422 (Unprocessable Entity)
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
     * Trata exceções genéricas de domínio não mapeadas especificamente.
     *
     * @param ex exceção de domínio
     * @return ProblemDetail com status 400
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
     * Trata exceções não esperadas (fallback).
     *
     * @param ex exceção genérica
     * @return ProblemDetail com status 500
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
