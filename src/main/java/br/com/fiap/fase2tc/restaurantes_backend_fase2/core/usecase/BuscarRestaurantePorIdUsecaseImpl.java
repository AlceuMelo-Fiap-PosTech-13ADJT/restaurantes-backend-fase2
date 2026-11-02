package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarRestaurantePorIdUsecaseImpl implements BuscarRestaurantePorIdUsecase {

    private final RestauranteGateway restauranteGateway;

    @Override
    public RestauranteOutputDto executar(UUID id) {
        if (id == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }

        Restaurante restaurante = restauranteGateway.buscarAtivoPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));

        return RestauranteOutputDto.fromDomain(restaurante);
    }
}
