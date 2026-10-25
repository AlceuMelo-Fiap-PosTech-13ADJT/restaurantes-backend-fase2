package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Representação de dados de um item do cardápio")
public class ItemCardapioResponseJson {

    @Schema(description = "Identificador único (UUID) do item", example = "d4c02e81-789a-4bc3-a982-123456789abc")
    private UUID id;

    @Schema(description = "UUID do restaurante proprietário", example = "c3b01f92-563b-4f68-b7e1-88892df9a001")
    private UUID restauranteId;

    @Schema(description = "Nome do prato ou bebida", example = "Risoto de Cogumelos Trufados")
    private String nome;

    @Schema(description = "Descrição dos ingredientes e preparo", example = "Arroz arbóreo, cogumelos salteados, azeite trufado e queijo parmesão.")
    private String descricao;

    @Schema(description = "Preço unitário em Reais (BRL)", example = "68.90")
    private BigDecimal preco;

    @Schema(description = "Indica se o item é exclusivo para consumo no salão", example = "false")
    private Boolean disponivelApenasRestaurante;

    @Schema(description = "Caminho ou URI da foto do prato", example = "/uploads/cardapio/risoto-trufado.jpg")
    private String fotoPath;

    @Schema(description = "Status ativo no cardápio", example = "true")
    private Boolean ativo;

    @Schema(description = "Data e hora de cadastro", example = "2026-10-25T10:00:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Data e hora da última alteração", example = "2026-10-25T10:00:00")
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
