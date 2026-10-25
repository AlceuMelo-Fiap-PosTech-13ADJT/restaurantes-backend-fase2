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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.RestauranteController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.handler.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de API REST: RestauranteApi")
class RestauranteApiTest {

    private MockMvc mockMvc;

    @Mock
    private RestauranteController restauranteController;

    @InjectMocks
    private RestauranteApi restauranteApi;

    private final UUID usuarioLogadoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restauranteApi)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve retornar 201 Created")
    void deveCriarRestaurante() throws Exception {
        UUID id = UUID.randomUUID();
        UUID donoId = UUID.randomUUID();

        when(restauranteController.criar(any(CriarRestauranteInputDto.class)))
                .thenReturn(new CriarRestauranteOutputDto(id));

        String json = """
                {
                    "nome": "Restaurante Italiano",
                    "endereco": "Rua Central, 100",
                    "tipoCozinha": "Italiana",
                    "horarioFuncionamento": "18:00 - 23:00",
                    "donoId": "%s"
                }
                """.formatted(donoId);

        mockMvc.perform(post("/v1/restaurantes")
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve retornar 200 OK")
    void deveAtualizarRestaurante() throws Exception {
        UUID id = UUID.randomUUID();
        RestauranteOutputDto output = RestauranteOutputDto.builder()
                .id(id)
                .nome("Nome Atualizado")
                .endereco("Endereco Atualizado")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("18:00 - 23:00")
                .ativo(true)
                .build();

        when(restauranteController.atualizar(any(AtualizarRestauranteInputDto.class))).thenReturn(output);

        String json = """
                {
                    "nome": "Nome Atualizado",
                    "endereco": "Endereco Atualizado",
                    "tipoCozinha": "Italiana",
                    "horarioFuncionamento": "18:00 - 23:00"
                }
                """;

        mockMvc.perform(put("/v1/restaurantes/" + id)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{id} deve retornar 200 OK")
    void deveBuscarRestaurantePorId() throws Exception {
        UUID id = UUID.randomUUID();
        RestauranteOutputDto output = RestauranteOutputDto.builder()
                .id(id)
                .nome("Restaurante X")
                .ativo(true)
                .build();

        when(restauranteController.buscarPorId(id)).thenReturn(output);

        mockMvc.perform(get("/v1/restaurantes/" + id)
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Restaurante X"));
    }

    @Test
    @DisplayName("GET /v1/restaurantes deve retornar 200 OK com lista")
    void deveListarRestaurantes() throws Exception {
        RestauranteOutputDto output = RestauranteOutputDto.builder()
                .id(UUID.randomUUID())
                .nome("Restaurante 1")
                .ativo(true)
                .build();

        when(restauranteController.listar()).thenReturn(List.of(output));

        mockMvc.perform(get("/v1/restaurantes")
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Restaurante 1"));
    }

    @Test
    @DisplayName("DELETE /v1/restaurantes/{id} deve retornar 204 No Content")
    void deveDesativarRestaurante() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/v1/restaurantes/" + id)
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isNoContent());

        verify(restauranteController).desativar(id, usuarioLogadoId);
    }

    @Test
    @DisplayName("POST /v1/restaurantes sem header deve retornar 400 Bad Request")
    void deveFalharSemHeader() throws Exception {
        mockMvc.perform(post("/v1/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
