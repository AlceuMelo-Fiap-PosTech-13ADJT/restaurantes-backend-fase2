package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.CardapioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.handler.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de API REST: CardapioApi")
class CardapioApiTest {

    private MockMvc mockMvc;

    @Mock
    private CardapioController cardapioController;

    @InjectMocks
    private CardapioApi cardapioApi;

    private final UUID usuarioLogadoId = UUID.randomUUID();
    private final UUID restauranteId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardapioApi)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/restaurantes/{restauranteId}/cardapio deve retornar 201 Created")
    void deveCriarItemCardapio() throws Exception {
        UUID itemId = UUID.randomUUID();
        ItemCardapioOutputDto output = ItemCardapioOutputDto.builder()
                .id(itemId)
                .restauranteId(restauranteId)
                .nome("Pizza Calabresa")
                .descricao("Molho, queijo, calabresa")
                .preco(new BigDecimal("42.00"))
                .disponivelApenasRestaurante(false)
                .ativo(true)
                .build();

        when(cardapioController.criar(any(CriarItemCardapioInputDto.class))).thenReturn(output);

        String json = """
                {
                    "nome": "Pizza Calabresa",
                    "descricao": "Molho, queijo, calabresa",
                    "preco": 42.00,
                    "disponivelApenasRestaurante": false
                }
                """;

        mockMvc.perform(post("/v1/restaurantes/" + restauranteId + "/cardapio")
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemId.toString()))
                .andExpect(jsonPath("$.nome").value("Pizza Calabresa"));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{restauranteId}/cardapio/{itemId} deve retornar 200 OK")
    void deveAtualizarItemCardapio() throws Exception {
        UUID itemId = UUID.randomUUID();
        ItemCardapioOutputDto output = ItemCardapioOutputDto.builder()
                .id(itemId)
                .restauranteId(restauranteId)
                .nome("Pizza Calabresa Especial")
                .descricao("Com cebola caramelizada")
                .preco(new BigDecimal("48.00"))
                .disponivelApenasRestaurante(false)
                .ativo(true)
                .build();

        when(cardapioController.atualizar(any(AtualizarItemCardapioInputDto.class))).thenReturn(output);

        String json = """
                {
                    "nome": "Pizza Calabresa Especial",
                    "descricao": "Com cebola caramelizada",
                    "preco": 48.00,
                    "disponivelApenasRestaurante": false
                }
                """;

        mockMvc.perform(put("/v1/restaurantes/" + restauranteId + "/cardapio/" + itemId)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId.toString()))
                .andExpect(jsonPath("$.nome").value("Pizza Calabresa Especial"));
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{restauranteId}/cardapio/{itemId} deve retornar 200 OK")
    void deveBuscarItemPorId() throws Exception {
        UUID itemId = UUID.randomUUID();
        ItemCardapioOutputDto output = ItemCardapioOutputDto.builder()
                .id(itemId)
                .restauranteId(restauranteId)
                .nome("Suco")
                .descricao("Laranja")
                .preco(new BigDecimal("9.00"))
                .disponivelApenasRestaurante(true)
                .ativo(true)
                .build();

        when(cardapioController.buscarPorId(restauranteId, itemId)).thenReturn(output);

        mockMvc.perform(get("/v1/restaurantes/" + restauranteId + "/cardapio/" + itemId)
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId.toString()))
                .andExpect(jsonPath("$.nome").value("Suco"));
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{restauranteId}/cardapio deve retornar 200 OK com lista")
    void deveListarItensCardapio() throws Exception {
        ItemCardapioOutputDto output = ItemCardapioOutputDto.builder()
                .id(UUID.randomUUID())
                .restauranteId(restauranteId)
                .nome("Item 1")
                .preco(BigDecimal.TEN)
                .ativo(true)
                .build();

        when(cardapioController.listarItens(restauranteId, usuarioLogadoId)).thenReturn(List.of(output));

        mockMvc.perform(get("/v1/restaurantes/" + restauranteId + "/cardapio")
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Item 1"));
    }

    @Test
    @DisplayName("DELETE /v1/restaurantes/{restauranteId}/cardapio/{itemId} deve retornar 204 No Content")
    void deveDesativarItemCardapio() throws Exception {
        UUID itemId = UUID.randomUUID();

        mockMvc.perform(delete("/v1/restaurantes/" + restauranteId + "/cardapio/" + itemId)
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isNoContent());

        verify(cardapioController).desativar(restauranteId, itemId, usuarioLogadoId);
    }

    @Test
    @DisplayName("POST sem header deve retornar 400 Bad Request")
    void deveFalharSemHeader() throws Exception {
        mockMvc.perform(post("/v1/restaurantes/" + restauranteId + "/cardapio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
