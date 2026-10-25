package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;

public interface AtualizarUsuarioUsecase {

    UsuarioOutputDto executar(AtualizarUsuarioInputDto inputDto);
}
