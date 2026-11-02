package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Estrutura padronizada para retorno de erros da API")
public class ErroResponseJson {

    @Schema(description = "Código de status HTTP correspondente ao erro", example = "400")
    private int status;

    @Schema(description = "Descrição detalhada do erro ou da regra violada", example = "O cabeçalho obrigatório 'usuario-logado-id' não foi fornecido.")
    private String mensagem;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Data e hora do registro da ocorrência do erro", example = "2026-10-25T14:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Lista de erros de validação de campo (quando aplicável)", example = "[\"nome: O nome do restaurante é obrigatório\"]")
    private List<String> erros;

    public ErroResponseJson(int status, String mensagem, LocalDateTime timestamp) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
        this.erros = Collections.emptyList();
    }
}
