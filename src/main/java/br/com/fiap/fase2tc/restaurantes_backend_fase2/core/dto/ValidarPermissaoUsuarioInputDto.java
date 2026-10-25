package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidarPermissaoUsuarioInputDto {
    private UUID usuarioOperadorId;
    private UUID restauranteId;
    private boolean requerDono;
}
