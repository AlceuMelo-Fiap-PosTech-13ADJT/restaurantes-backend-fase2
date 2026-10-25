package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.math.BigDecimal;

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
public class ItemCardapioRequestJson {

    @NotBlank(message = "O nome do item é obrigatório")
    @Size(max = 150, message = "O nome do item deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "A descrição do item é obrigatória")
    @Size(max = 500, message = "A descrição do item deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "A disponibilidade apenas no restaurante deve ser informada")
    private Boolean disponivelApenasRestaurante;

    @Size(max = 255, message = "O caminho da foto deve ter no máximo 255 caracteres")
    private String fotoPath;
}
