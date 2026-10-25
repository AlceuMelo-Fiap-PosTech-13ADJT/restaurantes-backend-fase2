package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
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
@Schema(description = "Representação detalhada de dados de um restaurante")
public class RestauranteResponseJson {

    @Schema(description = "Identificador único (UUID) do restaurante", example = "c3b01f92-563b-4f68-b7e1-88892df9a001")
    private UUID id;

    @Schema(description = "Nome do restaurante", example = "Restaurante Sabor & Arte")
    private String nome;

    @Schema(description = "Endereço completo", example = "Av. Paulista, 1000 - Bela Vista, São Paulo - SP")
    private String endereco;

    @Schema(description = "Especialidade ou tipo culinário", example = "Italiana")
    private String tipoCozinha;

    @Schema(description = "Horário de atendimento", example = "Seg a Sex: 11h30 às 23h")
    private String horarioFuncionamento;

    @Schema(description = "UUID do usuário proprietário vinculado", example = "b1a0f443-4318-4720-bd44-df41865c1001")
    private UUID donoId;

    @Schema(description = "Nome do proprietário", example = "Carlos Silva Dono")
    private String donoNome;

    @Schema(description = "Indicador de status ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Data e hora de cadastro", example = "2026-10-25T10:00:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Data e hora da última alteração", example = "2026-10-25T10:00:00")
    private LocalDateTime dataAlteracao;

    public RestauranteResponseJson(UUID id) {
        this.id = id;
    }

    public static RestauranteResponseJson fromDto(CriarRestauranteOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return new RestauranteResponseJson(dto.getId());
    }

    public static RestauranteResponseJson fromDto(RestauranteOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return RestauranteResponseJson.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .endereco(dto.getEndereco())
                .tipoCozinha(dto.getTipoCozinha())
                .horarioFuncionamento(dto.getHorarioFuncionamento())
                .donoId(dto.getDonoId())
                .donoNome(dto.getDonoNome())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .dataAlteracao(dto.getDataAlteracao())
                .build();
    }
}
