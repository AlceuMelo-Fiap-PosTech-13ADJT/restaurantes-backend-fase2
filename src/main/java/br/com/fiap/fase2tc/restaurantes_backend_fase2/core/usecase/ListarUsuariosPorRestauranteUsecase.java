package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.List;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;

public interface ListarUsuariosPorRestauranteUsecase {

    List<UsuarioOutputDto> executar(UUID restauranteId);
    List<UsuarioOutputDto> executar(UUID restauranteId, UUID usuarioOperadorId);
}
