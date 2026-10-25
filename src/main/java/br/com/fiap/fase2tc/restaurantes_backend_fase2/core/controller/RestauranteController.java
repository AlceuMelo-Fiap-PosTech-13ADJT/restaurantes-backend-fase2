package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller;

import java.util.List;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarRestaurantePorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarRestaurantesUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class RestauranteController {

    private final CriarRestauranteUsecase criarRestauranteUsecase;
    private final AtualizarRestauranteUsecase atualizarRestauranteUsecase;
    private final BuscarRestaurantePorIdUsecase buscarRestaurantePorIdUsecase;
    private final ListarRestaurantesUsecase listarRestaurantesUsecase;
    private final DesativarRestauranteUsecase desativarRestauranteUsecase;

    public RestauranteController(CriarRestauranteUsecase criarRestauranteUsecase) {
        this(criarRestauranteUsecase, null, null, null, null);
    }

    public CriarRestauranteOutputDto criar(CriarRestauranteInputDto inputDto) {
        return criarRestauranteUsecase.executar(inputDto);
    }

    public RestauranteOutputDto atualizar(AtualizarRestauranteInputDto inputDto) {
        return atualizarRestauranteUsecase.executar(inputDto);
    }

    public RestauranteOutputDto buscarPorId(UUID id) {
        return buscarRestaurantePorIdUsecase.executar(id);
    }

    public List<RestauranteOutputDto> listar() {
        return listarRestaurantesUsecase.executar();
    }

    public void desativar(DesativarRestauranteInputDto inputDto) {
        desativarRestauranteUsecase.executar(inputDto);
    }

    public void desativar(UUID restauranteId, UUID usuarioOperadorId) {
        desativar(DesativarRestauranteInputDto.builder()
                .restauranteId(restauranteId)
                .usuarioOperadorId(usuarioOperadorId)
                .build());
    }
}
