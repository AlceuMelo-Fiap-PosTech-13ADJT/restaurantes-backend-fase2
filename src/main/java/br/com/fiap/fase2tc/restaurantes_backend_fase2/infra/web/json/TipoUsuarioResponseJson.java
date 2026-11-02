package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
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
@Schema(description = "Representação de dados de um tipo de usuário")
public class TipoUsuarioResponseJson {

    @Schema(description = "Identificador único (UUID) do tipo de usuário", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID id;

    @Schema(description = "Constante enumerada do subtipo de usuário", example = "DONO_RESTAURANTE")
    private TipoUsuarioEnum tipo;

    @Schema(description = "Nome descritivo do tipo de usuário", example = "Dono de restaurante")
    private String nome;

    @Schema(description = "Indicador de status ativo no sistema", example = "true")
    private Boolean ativo;

    @Schema(description = "Data e hora de criação do registro", example = "2026-10-25T10:00:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Data e hora da última alteração do registro", example = "2026-10-25T10:00:00")
    private LocalDateTime dataAlteracao;

    public TipoUsuarioResponseJson(UUID id) {
        this.id = id;
    }

    public static TipoUsuarioResponseJson fromDto(TipoUsuarioOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return TipoUsuarioResponseJson.builder()
                .id(dto.getId())
                .tipo(dto.getTipo())
                .nome(dto.getNome())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .dataAlteracao(dto.getDataAlteracao())
                .build();
    }
}
