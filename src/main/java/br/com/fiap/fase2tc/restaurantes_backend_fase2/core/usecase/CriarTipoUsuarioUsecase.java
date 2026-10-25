package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;

public interface CriarTipoUsuarioUsecase {
    TipoUsuarioOutputDto executar(CriarTipoUsuarioInputDto inputDto);
}
