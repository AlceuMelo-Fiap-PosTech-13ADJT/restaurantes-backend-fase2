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
@Schema(description = "Payload para atualização dos dados de um restaurante")
public class AtualizarRestauranteRequestJson {

    @NotBlank(message = "O nome do restaurante é obrigatório")
    @Size(max = 150, message = "O nome do restaurante deve ter no máximo 150 caracteres")
    @Schema(description = "Novo nome do restaurante", example = "Restaurante Sabor & Arte Bistrô")
    private String nome;

    @NotBlank(message = "O endereço do restaurante é obrigatório")
    @Size(max = 255, message = "O endereço do restaurante deve ter no máximo 255 caracteres")
    @Schema(description = "Novo endereço do restaurante", example = "Rua Augusta, 500 - Consolação, São Paulo - SP")
    private String endereco;

    @NotBlank(message = "O tipo de cozinha é obrigatório")
    @Size(max = 100, message = "O tipo de cozinha deve ter no máximo 100 caracteres")
    @Schema(description = "Novo tipo de culinária", example = "Contemporânea")
    private String tipoCozinha;

    @NotBlank(message = "O horário de funcionamento é obrigatório")
    @Size(max = 100, message = "O horário de funcionamento deve ter no máximo 100 caracteres")
    @Schema(description = "Novo horário de funcionamento", example = "Ter a Dom: 12h às 23h")
    private String horarioFuncionamento;
}
