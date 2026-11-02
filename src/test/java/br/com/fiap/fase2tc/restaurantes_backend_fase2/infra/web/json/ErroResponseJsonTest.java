package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de DTOs JSON: ErroResponse")
class ErroResponseJsonTest {

    @Test
    @DisplayName("ErroResponseJson deve inicializar corretamente com status e mensagem")
    void erroResponseInicializacao() {
        LocalDateTime agora = LocalDateTime.now();
        ErroResponseJson response = new ErroResponseJson(404, "Recurso não encontrado", agora);

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMensagem()).isEqualTo("Recurso não encontrado");
        assertThat(response.getTimestamp()).isEqualTo(agora);
        assertThat(response.getErros()).isEmpty();
    }

    @Test
    @DisplayName("ErroResponseJson com lista detalhada de erros")
    void erroResponseComListaDeErros() {
        LocalDateTime agora = LocalDateTime.now();
        List<String> erros = List.of("nome: obrigatório", "preco: deve ser maior que zero");

        ErroResponseJson response = ErroResponseJson.builder()
                .status(400)
                .mensagem("Erro de validação")
                .timestamp(agora)
                .erros(erros)
                .build();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getErros()).hasSize(2);
        assertThat(response.getErros()).contains("nome: obrigatório");
    }
}
