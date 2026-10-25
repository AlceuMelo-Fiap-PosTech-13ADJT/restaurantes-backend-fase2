package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.util.UUID;

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
public class CriarRestauranteRequestJson {

    @NotBlank(message = "O nome do restaurante é obrigatório")
    @Size(max = 150, message = "O nome do restaurante deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "O endereço do restaurante é obrigatório")
    @Size(max = 255, message = "O endereço do restaurante deve ter no máximo 255 caracteres")
    private String endereco;

    @NotBlank(message = "O tipo de cozinha é obrigatório")
    @Size(max = 100, message = "O tipo de cozinha deve ter no máximo 100 caracteres")
    private String tipoCozinha;

    @NotBlank(message = "O horário de funcionamento é obrigatório")
    @Size(max = 100, message = "O horário de funcionamento deve ter no máximo 100 caracteres")
    private String horarioFuncionamento;

    @NotNull(message = "O ID do dono do restaurante é obrigatório")
    private UUID donoId;
}
