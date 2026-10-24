package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception;

public class AcessoNaoAutorizadoException extends RuntimeException {
    public AcessoNaoAutorizadoException(String message) {
        super(message);
    }
}
