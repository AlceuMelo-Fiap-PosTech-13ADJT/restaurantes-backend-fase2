package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Payload para cadastro de um novo restaurante")
public class CriarRestauranteRequestJson {

    @NotBlank(message = "O nome do restaurante é obrigatório")
    @Size(max = 150, message = "O nome do restaurante deve ter no máximo 150 caracteres")
    @Schema(description = "Nome fantasia do restaurante", example = "Restaurante Sabor & Arte")
    private String nome;

    @NotBlank(message = "O endereço do restaurante é obrigatório")
    @Size(max = 255, message = "O endereço do restaurante deve ter no máximo 255 caracteres")
    @Schema(description = "Endereço completo do estabelecimento", example = "Av. Paulista, 1000 - Bela Vista, São Paulo - SP")
    private String endereco;

    @NotBlank(message = "O tipo de cozinha é obrigatório")
    @Size(max = 100, message = "O tipo de cozinha deve ter no máximo 100 caracteres")
    @Schema(description = "Especialidade ou tipo de culinária", example = "Italiana")
    private String tipoCozinha;

    @NotBlank(message = "O horário de funcionamento é obrigatório")
    @Size(max = 100, message = "O horário de funcionamento deve ter no máximo 100 caracteres")
    @Schema(description = "Dias e horários de funcionamento", example = "Seg a Sex: 11h30 às 23h | Sáb e Dom: 12h às 00h")
    private String horarioFuncionamento;

    @NotNull(message = "O ID do dono do restaurante é obrigatório")
    @Schema(description = "UUID do usuário cadastrado com perfil de Dono de restaurante", example = "b1a0f443-4318-4720-bd44-df41865c1001")
    private UUID donoId;
}
