package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CriarItemCardapioInputDto {
    private UUID restauranteId;
    private UUID usuarioOperadorId;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivelApenasRestaurante;
    private String fotoPath;
}
