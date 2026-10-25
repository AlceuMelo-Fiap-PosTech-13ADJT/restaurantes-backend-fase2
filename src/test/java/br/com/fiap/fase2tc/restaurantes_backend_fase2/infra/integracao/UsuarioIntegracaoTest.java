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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.UsuarioRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AssociarTipoUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AtualizarUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.CriarUsuarioRequestJson;

@SpringBootTest
@Transactional
@DisplayName("Testes de Integração Ponta a Ponta (E2E): Usuário")
class UsuarioIntegracaoTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String HEADER_USUARIO = "usuario-logado-id";

    // Dados pré-carregados pelo script data.sql
    private static final UUID RESTAURANTE_1_ID = UUID.fromString("c3b01f92-563b-4f68-b7e1-88892df9a001");
    private static final UUID DONO_R1_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1001");
    private static final UUID CLIENTE_R1_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1002");
    private static final UUID DONO_TIPO_ID = UUID.fromString("e1a8a992-1262-4f0e-b8d2-1c6d860d5b01");
    private static final UUID CLIENTE_TIPO_ID = UUID.fromString("e1a8a992-1262-4f0e-b8d2-1c6d860d5b02");

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Deve cadastrar novo usuário com sucesso e persistir no banco H2")
    void deveCadastrarUsuarioComSucessoEPersistirNoH2() throws Exception {
        CriarUsuarioRequestJson request = CriarUsuarioRequestJson.builder()
                .nome("Lucas Ferreira")
                .email("lucas.ferreira@bellanapoli.com")
                .tipoUsuarioId(CLIENTE_TIPO_ID)
                .restauranteId(RESTAURANTE_1_ID)
                .build();

        MvcResult result = mockMvc.perform(post("/v1/usuarios")
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome", is("Lucas Ferreira")))
                .andExpect(jsonPath("$.email", is("lucas.ferreira@bellanapoli.com")))
                .andExpect(jsonPath("$.restauranteId", is(RESTAURANTE_1_ID.toString())))
                .andExpect(jsonPath("$.tipoUsuarioId", is(CLIENTE_TIPO_ID.toString())))
                .andExpect(jsonPath("$.ativo", is(true)))
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID usuarioCriadoId = UUID.fromString(jsonNode.get("id").asText());

        // Validação direta no repositório JPA do H2
        UsuarioEntity entity = usuarioRepository.findById(usuarioCriadoId).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getNome()).isEqualTo("Lucas Ferreira");
        assertThat(entity.getEmail()).isEqualTo("lucas.ferreira@bellanapoli.com");
        assertThat(entity.getAtivo()).isTrue();
        assertThat(entity.getDataCriacao()).isNotNull();

        // Consulta via GET /v1/usuarios/{id}
        mockMvc.perform(get("/v1/usuarios/{id}", usuarioCriadoId)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Lucas Ferreira")));
    }

    @Test
    @DisplayName("Validação de unicidade: deve rejeitar cadastro com e-mail já existente com 422 Unprocessable Entity")
    void deveRejeitarCadastroComEmailDuplicado() throws Exception {
        CriarUsuarioRequestJson request = CriarUsuarioRequestJson.builder()
                .nome("Carlos Silva Clone")
                .email("carlos.silva@bellanapoli.com") // Já existente no seed data.sql
                .tipoUsuarioId(DONO_TIPO_ID)
                .restauranteId(RESTAURANTE_1_ID)
                .build();

        mockMvc.perform(post("/v1/usuarios")
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.mensagem", is("Já existe um usuário cadastrado com este e-mail.")));
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando tipo de usuário não existir com 404 Not Found")
    void deveRejeitarCadastroComTipoUsuarioInexistente() throws Exception {
        CriarUsuarioRequestJson request = CriarUsuarioRequestJson.builder()
                .nome("Usuario Teste")
                .email("usuario.teste@email.com")
                .tipoUsuarioId(UUID.randomUUID()) // Inexistente
                .restauranteId(RESTAURANTE_1_ID)
                .build();

        mockMvc.perform(post("/v1/usuarios")
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.mensagem", is("Tipo de usuário não encontrado.")));
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando restaurante não existir com 404 Not Found")
    void deveRejeitarCadastroComRestauranteInexistente() throws Exception {
        CriarUsuarioRequestJson request = CriarUsuarioRequestJson.builder()
                .nome("Usuario Teste")
                .email("usuario.teste@email.com")
                .tipoUsuarioId(CLIENTE_TIPO_ID)
                .restauranteId(UUID.randomUUID()) // Inexistente
                .build();

        mockMvc.perform(post("/v1/usuarios")
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.mensagem", is("Restaurante não encontrado.")));
    }

    @Test
    @DisplayName("Deve rejeitar cadastro com dados de entrada inválidos com 400 Bad Request")
    void deveRejeitarCadastroComDadosInvalidos() throws Exception {
        CriarUsuarioRequestJson request = CriarUsuarioRequestJson.builder()
                .nome("")
                .email("email-invalido")
                .tipoUsuarioId(null)
                .build();

        mockMvc.perform(post("/v1/usuarios")
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.erros").isArray());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        mockMvc.perform(get("/v1/usuarios/{id}", DONO_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(DONO_R1_ID.toString())))
                .andExpect(jsonPath("$.nome", is("Carlos Silva (Dono R1)")))
                .andExpect(jsonPath("$.email", is("carlos.silva@bellanapoli.com")))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar usuário por ID inexistente")
    void deveRetornarNotFoundAoBuscarUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/v1/usuarios/{id}", UUID.randomUUID())
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("Deve listar usuários por restaurante com sucesso")
    void deveListarUsuariosPorRestauranteComSucesso() throws Exception {
        mockMvc.perform(get("/v1/usuarios/restaurante/{restauranteId}", RESTAURANTE_1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].restauranteId", is(RESTAURANTE_1_ID.toString())));
    }

    @Test
    @DisplayName("Deve atualizar dados de usuário existente com sucesso")
    void deveAtualizarUsuarioComSucesso() throws Exception {
        AtualizarUsuarioRequestJson request = AtualizarUsuarioRequestJson.builder()
                .nome("Mariana Costa Atualizada")
                .email("mariana.atualizada@email.com")
                .tipoUsuarioId(CLIENTE_TIPO_ID)
                .restauranteId(RESTAURANTE_1_ID)
                .build();

        mockMvc.perform(put("/v1/usuarios/{id}", CLIENTE_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(CLIENTE_R1_ID.toString())))
                .andExpect(jsonPath("$.nome", is("Mariana Costa Atualizada")))
                .andExpect(jsonPath("$.email", is("mariana.atualizada@email.com")));

        // Validação no H2
        UsuarioEntity entity = usuarioRepository.findById(CLIENTE_R1_ID).orElseThrow();
        assertThat(entity.getNome()).isEqualTo("Mariana Costa Atualizada");
        assertThat(entity.getEmail()).isEqualTo("mariana.atualizada@email.com");
        assertThat(entity.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Deve associar tipo de usuário com sucesso")
    void deveAssociarTipoUsuarioComSucesso() throws Exception {
        AssociarTipoUsuarioRequestJson request = AssociarTipoUsuarioRequestJson.builder()
                .tipoUsuarioId(DONO_TIPO_ID)
                .build();

        mockMvc.perform(post("/v1/usuarios/{usuarioId}/tipo-usuario", CLIENTE_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(CLIENTE_R1_ID.toString())))
                .andExpect(jsonPath("$.tipoUsuarioId", is(DONO_TIPO_ID.toString())));

        // Validação no H2
        UsuarioEntity entity = usuarioRepository.findById(CLIENTE_R1_ID).orElseThrow();
        assertThat(entity.getTipoUsuario().getId()).isEqualTo(DONO_TIPO_ID);
    }

    @Test
    @DisplayName("Exclusão lógica (soft delete) mantém registro no banco com ativo = false")
    void deveDesativarUsuarioComSoftDelete() throws Exception {
        mockMvc.perform(delete("/v1/usuarios/{id}", CLIENTE_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isNoContent());

        // Consulta direta por ID deve retornar 404 Not Found após desativação
        mockMvc.perform(get("/v1/usuarios/{id}", CLIENTE_R1_ID)
                .header(HEADER_USUARIO, DONO_R1_ID.toString()))
                .andExpect(status().isNotFound());

        // No banco H2, o registro ainda existe fisicamente com ativo = false (Soft Delete comprovado)
        UsuarioEntity entity = usuarioRepository.findById(CLIENTE_R1_ID).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getAtivo()).isFalse();
        assertThat(entity.getDataAlteracao()).isNotNull();
    }
}
