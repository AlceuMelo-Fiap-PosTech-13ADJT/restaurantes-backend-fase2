package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
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
@Schema(description = "Payload para cadastro ou edição de item do cardápio")
public class ItemCardapioRequestJson {

    @NotBlank(message = "O nome do item é obrigatório")
    @Size(max = 150, message = "O nome do item deve ter no máximo 150 caracteres")
    @Schema(description = "Nome do prato ou bebida", example = "Risoto de Cogumelos Trufados")
    private String nome;

    @NotBlank(message = "A descrição do item é obrigatória")
    @Size(max = 500, message = "A descrição do item deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição detalhada dos ingredientes e preparo", example = "Arroz arbóreo, cogumelos paris e shimeji salteados, azeite trufado e queijo parmesão ralado.")
    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    @Schema(description = "Preço unitário do item em Reais (BRL)", example = "68.90")
    private BigDecimal preco;

    @NotNull(message = "A disponibilidade apenas no restaurante deve ser informada")
    @Schema(description = "Indica se o prato só pode ser consumido presencialmente no salão do restaurante", example = "false")
    private Boolean disponivelApenasRestaurante;

    @Size(max = 255, message = "O caminho da foto deve ter no máximo 255 caracteres")
    @Schema(description = "Caminho do arquivo ou URI da imagem do prato", example = "/uploads/cardapio/risoto-trufado.jpg")
    private String fotoPath;
}
