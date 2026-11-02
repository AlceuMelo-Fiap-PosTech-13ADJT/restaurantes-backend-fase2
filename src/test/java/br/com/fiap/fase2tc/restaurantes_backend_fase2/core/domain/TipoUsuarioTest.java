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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;

@DisplayName("Testes de Domínio: TipoUsuario")
class TipoUsuarioTest {

    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuario = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .dataCriacao(LocalDateTime.now().minusDays(1))
                .build();
    }

    @Nested
    @DisplayName("Validação para criação")
    class ValidarParaCriacaoTests {

        @Test
        @DisplayName("Deve validar com sucesso quando tipo estiver preenchido")
        void deveValidarComSucessoQuandoTipoValido() {
            assertDoesNotThrow(() -> tipoUsuario.validarParaCriacao());
        }

        @Test
        @DisplayName("Deve lançar RegraNegocioException quando tipo for nulo")
        void deveLancarExcecaoQuandoTipoNulo() {
            tipoUsuario = TipoUsuario.builder()
                    .tipo(null)
                    .ativo(true)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> tipoUsuario.validarParaCriacao()
            );
            assertEquals("Tipo de usuário é obrigatório.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Atualização de tipo")
    class AtualizarTipoTests {

        @Test
        @DisplayName("Deve atualizar o tipo e a data de alteração com sucesso")
        void deveAtualizarTipoComSucesso() {
            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

            tipoUsuario.atualizarTipo(TipoUsuarioEnum.CLIENTE);

            assertEquals(TipoUsuarioEnum.CLIENTE, tipoUsuario.getTipo());
            assertNotNull(tipoUsuario.getDataAlteracao());
            assertTrue(tipoUsuario.getDataAlteracao().isAfter(antes));
        }

        @Test
        @DisplayName("Deve lançar RegraNegocioException ao tentar atualizar com tipo nulo")
        void deveLancarExcecaoAoAtualizarComTipoNulo() {
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> tipoUsuario.atualizarTipo(null)
            );
            assertEquals("Tipo de usuário é obrigatório.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Desativação e validação de ativo")
    class AtivoTests {

        @Test
        @DisplayName("Deve desativar o tipo de usuário com sucesso")
        void deveDesativar() {
            assertTrue(tipoUsuario.isAtivo());

            tipoUsuario.desativar();

            assertFalse(tipoUsuario.isAtivo());
            assertEquals(false, tipoUsuario.getAtivo());
            assertNotNull(tipoUsuario.getDataAlteracao());
        }

        @Test
        @DisplayName("Deve retornar false em isAtivo quando ativo for nulo")
        void deveRetornarFalseQuandoAtivoForNulo() {
            tipoUsuario = TipoUsuario.builder().ativo(null).build();
            assertFalse(tipoUsuario.isAtivo());
        }

        @Test
        @DisplayName("validarAtivo deve passar quando usuário estiver ativo")
        void validarAtivoDevePassarQuandoAtivo() {
            assertDoesNotThrow(() -> tipoUsuario.validarAtivo());
        }

        @Test
        @DisplayName("validarAtivo deve lançar exceção quando ativo for false")
        void validarAtivoDeveLancarExcecaoQuandoInativo() {
            tipoUsuario.desativar();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> tipoUsuario.validarAtivo()
            );
            assertEquals("Tipo de usuário inativo.", ex.getMessage());
        }

        @Test
        @DisplayName("validarAtivo deve lançar exceção quando ativo for nulo")
        void validarAtivoDeveLancarExcecaoQuandoAtivoNulo() {
            tipoUsuario = TipoUsuario.builder().ativo(null).build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> tipoUsuario.validarAtivo()
            );
            assertEquals("Tipo de usuário inativo.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Verificações de papel e nome")
    class PapelENomeTests {

        @Test
        @DisplayName("Deve identificar corretamente dono de restaurante")
        void deveIdentificarDonoRestaurante() {
            tipoUsuario = TipoUsuario.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).build();

            assertTrue(tipoUsuario.isDonoRestaurante());
            assertFalse(tipoUsuario.isCliente());
            assertEquals("Dono de restaurante", tipoUsuario.getNome());
        }

        @Test
        @DisplayName("Deve identificar corretamente cliente")
        void deveIdentificarCliente() {
            tipoUsuario = TipoUsuario.builder().tipo(TipoUsuarioEnum.CLIENTE).build();

            assertTrue(tipoUsuario.isCliente());
            assertFalse(tipoUsuario.isDonoRestaurante());
            assertEquals("Cliente", tipoUsuario.getNome());
        }

        @Test
        @DisplayName("Deve tratar tipo nulo com segurança")
        void deveTratarTipoNuloComSeguranca() {
            tipoUsuario = TipoUsuario.builder().tipo(null).build();

            assertFalse(tipoUsuario.isDonoRestaurante());
            assertFalse(tipoUsuario.isCliente());
            assertNull(tipoUsuario.getNome());
        }
    }

    @Nested
    @DisplayName("Construtores e Getters")
    class ConstrutoresTests {

        @Test
        @DisplayName("Deve instanciar via construtor padrão vazio")
        void deveInstanciarConstrutorVazio() {
            TipoUsuario instancia = new TipoUsuario();
            assertNotNull(instancia);
            assertNull(instancia.getId());
            assertNull(instancia.getTipo());
        }
    }
}
