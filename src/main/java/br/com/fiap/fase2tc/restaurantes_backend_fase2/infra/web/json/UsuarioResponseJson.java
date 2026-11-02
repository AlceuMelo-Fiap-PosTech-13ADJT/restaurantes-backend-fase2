package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
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
@Schema(description = "Representação dos dados do usuário associado")
public class UsuarioResponseJson {

    @Schema(description = "Identificador único (UUID) do usuário", example = "b1a0f443-4318-4720-bd44-df41865c1001")
    private UUID id;

    @Schema(description = "Nome do usuário", example = "Carlos Silva Dono")
    private String nome;

    @Schema(description = "Endereço de e-mail", example = "carlos.dono@restaurante.com")
    private String email;

    @Schema(description = "UUID do restaurante ao qual o usuário pertence", example = "e5b8d960-983b-486a-8b1b-6cb4be775e7a")
    private UUID restauranteId;

    @Schema(description = "UUID do tipo de usuário vinculado", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID tipoUsuarioId;

    @Schema(description = "Nome do perfil/tipo de usuário", example = "Dono de restaurante")
    private String tipoUsuarioNome;

    @Schema(description = "Indicador de status ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Data e hora de criação", example = "2026-10-25T10:00:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Data e hora da última alteração", example = "2026-10-25T10:00:00")
    private LocalDateTime dataAlteracao;

    public UsuarioResponseJson(UUID id) {
        this.id = id;
    }

    public static UsuarioResponseJson fromDto(UsuarioOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return UsuarioResponseJson.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .restauranteId(dto.getRestauranteId())
                .tipoUsuarioId(dto.getTipoUsuarioId())
                .tipoUsuarioNome(dto.getTipoUsuarioNome())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .dataAlteracao(dto.getDataAlteracao())
                .build();
    }
}
