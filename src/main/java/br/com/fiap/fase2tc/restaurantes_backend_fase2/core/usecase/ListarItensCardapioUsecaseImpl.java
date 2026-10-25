package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.List;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarItensCardapioUsecaseImpl implements ListarItensCardapioUsecase {

    private final ItemCardapioGateway itemCardapioGateway;
    private final RestauranteGateway restauranteGateway;

    @Override
    public List<ItemCardapioOutputDto> executar(UUID restauranteId) {
        if (restauranteId == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }

        restauranteGateway.buscarAtivoPorId(restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));

        return itemCardapioGateway.listarAtivosPorRestaurante(restauranteId)
                .stream()
                .map(item -> ItemCardapioOutputDto.fromDomain(restauranteId, item))
                .toList();
    }
}
