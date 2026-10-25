package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarCardapioUsecaseImpl implements AtualizarCardapioUsecase {

    private final AtualizarItemCardapioUsecase atualizarItemCardapioUsecase;

    @Override
    public ItemCardapioOutputDto executar(ItemCardapioInputDto inputDto) {
        if (inputDto == null) {
            throw new RegraNegocioException("Dados do item do cardápio são obrigatórios.");
        }

        var atualizarDto = AtualizarItemCardapioInputDto.builder()
                .restauranteId(inputDto.getRestauranteId())
                .itemId(inputDto.getId())
                .usuarioOperadorId(inputDto.getUsuarioOperadorId())
                .nome(inputDto.getNome())
                .descricao(inputDto.getDescricao())
                .preco(inputDto.getPreco())
                .disponivelApenasRestaurante(inputDto.getDisponivelApenasRestaurante())
                .fotoPath(inputDto.getFotoPath())
                .build();

        return atualizarItemCardapioUsecase.executar(atualizarDto);
    }
}
