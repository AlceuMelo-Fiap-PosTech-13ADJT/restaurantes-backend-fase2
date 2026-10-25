package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;

public interface BuscarUsuarioPorIdUsecase {

    UsuarioOutputDto executar(UUID id);
}
