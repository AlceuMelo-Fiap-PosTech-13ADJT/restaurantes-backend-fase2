package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.handler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.AcessoNaoAutorizadoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ErroResponseJson;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AcessoNaoAutorizadoException.class, DonoRestauranteInvalidoException.class})
    public ResponseEntity<ErroResponseJson> handleAcessoNaoAutorizado(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErroResponseJson(HttpStatus.FORBIDDEN.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseJson> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponseJson(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponseJson> handleRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseJson(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErroResponseJson> handleHeaderFaltante(MissingRequestHeaderException ex) {
        String msg = "O cabeçalho obrigatório '" + ex.getHeaderName() + "' não foi fornecido.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponseJson(HttpStatus.BAD_REQUEST.value(), msg, LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseJson> handleValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        ErroResponseJson corpo = ErroResponseJson.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem("Erro de validação nos dados fornecidos.")
                .timestamp(LocalDateTime.now())
                .erros(erros)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseJson> handleGenerico(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponseJson(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Ocorreu um erro interno no servidor.", LocalDateTime.now()));
    }
}
