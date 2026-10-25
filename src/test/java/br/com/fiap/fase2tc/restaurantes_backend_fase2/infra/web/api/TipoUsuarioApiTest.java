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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.TipoUsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.handler.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de API REST: TipoUsuarioApi")
class TipoUsuarioApiTest {

    private MockMvc mockMvc;

    @Mock
    private TipoUsuarioController tipoUsuarioController;

    @InjectMocks
    private TipoUsuarioApi tipoUsuarioApi;

    private final UUID usuarioLogadoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tipoUsuarioApi)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/tipos-usuario deve retornar 201 Created")
    void deveCriarTipoUsuario() throws Exception {
        UUID id = UUID.randomUUID();
        TipoUsuarioOutputDto output = TipoUsuarioOutputDto.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.CLIENTE)
                .nome("Cliente")
                .ativo(true)
                .build();

        when(tipoUsuarioController.criar(any(CriarTipoUsuarioInputDto.class))).thenReturn(output);

        String json = "{\"nome\": \"Cliente\"}";

        mockMvc.perform(post("/v1/tipos-usuario")
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Cliente"));
    }

    @Test
    @DisplayName("PUT /v1/tipos-usuario/{id} deve retornar 200 OK")
    void deveAtualizarTipoUsuario() throws Exception {
        UUID id = UUID.randomUUID();
        TipoUsuarioOutputDto output = TipoUsuarioOutputDto.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .nome("Dono de restaurante")
                .ativo(true)
                .build();

        when(tipoUsuarioController.atualizar(any(AtualizarTipoUsuarioInputDto.class))).thenReturn(output);

        String json = "{\"nome\": \"Dono de restaurante\"}";

        mockMvc.perform(put("/v1/tipos-usuario/" + id)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Dono de restaurante"));
    }

    @Test
    @DisplayName("GET /v1/tipos-usuario/{id} deve retornar 200 OK")
    void deveBuscarTipoUsuarioPorId() throws Exception {
        UUID id = UUID.randomUUID();
        TipoUsuarioOutputDto output = TipoUsuarioOutputDto.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.CLIENTE)
                .nome("Cliente")
                .ativo(true)
                .build();

        when(tipoUsuarioController.buscarPorId(id)).thenReturn(output);

        mockMvc.perform(get("/v1/tipos-usuario/" + id)
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Cliente"));
    }

    @Test
    @DisplayName("GET /v1/tipos-usuario deve retornar 200 OK com lista")
    void deveListarTiposUsuario() throws Exception {
        TipoUsuarioOutputDto output = TipoUsuarioOutputDto.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.CLIENTE)
                .nome("Cliente")
                .ativo(true)
                .build();

        when(tipoUsuarioController.listar()).thenReturn(List.of(output));

        mockMvc.perform(get("/v1/tipos-usuario")
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cliente"));
    }

    @Test
    @DisplayName("DELETE /v1/tipos-usuario/{id} deve retornar 204 No Content")
    void deveDesativarTipoUsuario() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/v1/tipos-usuario/" + id)
                        .header("usuario-logado-id", usuarioLogadoId))
                .andExpect(status().isNoContent());

        verify(tipoUsuarioController).desativar(id);
    }

    @Test
    @DisplayName("POST /v1/tipos-usuario/usuarios/{usuarioId}/associar deve retornar 200 OK")
    void deveAssociarTipoUsuario() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();
        UsuarioOutputDto output = UsuarioOutputDto.builder()
                .id(usuarioId)
                .nome("Usuario Teste")
                .tipoUsuarioId(tipoId)
                .tipoUsuarioNome("Cliente")
                .build();

        when(tipoUsuarioController.associar(any(AssociarTipoUsuarioInputDto.class))).thenReturn(output);

        String json = "{\"tipoUsuarioId\": \"" + tipoId + "\"}";

        mockMvc.perform(post("/v1/tipos-usuario/usuarios/" + usuarioId + "/associar")
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                .andExpect(jsonPath("$.tipoUsuarioId").value(tipoId.toString()));
    }

    @Test
    @DisplayName("POST sem header obrigatório deve retornar 400 Bad Request")
    void deveFalharSemHeaderObrigatorio() throws Exception {
        mockMvc.perform(post("/v1/tipos-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Cliente\"}"))
                .andExpect(status().isBadRequest());
    }
}
