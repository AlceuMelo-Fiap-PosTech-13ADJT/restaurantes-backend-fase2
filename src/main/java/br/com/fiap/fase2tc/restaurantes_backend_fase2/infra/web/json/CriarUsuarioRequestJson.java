package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Payload para criação de um novo usuário")
public class CriarUsuarioRequestJson {

    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    @Schema(description = "Nome completo do usuário", example = "Maria Silva")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    @Schema(description = "Endereço de e-mail do usuário", example = "maria.silva@restaurante.com")
    private String email;

    @NotNull(message = "O ID do tipo de usuário é obrigatório")
    @Schema(description = "UUID do tipo de usuário associado", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID tipoUsuarioId;

    @Schema(description = "UUID do restaurante ao qual o usuário pertence (opcional se ainda não vinculado)", example = "e5b8d960-983b-486a-8b1b-6cb4be775e7a")
    private UUID restauranteId;
}
