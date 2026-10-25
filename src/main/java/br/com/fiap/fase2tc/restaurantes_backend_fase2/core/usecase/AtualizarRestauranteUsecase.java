package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;

public interface AtualizarRestauranteUsecase {
    RestauranteOutputDto executar(AtualizarRestauranteInputDto inputDto);
}
