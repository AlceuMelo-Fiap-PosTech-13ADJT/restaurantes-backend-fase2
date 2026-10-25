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

import java.math.BigDecimal;
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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.ItemCardapioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.ItemCardapioRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ItemCardapioRequestJson;

@SpringBootTest
@Transactional
@DisplayName("Testes de Integração Ponta a Ponta (E2E): Cardápio")
class CardapioIntegracaoTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    private static final String HEADER_USUARIO = "usuario-logado-id";

    // Dados pré-carregados pelo script data.sql
    private static final UUID RESTAURANTE_1_ID = UUID.fromString("c3b01f92-563b-4f68-b7e1-88892df9a001");
    private static final UUID RESTAURANTE_2_ID = UUID.fromString("c3b01f92-563b-4f68-b7e1-88892df9a002");
    private static final UUID DONO_R1_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1001");
    private static final UUID CLIENTE_R1_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1002");
    private static final UUID DONO_R2_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1003");
    private static final UUID CLIENTE_R2_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1004");
    private static final UUID ITEM_R1_ID = UUID.fromString("f4c02a83-674c-4e79-c8f2-99903ef0b001");

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Dono criando item de cardápio com sucesso deve retornar 201 Created e persistir no H2")
    void deveCriarItemCardapioComSucessoPeloDono() throws Exception {
        ItemCardapioRequestJson request = ItemCardapioRequestJson.builder()
                .nome("Risoto de Funghi Secchi")
                .descricao("Arroz arbóreo italiano com seleção de cogumelos secos e queijo grana padano")
                .preco(new BigDecimal("68.50"))
                .disponivelApenasRestaurante(false)
                .fotoPath("/imagens/cardapio/risoto-funghi.jpg")
                .build();

        MvcResult result = mockMvc.perform(post("/v1/restaurantes/{restauranteId}/cardapio", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.restauranteId", is(RESTAURANTE_1_ID.toString())))
                .andExpect(jsonPath("$.nome", is("Risoto de Funghi Secchi")))
                .andExpect(jsonPath("$.preco", is(68.50)))
                .andExpect(jsonPath("$.ativo", is(true)))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID itemId = UUID.fromString(json.get("id").asText());

        // Validação direta no repositório JPA
        ItemCardapioEntity entity = itemCardapioRepository.findById(itemId).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getNome()).isEqualTo("Risoto de Funghi Secchi");
        assertThat(entity.getRestauranteId()).isEqualTo(RESTAURANTE_1_ID);
        assertThat(entity.getAtivo()).isTrue();
        assertThat(entity.getDataCriacao()).isNotNull();
    }

    @Test
    @DisplayName("Cliente consultando cardápio do restaurante deve retornar 200 OK com itens ativos")
    void devePermitirClienteConsultarCardapio() throws Exception {
        mockMvc.perform(get("/v1/restaurantes/{restauranteId}/cardapio", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, CLIENTE_R1_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].restauranteId", is(RESTAURANTE_1_ID.toString())))
                .andExpect(jsonPath("$[0].ativo", is(true)));
    }

    @Test
    @DisplayName("Cliente tentando criar item no cardápio deve receber 403 Forbidden")
    void deveRejeitarCriacaoItemPorCliente() throws Exception {
        ItemCardapioRequestJson request = ItemCardapioRequestJson.builder()
                .nome("Prato Proibido Cliente")
                .descricao("Tentativa não autorizada")
                .preco(new BigDecimal("30.00"))
                .disponivelApenasRestaurante(false)
                .build();

        mockMvc.perform(post("/v1/restaurantes/{restauranteId}/cardapio", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, CLIENTE_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));
    }

    @Test
    @DisplayName("Cliente tentando alterar item no cardápio deve receber 403 Forbidden")
    void deveRejeitarAlteracaoItemPorCliente() throws Exception {
        ItemCardapioRequestJson request = ItemCardapioRequestJson.builder()
                .nome("Pizza Margherita Alterada por Cliente")
                .descricao("Tentativa não autorizada")
                .preco(new BigDecimal("10.00"))
                .disponivelApenasRestaurante(false)
                .build();

        mockMvc.perform(put("/v1/restaurantes/{restauranteId}/cardapio/{itemId}", RESTAURANTE_1_ID, ITEM_R1_ID)
                .header(HEADER_USUARIO, CLIENTE_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));
    }

    @Test
    @DisplayName("Isolamento multi-tenant: usuário de outro restaurante tentando manipular cardápio deve receber 403 Forbidden")
    void deveRejeitarManipulacaoPorUsuarioDeOutroRestaurante() throws Exception {
        ItemCardapioRequestJson request = ItemCardapioRequestJson.builder()
                .nome("Item Invasor R2 em R1")
                .descricao("Tentativa de cross-tenant")
                .preco(new BigDecimal("99.00"))
                .disponivelApenasRestaurante(false)
                .build();

        // Dono do Restaurante 2 tentando criar item no Restaurante 1
        mockMvc.perform(post("/v1/restaurantes/{restauranteId}/cardapio", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R2_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));

        // Dono do Restaurante 2 tentando atualizar item do Restaurante 1
        mockMvc.perform(put("/v1/restaurantes/{restauranteId}/cardapio/{itemId}", RESTAURANTE_1_ID, ITEM_R1_ID)
                .header(HEADER_USUARIO, DONO_R2_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));

        // Dono do Restaurante 2 tentando desativar item do Restaurante 1
        mockMvc.perform(delete("/v1/restaurantes/{restauranteId}/cardapio/{itemId}", RESTAURANTE_1_ID, ITEM_R1_ID)
                .header(HEADER_USUARIO, DONO_R2_ID.toString()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));
    }

    @Test
    @DisplayName("Dono atualizando item do seu restaurante com sucesso deve retornar 200 OK")
    void deveAtualizarItemCardapioComSucessoPeloDono() throws Exception {
        ItemCardapioRequestJson request = ItemCardapioRequestJson.builder()
                .nome("Pizza Margherita Especial D.O.C.")
                .descricao("Tomates San Marzano, mozzarella di bufala campana e azeite extravirgem")
                .preco(new BigDecimal("72.00"))
                .disponivelApenasRestaurante(true)
                .fotoPath("/imagens/cardapio/margherita-doc.jpg")
                .build();

        mockMvc.perform(put("/v1/restaurantes/{restauranteId}/cardapio/{itemId}", RESTAURANTE_1_ID, ITEM_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ITEM_R1_ID.toString())))
                .andExpect(jsonPath("$.nome", is("Pizza Margherita Especial D.O.C.")))
                .andExpect(jsonPath("$.preco", is(72.00)))
                .andExpect(jsonPath("$.disponivelApenasRestaurante", is(true)));

        // Validação no H2
        ItemCardapioEntity entity = itemCardapioRepository.findById(ITEM_R1_ID).orElseThrow();
        assertThat(entity.getNome()).isEqualTo("Pizza Margherita Especial D.O.C.");
        assertThat(entity.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Exclusão lógica (soft delete) mantém registro no banco com ativo = false e omite das listagens regulares")
    void deveDesativarItemCardapioComSoftDelete() throws Exception {
        // Dono do Restaurante 1 desativa o item
        mockMvc.perform(delete("/v1/restaurantes/{restauranteId}/cardapio/{itemId}", RESTAURANTE_1_ID, ITEM_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isNoContent());

        // Consulta direta por ID deve retornar 404 Not Found (item inativo)
        mockMvc.perform(get("/v1/restaurantes/{restauranteId}/cardapio/{itemId}", RESTAURANTE_1_ID, ITEM_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isNotFound());

        // Listagem regular do cardápio não deve mais conter o item desativado
        MvcResult result = mockMvc.perform(get("/v1/restaurantes/{restauranteId}/cardapio", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).doesNotContain(ITEM_R1_ID.toString());

        // No banco de dados H2, o registro ainda existe fisicamente (Soft Delete comprovado)
        ItemCardapioEntity entity = itemCardapioRepository.findById(ITEM_R1_ID).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getAtivo()).isFalse();
        assertThat(entity.getDataAlteracao()).isNotNull();
    }
}
