package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CriarItemCardapioUsecaseImpl implements CriarItemCardapioUsecase {

    private final ItemCardapioGateway itemCardapioGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public ItemCardapioOutputDto executar(CriarItemCardapioInputDto inputDto) {
        if (inputDto == null) {
            throw new RegraNegocioException("Dados do item do cardápio são obrigatórios.");
        }
        if (inputDto.getRestauranteId() == null) {
            throw new RegraNegocioException("ID do restaurante é obrigatório.");
        }

        validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                .usuarioOperadorId(inputDto.getUsuarioOperadorId())
                .restauranteId(inputDto.getRestauranteId())
                .requerDono(true)
                .build());

        ItemCardapio item = ItemCardapio.builder()
                .nome(inputDto.getNome())
                .descricao(inputDto.getDescricao())
                .preco(inputDto.getPreco())
                .disponivelApenasRestaurante(inputDto.getDisponivelApenasRestaurante())
                .fotoPath(inputDto.getFotoPath())
                .ativo(true)
                .build();

        item.validarParaCriacao();
        ItemCardapio salvo = itemCardapioGateway.salvar(inputDto.getRestauranteId(), item);

        return ItemCardapioOutputDto.fromDomain(inputDto.getRestauranteId(), salvo);
    }
}
