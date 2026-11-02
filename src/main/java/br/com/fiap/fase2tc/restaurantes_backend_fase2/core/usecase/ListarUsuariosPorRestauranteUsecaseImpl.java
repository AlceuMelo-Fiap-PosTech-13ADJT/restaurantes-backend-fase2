package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.List;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.AcessoNaoAutorizadoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarUsuariosPorRestauranteUsecaseImpl implements ListarUsuariosPorRestauranteUsecase {

    private final UsuarioGateway usuarioGateway;
    private final RestauranteGateway restauranteGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public List<UsuarioOutputDto> executar(UUID restauranteId) {
        return executar(restauranteId, null);
    }

    @Override
    public List<UsuarioOutputDto> executar(UUID restauranteId, UUID usuarioOperadorId) {
        if (restauranteId == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }

        if (usuarioOperadorId != null) {
            Usuario operador = validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                    .usuarioOperadorId(usuarioOperadorId)
                    .restauranteId(restauranteId)
                    .requerDono(false)
                    .build());

            if (operador.getRestauranteId() != null && !operador.getRestauranteId().equals(restauranteId)) {
                throw new AcessoNaoAutorizadoException("Operador não pertence a este restaurante.");
            }
        } else {
            restauranteGateway.buscarAtivoPorId(restauranteId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));
        }

        return usuarioGateway.listarAtivosPorRestaurante(restauranteId).stream()
                .map(UsuarioOutputDto::fromDomain)
                .toList();
    }
}
