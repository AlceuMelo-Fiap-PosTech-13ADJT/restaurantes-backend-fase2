package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.integracao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.RestauranteEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.RestauranteRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AtualizarRestauranteRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.CriarRestauranteRequestJson;

@SpringBootTest
@Transactional
@DisplayName("Testes de Integração Ponta a Ponta (E2E): Restaurante")
class RestauranteIntegracaoTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private RestauranteRepository restauranteRepository;

    private static final String HEADER_USUARIO = "usuario-logado-id";

    // Dados pré-carregados pelo script data.sql
    private static final UUID RESTAURANTE_1_ID = UUID.fromString("c3b01f92-563b-4f68-b7e1-88892df9a001");
    private static final UUID RESTAURANTE_2_ID = UUID.fromString("c3b01f92-563b-4f68-b7e1-88892df9a002");
    private static final UUID DONO_R1_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1001");
    private static final UUID CLIENTE_R1_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1002");
    private static final UUID DONO_R2_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1003");
    private static final UUID CLIENTE_R2_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1004");

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Deve cadastrar restaurante com sucesso pelo dono e persistir no banco H2")
    void deveCadastrarRestauranteComSucessoPeloDonoEPersistirNoH2() throws Exception {
        CriarRestauranteRequestJson request = CriarRestauranteRequestJson.builder()
                .nome("Trattoria Bella Vista")
                .endereco("Alameda Santos, 1200 - Cerqueira César, São Paulo - SP")
                .tipoCozinha("Italiana Contemporânea")
                .horarioFuncionamento("12:00 às 23:00")
                .donoId(DONO_R1_ID)
                .build();

        MvcResult result = mockMvc.perform(post("/v1/restaurantes")
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID restauranteCriadoId = UUID.fromString(jsonNode.get("id").asText());

        // Valida persistência direta no repositório JPA do H2
        RestauranteEntity entity = restauranteRepository.findById(restauranteCriadoId).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getNome()).isEqualTo("Trattoria Bella Vista");
        assertThat(entity.getAtivo()).isTrue();
        assertThat(entity.getDataCriacao()).isNotNull();

        // Valida consulta HTTP GET com sucesso
        mockMvc.perform(get("/v1/restaurantes/{id}", restauranteCriadoId)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(restauranteCriadoId.toString())))
                .andExpect(jsonPath("$.nome", is("Trattoria Bella Vista")))
                .andExpect(jsonPath("$.tipoCozinha", is("Italiana Contemporânea")))
                .andExpect(jsonPath("$.donoId", is(DONO_R1_ID.toString())))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("Tentativa de cadastro por cliente deve gerar 403 Forbidden")
    void deveRejeitarCadastroRestauranteQuandoUsuarioNaoForDono() throws Exception {
        CriarRestauranteRequestJson request = CriarRestauranteRequestJson.builder()
                .nome("Restaurante Sem Dono Valido")
                .endereco("Rua Teste, 99")
                .tipoCozinha("Variada")
                .horarioFuncionamento("12:00 às 20:00")
                .donoId(CLIENTE_R1_ID) // Cliente não tem perfil de DONO_RESTAURANTE
                .build();

        mockMvc.perform(post("/v1/restaurantes")
                .header(HEADER_USUARIO, CLIENTE_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.mensagem").isNotEmpty());
    }

    @Test
    @DisplayName("Dono atualizando restaurante existente deve retornar 200 OK com dados refletidos")
    void deveAtualizarRestauranteComSucessoPeloDono() throws Exception {
        AtualizarRestauranteRequestJson request = AtualizarRestauranteRequestJson.builder()
                .nome("Cantina Bella Roma - Reforma")
                .endereco("Rua Augusta, 150 - Cerqueira Cesar, São Paulo - SP")
                .tipoCozinha("Italiana Tradicional")
                .horarioFuncionamento("11:30 às 23:30")
                .build();

        mockMvc.perform(put("/v1/restaurantes/{id}", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(RESTAURANTE_1_ID.toString())))
                .andExpect(jsonPath("$.nome", is("Cantina Bella Roma - Reforma")))
                .andExpect(jsonPath("$.tipoCozinha", is("Italiana Tradicional")));

        // Validação no H2
        RestauranteEntity entity = restauranteRepository.findById(RESTAURANTE_1_ID).orElseThrow();
        assertThat(entity.getNome()).isEqualTo("Cantina Bella Roma - Reforma");
        assertThat(entity.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Tentativa de atualização por outro usuário (não dono) deve gerar 403 Forbidden")
    void deveRejeitarAtualizacaoRestaurantePorOutroUsuarioNaoDono() throws Exception {
        AtualizarRestauranteRequestJson request = AtualizarRestauranteRequestJson.builder()
                .nome("Tentativa de Alteração Indevida")
                .endereco("Rua Hacker, 1")
                .tipoCozinha("Geral")
                .horarioFuncionamento("10:00 às 22:00")
                .build();

        // Dono do Restaurante 2 tentando atualizar o Restaurante 1
        mockMvc.perform(put("/v1/restaurantes/{id}", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R2_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));

        // Cliente do Restaurante 1 tentando atualizar o Restaurante 1
        mockMvc.perform(put("/v1/restaurantes/{id}", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, CLIENTE_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));
    }

    @Test
    @DisplayName("Isolamento multi-tenant: exclusão lógica (soft delete) e restrição de acesso por operador")
    void deveGarantirIsolamentoMultiTenantNaDesativacaoEListagem() throws Exception {
        // Tentativa de desativação do Restaurante 1 pelo Dono do Restaurante 2 -> 403 Forbidden
        mockMvc.perform(delete("/v1/restaurantes/{id}", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R2_ID.toString()))
                .andExpect(status().isForbidden());

        // Dono R1 desativa seu próprio restaurante -> 204 No Content
        mockMvc.perform(delete("/v1/restaurantes/{id}", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isNoContent());

        // Consulta direta por ID deve retornar 404 Not Found (pois soft delete marca ativo = false)
        mockMvc.perform(get("/v1/restaurantes/{id}", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isNotFound());

        // No banco H2, o registro ainda existe fisicamente (Soft Delete comprovado)
        RestauranteEntity entity = restauranteRepository.findById(RESTAURANTE_1_ID).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getAtivo()).isFalse();
        assertThat(entity.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Deve listar restaurantes ativos com sucesso")
    void deveListarRestaurantesAtivosComSucesso() throws Exception {
        mockMvc.perform(get("/v1/restaurantes")
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }
}
