package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarRestauranteInputDto;

public interface DesativarRestauranteUsecase {
    void executar(DesativarRestauranteInputDto inputDto);
}
