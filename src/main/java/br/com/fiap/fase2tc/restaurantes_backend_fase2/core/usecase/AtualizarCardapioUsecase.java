package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;

public interface AtualizarCardapioUsecase {
    ItemCardapioOutputDto executar(ItemCardapioInputDto inputDto);
}
