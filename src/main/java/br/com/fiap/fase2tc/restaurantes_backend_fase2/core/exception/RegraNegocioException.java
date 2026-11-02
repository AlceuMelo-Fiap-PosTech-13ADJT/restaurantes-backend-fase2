package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception;

import lombok.Getter;

@Getter
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String message) {
        super(message);
    }
}
