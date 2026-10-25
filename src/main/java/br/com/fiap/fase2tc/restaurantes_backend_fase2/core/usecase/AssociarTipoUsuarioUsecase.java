package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;

public interface AssociarTipoUsuarioUsecase {
    UsuarioOutputDto executar(AssociarTipoUsuarioInputDto inputDto);
}
