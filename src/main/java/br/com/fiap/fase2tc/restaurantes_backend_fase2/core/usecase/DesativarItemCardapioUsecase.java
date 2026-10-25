package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarItemCardapioInputDto;

public interface DesativarItemCardapioUsecase {
    void executar(DesativarItemCardapioInputDto inputDto);
}
