package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import java.util.Arrays;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoUsuarioEnum {
    DONO_RESTAURANTE("Dono de restaurante"),
    CLIENTE("Cliente");

    private final String descricao;

    public static TipoUsuarioEnum fromDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraNegocioException("Descrição do tipo de usuário é obrigatória.");
        }
        return Arrays.stream(TipoUsuarioEnum.values())
                .filter(tipo -> tipo.getDescricao().equalsIgnoreCase(descricao.trim())
                        || tipo.name().equalsIgnoreCase(descricao.trim()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tipo de usuário inválido: " + descricao));
    }

    public boolean isDonoRestaurante() {
        return this == DONO_RESTAURANTE;
    }

    public boolean isCliente() {
        return this == CLIENTE;
    }

}
