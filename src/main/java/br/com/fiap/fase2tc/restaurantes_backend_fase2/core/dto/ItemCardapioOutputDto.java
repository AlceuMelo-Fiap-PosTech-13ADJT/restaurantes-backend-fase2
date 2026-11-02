package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCardapioOutputDto {
    private UUID id;
    private UUID restauranteId;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivelApenasRestaurante;
    private String fotoPath;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public static ItemCardapioOutputDto fromDomain(UUID restauranteId, ItemCardapio item) {
        if (item == null) {
            return null;
        }
        return ItemCardapioOutputDto.builder()
                .id(item.getId())
                .restauranteId(restauranteId)
                .nome(item.getNome())
                .descricao(item.getDescricao())
                .preco(item.getPreco())
                .disponivelApenasRestaurante(item.getDisponivelApenasRestaurante())
                .fotoPath(item.getFotoPath())
                .ativo(item.getAtivo())
                .dataCriacao(item.getDataCriacao())
                .dataAlteracao(item.getDataAlteracao())
                .build();
    }
}
