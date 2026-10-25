package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.List;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarRestaurantesUsecaseImpl implements ListarRestaurantesUsecase {

    private final RestauranteGateway restauranteGateway;

    @Override
    public List<RestauranteOutputDto> executar() {
        return restauranteGateway.listarAtivos()
                .stream()
                .map(RestauranteOutputDto::fromDomain)
                .toList();
    }
}
