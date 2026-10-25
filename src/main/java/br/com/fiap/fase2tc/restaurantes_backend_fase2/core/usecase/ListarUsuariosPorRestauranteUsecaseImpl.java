package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.List;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarUsuariosPorRestauranteUsecaseImpl implements ListarUsuariosPorRestauranteUsecase {

    private final UsuarioGateway usuarioGateway;
    private final RestauranteGateway restauranteGateway;

    @Override
    public List<UsuarioOutputDto> executar(UUID restauranteId) {
        if (restauranteId == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }

        restauranteGateway.buscarAtivoPorId(restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));

        return usuarioGateway.listarAtivosPorRestaurante(restauranteId).stream()
                .map(UsuarioOutputDto::fromDomain)
                .toList();
    }
}
