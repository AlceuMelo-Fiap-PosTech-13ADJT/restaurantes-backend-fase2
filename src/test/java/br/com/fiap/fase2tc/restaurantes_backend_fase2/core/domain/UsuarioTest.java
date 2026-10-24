package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.AcessoNaoAutorizadoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;

@DisplayName("Testes de Domínio: Usuario")
class UsuarioTest {

    private Usuario usuario;
    private TipoUsuario tipoDonoAtivo;
    private TipoUsuario tipoClienteAtivo;

    @BeforeEach
    void setUp() {
        tipoDonoAtivo = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .build();

        tipoClienteAtivo = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("João Silva")
                .email("joao.silva@email.com")
                .tipoUsuario(tipoDonoAtivo)
                .ativo(true)
                .dataCriacao(LocalDateTime.now().minusDays(2))
                .build();
    }

    @Nested
    @DisplayName("Papéis de usuário")
    class PapelUsuarioTests {

        @Test
        @DisplayName("Deve identificar usuário como Dono de Restaurante")
        void deveIdentificarDonoRestaurante() {
            assertTrue(usuario.isDonoRestaurante());
            assertFalse(usuario.isCliente());
        }

        @Test
        @DisplayName("Deve identificar usuário como Cliente")
        void deveIdentificarCliente() {
            usuario = Usuario.builder()
                    .tipoUsuario(tipoClienteAtivo)
                    .ativo(true)
                    .build();

            assertTrue(usuario.isCliente());
            assertFalse(usuario.isDonoRestaurante());
        }

        @Test
        @DisplayName("Deve retornar false para papéis quando tipoUsuario for nulo")
        void deveRetornarFalseQuandoTipoUsuarioNulo() {
            usuario = Usuario.builder()
                    .tipoUsuario(null)
                    .ativo(true)
                    .build();

            assertFalse(usuario.isDonoRestaurante());
            assertFalse(usuario.isCliente());
        }
    }

    @Nested
    @DisplayName("Validação de status ativo")
    class ValidarAtivoTests {

        @Test
        @DisplayName("Deve validar com sucesso quando usuário estiver ativo")
        void deveValidarComSucessoQuandoAtivo() {
            assertTrue(usuario.isAtivo());
            assertDoesNotThrow(() -> usuario.validarAtivo());
        }

        @Test
        @DisplayName("Deve lançar AcessoNaoAutorizadoException quando usuário estiver inativo")
        void deveLancarExcecaoQuandoInativo() {
            usuario = Usuario.builder()
                    .ativo(false)
                    .build();

            assertFalse(usuario.isAtivo());
            AcessoNaoAutorizadoException ex = assertThrows(
                    AcessoNaoAutorizadoException.class,
                    () -> usuario.validarAtivo()
            );
            assertEquals("Usuário inativo ou não autorizado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar AcessoNaoAutorizadoException quando ativo for nulo")
        void deveLancarExcecaoQuandoAtivoNulo() {
            usuario = Usuario.builder()
                    .ativo(null)
                    .build();

            assertFalse(usuario.isAtivo());
            AcessoNaoAutorizadoException ex = assertThrows(
                    AcessoNaoAutorizadoException.class,
                    () -> usuario.validarAtivo()
            );
            assertEquals("Usuário inativo ou não autorizado.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Associação de Tipo de Usuário")
    class AssociarTipoUsuarioTests {

        @Test
        @DisplayName("Deve associar novo tipo de usuário quando o usuário ainda não possuir nenhum")
        void deveAssociarTipoQuandoUsuarioSemTipoAnterior() {
            usuario = Usuario.builder()
                    .id(UUID.randomUUID())
                    .tipoUsuario(null)
                    .ativo(true)
                    .build();

            usuario.associarTipoUsuario(tipoClienteAtivo);

            assertEquals(tipoClienteAtivo, usuario.getTipoUsuario());
            assertNotNull(usuario.getDataAlteracao());
        }

        @Test
        @DisplayName("Deve atualizar tipo de usuário quando o ID do novo tipo for diferente do atual")
        void deveAtualizarTipoQuandoDiferente() {
            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

            usuario.associarTipoUsuario(tipoClienteAtivo);

            assertEquals(tipoClienteAtivo, usuario.getTipoUsuario());
            assertNotNull(usuario.getDataAlteracao());
            assertTrue(usuario.getDataAlteracao().isAfter(antes));
        }

        @Test
        @DisplayName("Deve lançar RegraNegocioException ao tentar associar o mesmo tipo já cadastrado")
        void deveLancarExcecaoQuandoAssociarMesmoTipo() {
            TipoUsuario mesmoTipo = TipoUsuario.builder()
                    .id(tipoDonoAtivo.getId())
                    .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                    .ativo(true)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> usuario.associarTipoUsuario(mesmoTipo)
            );
            assertEquals("Tipo de usuário é o mesmo já cadastrado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar RegraNegocioException quando o novo tipo de usuário for nulo")
        void deveLancarExcecaoQuandoNovoTipoNulo() {
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> usuario.associarTipoUsuario(null)
            );
            assertEquals("Tipo de usuário válido é obrigatório.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar RegraNegocioException quando o novo tipo não tiver TipoUsuarioEnum definido")
        void deveLancarExcecaoQuandoEnumTipoNulo() {
            TipoUsuario tipoSemEnum = TipoUsuario.builder()
                    .id(UUID.randomUUID())
                    .tipo(null)
                    .ativo(true)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> usuario.associarTipoUsuario(tipoSemEnum)
            );
            assertEquals("Tipo de usuário válido é obrigatório.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar RegraNegocioException quando o novo tipo tiver ativo nulo")
        void deveLancarExcecaoQuandoAtivoNuloNoTipo() {
            TipoUsuario tipoAtivoNulo = TipoUsuario.builder()
                    .id(UUID.randomUUID())
                    .tipo(TipoUsuarioEnum.CLIENTE)
                    .ativo(null)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> usuario.associarTipoUsuario(tipoAtivoNulo)
            );
            assertEquals("Tipo de usuário válido é obrigatório.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar RegraNegocioException quando o novo tipo de usuário estiver inativo")
        void deveLancarExcecaoQuandoTipoInativo() {
            TipoUsuario tipoInativo = TipoUsuario.builder()
                    .id(UUID.randomUUID())
                    .tipo(TipoUsuarioEnum.CLIENTE)
                    .ativo(false)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> usuario.associarTipoUsuario(tipoInativo)
            );
            assertEquals("Tipo de usuário válido é obrigatório.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Construtores e Getters")
    class ConstrutoresTests {

        @Test
        @DisplayName("Deve instanciar via construtor padrão vazio")
        void deveInstanciarConstrutorVazio() {
            Usuario instancia = new Usuario();
            assertNotNull(instancia);
            assertNull(instancia.getId());
            assertNull(instancia.getNome());
            assertNull(instancia.getEmail());
        }
    }
}
