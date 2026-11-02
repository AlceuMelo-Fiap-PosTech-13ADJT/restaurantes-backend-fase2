package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarItemCardapioUsecaseImpl implements AtualizarItemCardapioUsecase {

    private final ItemCardapioGateway itemCardapioGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public ItemCardapioOutputDto executar(AtualizarItemCardapioInputDto inputDto) {
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

        item.atualizarDados(
                inputDto.getNome(),
                inputDto.getDescricao(),
                inputDto.getPreco(),
                inputDto.getDisponivelApenasRestaurante(),
                inputDto.getFotoPath());

        ItemCardapio atualizado = itemCardapioGateway.atualizar(inputDto.getRestauranteId(), item);

        return ItemCardapioOutputDto.fromDomain(inputDto.getRestauranteId(), atualizado);
    }
}
