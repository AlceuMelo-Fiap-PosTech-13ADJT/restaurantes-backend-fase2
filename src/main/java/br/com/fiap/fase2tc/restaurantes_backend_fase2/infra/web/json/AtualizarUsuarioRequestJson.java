package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Payload para atualização de um usuário existente")
public class AtualizarUsuarioRequestJson {

    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    @Schema(description = "Nome completo do usuário", example = "Maria Silva Atualizada")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    @Schema(description = "Endereço de e-mail do usuário", example = "maria.atualizada@restaurante.com")
    private String email;

    @Schema(description = "UUID do tipo de usuário associado (opcional)", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID tipoUsuarioId;

    @Schema(description = "UUID do restaurante ao qual o usuário pertence (opcional)", example = "e5b8d960-983b-486a-8b1b-6cb4be775e7a")
    private UUID restauranteId;
}
