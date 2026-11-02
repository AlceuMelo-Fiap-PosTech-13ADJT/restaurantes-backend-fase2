package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesativarItemCardapioUsecaseImpl implements DesativarItemCardapioUsecase {

    private final ItemCardapioGateway itemCardapioGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public void executar(DesativarItemCardapioInputDto inputDto) {
        if (inputDto == null || inputDto.getRestauranteId() == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }
        if (inputDto.getItemId() == null) {
            throw new RegraNegocioException("ID do item do cardápio é obrigatório.");
        }

        validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                .usuarioOperadorId(inputDto.getUsuarioOperadorId())
                .restauranteId(inputDto.getRestauranteId())
                .requerDono(true)
                .build());

        ItemCardapio item = itemCardapioGateway.buscarAtivoPorId(inputDto.getRestauranteId(), inputDto.getItemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item do cardápio não encontrado."));

        item.desativar();
        itemCardapioGateway.desativar(inputDto.getRestauranteId(), inputDto.getItemId());
    }
}
