package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;

public interface BuscarTipoUsuarioPorIdUsecase {
    TipoUsuarioOutputDto executar(UUID id);
}
