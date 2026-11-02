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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.UsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.handler.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de API REST: UsuarioApi")
class UsuarioApiTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioController usuarioController;

    @InjectMocks
    private UsuarioApi usuarioApi;

    private final UUID usuarioLogadoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioApi)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/usuarios deve retornar 201 Created ao cadastrar usuário com sucesso")
    void deveCriarUsuarioComSucesso() throws Exception {
        UUID id = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();

        UsuarioOutputDto outputDto = UsuarioOutputDto.builder()
                .id(id)
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuarioId(tipoId)
                .tipoUsuarioNome("Cliente")
                .restauranteId(restauranteId)
                .ativo(true)
                .build();

        when(usuarioController.criar(any(CriarUsuarioInputDto.class))).thenReturn(outputDto);

        String jsonPayload = """
                {
                    "nome": "Carlos Silva",
                    "email": "carlos@email.com",
                    "tipoUsuarioId": "%s",
                    "restauranteId": "%s"
                }
                """.formatted(tipoId, restauranteId);

        mockMvc.perform(post("/v1/usuarios")
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Carlos Silva"))
                .andExpect(jsonPath("$.email").value("carlos@email.com"))
                .andExpect(jsonPath("$.restauranteId").value(restauranteId.toString()));

        verify(usuarioController).criar(any(CriarUsuarioInputDto.class));
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve retornar 200 OK ao atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() throws Exception {
        UUID id = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();

        UsuarioOutputDto outputDto = UsuarioOutputDto.builder()
                .id(id)
                .nome("Carlos Silva Atualizado")
                .email("carlos.atualizado@email.com")
                .tipoUsuarioId(tipoId)
                .tipoUsuarioNome("Dono de restaurante")
                .ativo(true)
                .build();

        when(usuarioController.atualizar(any(AtualizarUsuarioInputDto.class))).thenReturn(outputDto);

        String jsonPayload = """
                {
                    "nome": "Carlos Silva Atualizado",
                    "email": "carlos.atualizado@email.com",
                    "tipoUsuarioId": "%s"
                }
                """.formatted(tipoId);

        mockMvc.perform(put("/v1/usuarios/{id}", id)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Carlos Silva Atualizado"))
                .andExpect(jsonPath("$.email").value("carlos.atualizado@email.com"));

        verify(usuarioController).atualizar(any(AtualizarUsuarioInputDto.class));
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} deve retornar 200 OK com os dados do usuário")
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        UUID id = UUID.randomUUID();
        UsuarioOutputDto outputDto = UsuarioOutputDto.builder()
                .id(id)
                .nome("João Usuário")
                .email("joao@email.com")
                .tipoUsuarioId(UUID.randomUUID())
                .tipoUsuarioNome("Cliente")
                .ativo(true)
                .build();

        when(usuarioController.buscarPorId(id)).thenReturn(outputDto);

        mockMvc.perform(get("/v1/usuarios/{id}", id)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("João Usuário"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.tipoUsuarioNome").value("Cliente"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(usuarioController).buscarPorId(id);
    }

    @Test
    @DisplayName("GET /v1/usuarios/restaurante/{restauranteId} deve retornar 200 OK com lista de usuários")
    void deveListarUsuariosPorRestauranteComSucesso() throws Exception {
        UUID restauranteId = UUID.randomUUID();
        UsuarioOutputDto u1 = UsuarioOutputDto.builder()
                .id(UUID.randomUUID())
                .nome("Usuário 1")
                .email("u1@email.com")
                .ativo(true)
                .build();
        UsuarioOutputDto u2 = UsuarioOutputDto.builder()
                .id(UUID.randomUUID())
                .nome("Usuário 2")
                .email("u2@email.com")
                .ativo(true)
                .build();

        when(usuarioController.listarPorRestaurante(restauranteId, usuarioLogadoId)).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/v1/usuarios/restaurante/{restauranteId}", restauranteId)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Usuário 1"))
                .andExpect(jsonPath("$[1].nome").value("Usuário 2"));

        verify(usuarioController).listarPorRestaurante(restauranteId, usuarioLogadoId);
    }

    @Test
    @DisplayName("DELETE /v1/usuarios/{id} deve retornar 204 No Content ao desativar usuário")
    void deveDesativarUsuarioComSucesso() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/v1/usuarios/{id}", id)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioController).desativar(id, usuarioLogadoId);
    }

    @Test
    @DisplayName("POST /v1/usuarios/{usuarioId}/tipo-usuario deve retornar 200 OK com usuário atualizado")
    void deveAssociarTipoUsuarioComSucesso() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        UUID tipoUsuarioId = UUID.randomUUID();
        UsuarioOutputDto outputDto = UsuarioOutputDto.builder()
                .id(usuarioId)
                .nome("Maria")
                .email("maria@email.com")
                .tipoUsuarioId(tipoUsuarioId)
                .tipoUsuarioNome("Dono de restaurante")
                .ativo(true)
                .build();

        when(usuarioController.associarTipoUsuario(any(AssociarTipoUsuarioInputDto.class))).thenReturn(outputDto);

        String jsonPayload = """
                {
                    "tipoUsuarioId": "%s"
                }
                """.formatted(tipoUsuarioId);

        mockMvc.perform(post("/v1/usuarios/{usuarioId}/tipo-usuario", usuarioId)
                        .header("usuario-logado-id", usuarioLogadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                .andExpect(jsonPath("$.tipoUsuarioId").value(tipoUsuarioId.toString()))
                .andExpect(jsonPath("$.tipoUsuarioNome").value("Dono de restaurante"));

        verify(usuarioController).associarTipoUsuario(any(AssociarTipoUsuarioInputDto.class));
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} sem o header usuario-logado-id deve retornar 400 Bad Request")
    void deveRetornar400QuandoHeaderObrigatorioAusente() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
