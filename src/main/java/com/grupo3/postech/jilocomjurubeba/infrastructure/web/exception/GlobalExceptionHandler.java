package com.grupo3.postech.jilocomjurubeba.infrastructure.web.exception;

import com.grupo3.postech.jilocomjurubeba.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DominioException.class)
    public ResponseEntity<ProblemDetail> handleDominio(DominioException ex, HttpServletRequest req) {
        HttpStatus status = mapStatus(ex);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        pd.setTitle(status.getReasonPhrase());
        pd.setType(URI.create("https://jilo.com/problems/dominio"));
        pd.setInstance(URI.create(req.getRequestURI()));

        if (ex instanceof ValidacaoException ve) {
            pd.setProperty("errors", ve.getErrosPorCampo());
        }
        if (ex instanceof EntidadeNaoEncontradaException ene) {
            pd.setProperty("entity", ene.getNomeEntidade());
            pd.setProperty("id", ene.getIdentificador());
        }

        return ResponseEntity
            .status(status)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON) // ✅ força RFC 9457 no body
            .body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeral(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro inesperado"
        );
        pd.setTitle("Internal Server Error");
        pd.setType(URI.create("https://jilo.com/problems/internal"));
        pd.setInstance(URI.create(req.getRequestURI()));

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(pd);
    }

    private HttpStatus mapStatus(DominioException ex) {
        if (ex instanceof EntidadeNaoEncontradaException) return HttpStatus.NOT_FOUND;
        if (ex instanceof ValidacaoException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof RegraDeNegocioException) return HttpStatus.UNPROCESSABLE_ENTITY; // 422
        return HttpStatus.BAD_REQUEST;
    }
}
