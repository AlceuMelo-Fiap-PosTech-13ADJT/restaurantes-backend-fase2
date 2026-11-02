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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.TipoUsuarioRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AtualizarTipoUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.CriarTipoUsuarioRequestJson;

@SpringBootTest
@Transactional
@DisplayName("Testes de Integração Ponta a Ponta (E2E): Tipo de Usuário")
class TipoUsuarioIntegracaoTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    private static final String HEADER_USUARIO = "usuario-logado-id";

    // Dados pré-carregados pelo script data.sql
    private static final UUID DONO_TIPO_ID = UUID.fromString("e1a8a992-1262-4f0e-b8d2-1c6d860d5b01");
    private static final UUID CLIENTE_TIPO_ID = UUID.fromString("e1a8a992-1262-4f0e-b8d2-1c6d860d5b02");
    private static final UUID USUARIO_LOGADO_ID = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1001");

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Deve listar os tipos de usuários pré-carregados suportados pelo enum TipoUsuarioEnum")
    void deveListarTiposUsuarioComSucesso() throws Exception {
        mockMvc.perform(get("/v1/tipos-usuario")
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].tipo").isNotEmpty())
                .andExpect(jsonPath("$[0].nome").isNotEmpty())
                .andExpect(jsonPath("$[0].ativo", is(true)));
    }

    @Test
    @DisplayName("Deve buscar tipo de usuário por ID com sucesso no banco H2")
    void deveBuscarTipoUsuarioPorId() throws Exception {
        mockMvc.perform(get("/v1/tipos-usuario/{id}", DONO_TIPO_ID)
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(DONO_TIPO_ID.toString())))
                .andExpect(jsonPath("$.tipo", is("DONO_RESTAURANTE")))
                .andExpect(jsonPath("$.nome", is("Dono de restaurante")))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("Tentativa de cadastrar subtipo inválido que não pertence ao enum deve retornar 422 Unprocessable Entity")
    void deveRejeitarCadastroDeTipoNaoPertencenteAoEnum() throws Exception {
        CriarTipoUsuarioRequestJson request = CriarTipoUsuarioRequestJson.builder()
                .nome("GERENTE_ADMINISTRATIVO")
                .build();

        mockMvc.perform(post("/v1/tipos-usuario")
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.mensagem").isNotEmpty());
    }

    @Test
    @DisplayName("Validação de unicidade: tentativa de cadastrar tipo já existente deve retornar 422 Unprocessable Entity")
    void deveRejeitarCadastroDeTipoJaExistente() throws Exception {
        CriarTipoUsuarioRequestJson request = CriarTipoUsuarioRequestJson.builder()
                .nome("Dono de restaurante") // Já existente no data.sql
                .build();

        mockMvc.perform(post("/v1/tipos-usuario")
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.mensagem", is("Tipo de usuário já cadastrado.")));
    }

    @Test
    @DisplayName("Validação de campos obrigatórios: nome em branco deve retornar 400 Bad Request")
    void deveRejeitarCriacaoComCampoObrigatorioInvalido() throws Exception {
        CriarTipoUsuarioRequestJson request = CriarTipoUsuarioRequestJson.builder()
                .nome("")
                .build();

        mockMvc.perform(post("/v1/tipos-usuario")
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.erros").isArray());
    }

    @Test
    @DisplayName("Deve atualizar descrição de um tipo de usuário com sucesso")
    void deveAtualizarTipoUsuarioComSucesso() throws Exception {
        AtualizarTipoUsuarioRequestJson request = AtualizarTipoUsuarioRequestJson.builder()
                .nome("Dono de restaurante")
                .build();

        mockMvc.perform(put("/v1/tipos-usuario/{id}", DONO_TIPO_ID)
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(DONO_TIPO_ID.toString())))
                .andExpect(jsonPath("$.tipo", is("DONO_RESTAURANTE")))
                .andExpect(jsonPath("$.nome", is("Dono de restaurante")));

        // Validação no H2
        TipoUsuarioEntity entity = tipoUsuarioRepository.findById(DONO_TIPO_ID).orElseThrow();
        assertThat(entity.getNome()).isEqualTo("Dono de restaurante");
        assertThat(entity.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Exclusão lógica (soft delete) de tipo de usuário mantém registro no banco com ativo = false")
    void deveDesativarTipoUsuarioComSoftDelete() throws Exception {
        mockMvc.perform(delete("/v1/tipos-usuario/{id}", CLIENTE_TIPO_ID)
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString()))
                .andExpect(status().isNoContent());

        // Consulta direta por ID deve retornar 404 Not Found após desativação
        mockMvc.perform(get("/v1/tipos-usuario/{id}", CLIENTE_TIPO_ID)
                .header(HEADER_USUARIO, USUARIO_LOGADO_ID.toString()))
                .andExpect(status().isNotFound());

        // No banco H2, o registro ainda existe fisicamente com ativo = false
        TipoUsuarioEntity entity = tipoUsuarioRepository.findById(CLIENTE_TIPO_ID).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getAtivo()).isFalse();
        assertThat(entity.getDataAlteracao()).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden quando usuário cliente tentar criar tipo de usuário")
    void deveRetornar403QuandoClienteTentarCriarTipoUsuario() throws Exception {
        UUID clienteId = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1002");
        CriarTipoUsuarioRequestJson request = CriarTipoUsuarioRequestJson.builder()
                .nome("Cliente Especial")
                .build();

        mockMvc.perform(post("/v1/tipos-usuario")
                .header(HEADER_USUARIO, clienteId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));
    }
}
