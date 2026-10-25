package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Payload para associação de tipo a um usuário")
public class AssociarTipoUsuarioRequestJson {

    @NotNull(message = "O ID do tipo de usuário é obrigatório")
    @Schema(description = "UUID do tipo de usuário a ser vinculado ao usuário", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID tipoUsuarioId;
}
