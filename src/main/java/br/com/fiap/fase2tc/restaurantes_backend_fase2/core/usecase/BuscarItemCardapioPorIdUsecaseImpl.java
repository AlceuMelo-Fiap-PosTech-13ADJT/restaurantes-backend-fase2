package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarItemCardapioPorIdUsecaseImpl implements BuscarItemCardapioPorIdUsecase {

    private final ItemCardapioGateway itemCardapioGateway;
    private final RestauranteGateway restauranteGateway;

    @Override
    public ItemCardapioOutputDto executar(UUID restauranteId, UUID itemId) {
        if (restauranteId == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }
        if (itemId == null) {
            throw new RegraNegocioException("ID do item do cardápio é obrigatório.");
        }

        restauranteGateway.buscarAtivoPorId(restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));

        ItemCardapio item = itemCardapioGateway.buscarAtivoPorId(restauranteId, itemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item do cardápio não encontrado."));

        return ItemCardapioOutputDto.fromDomain(restauranteId, item);
    }
}
