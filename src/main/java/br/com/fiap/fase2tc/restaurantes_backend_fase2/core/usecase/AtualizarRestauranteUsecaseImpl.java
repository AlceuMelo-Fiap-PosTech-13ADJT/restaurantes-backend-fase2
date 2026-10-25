package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarRestauranteUsecaseImpl implements AtualizarRestauranteUsecase {

    private final RestauranteGateway restauranteGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public RestauranteOutputDto executar(AtualizarRestauranteInputDto inputDto) {
        if (inputDto == null || inputDto.getRestauranteId() == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }

        validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                .usuarioOperadorId(inputDto.getUsuarioOperadorId())
                .restauranteId(inputDto.getRestauranteId())
                .requerDono(true)
                .build());

        Restaurante restaurante = restauranteGateway.buscarAtivoPorId(inputDto.getRestauranteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));

        restaurante.atualizarDados(
                inputDto.getNome(),
                inputDto.getEndereco(),
                inputDto.getTipoCozinha(),
                inputDto.getHorarioFuncionamento());

        Restaurante atualizado = restauranteGateway.atualizar(restaurante);

        return RestauranteOutputDto.fromDomain(atualizado);
    }
}
