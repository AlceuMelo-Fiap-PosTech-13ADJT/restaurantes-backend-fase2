package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCardapioResponseJson {

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

    public ItemCardapioResponseJson(UUID id) {
        this.id = id;
    }

    public static ItemCardapioResponseJson fromDto(ItemCardapioOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return ItemCardapioResponseJson.builder()
                .id(dto.getId())
                .restauranteId(dto.getRestauranteId())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .disponivelApenasRestaurante(dto.getDisponivelApenasRestaurante())
                .fotoPath(dto.getFotoPath())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .dataAlteracao(dto.getDataAlteracao())
                .build();
    }
}
