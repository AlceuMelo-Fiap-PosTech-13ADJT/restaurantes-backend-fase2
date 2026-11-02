package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Payload para atualização de um tipo de usuário")
public class AtualizarTipoUsuarioRequestJson {

    @NotBlank(message = "O nome do tipo de usuário é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Schema(description = "Novo nome do tipo de usuário", example = "Cliente")
    private String nome;
}
