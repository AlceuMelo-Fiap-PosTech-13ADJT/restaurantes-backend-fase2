package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.List;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;

public interface ListarItensCardapioUsecase {
    List<ItemCardapioOutputDto> executar(UUID restauranteId);
}
