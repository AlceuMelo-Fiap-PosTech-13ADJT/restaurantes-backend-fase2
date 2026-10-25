package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;

public interface ValidarPermissaoUsuarioUsecase {
    Usuario executar(ValidarPermissaoUsuarioInputDto inputDto);
}
