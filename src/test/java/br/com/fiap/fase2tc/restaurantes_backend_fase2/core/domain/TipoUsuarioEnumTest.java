package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;

@DisplayName("Testes de Domínio: TipoUsuarioEnum")
class TipoUsuarioEnumTest {

    @Nested
    @DisplayName("Testes de conversão fromDescricao")
    class FromDescricaoTests {

        @Test
        @DisplayName("Deve converter com sucesso a partir da descrição exata")
        void deveConverterPorDescricao() {
            assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, TipoUsuarioEnum.fromDescricao("Dono de restaurante"));
            assertEquals(TipoUsuarioEnum.CLIENTE, TipoUsuarioEnum.fromDescricao("Cliente"));
        }

        @Test
        @DisplayName("Deve converter ignorando maiúsculas, minúsculas e espaços em branco")
        void deveConverterIgnorandoCaseEEspacos() {
            assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, TipoUsuarioEnum.fromDescricao("  dono de restaurante  "));
            assertEquals(TipoUsuarioEnum.CLIENTE, TipoUsuarioEnum.fromDescricao("  cLiente  "));
        }

        @Test
        @DisplayName("Deve converter com sucesso a partir do nome do enum")
        void deveConverterPorNomeEnum() {
            assertEquals(TipoUsuarioEnum.DONO_RESTAURANTE, TipoUsuarioEnum.fromDescricao("DONO_RESTAURANTE"));
            assertEquals(TipoUsuarioEnum.CLIENTE, TipoUsuarioEnum.fromDescricao("CLIENTE"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { "   ", "\t", "\n" })
        @DisplayName("Deve lançar RegraNegocioException quando a descrição for nula ou em branco")
        void deveLancarExcecaoQuandoDescricaoNulaOuVazia(String descricaoInvalida) {
            RegraNegocioException exception = assertThrows(
                    RegraNegocioException.class,
                    () -> TipoUsuarioEnum.fromDescricao(descricaoInvalida));
            assertEquals("Descrição do tipo de usuário é obrigatória.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando a descrição não corresponder a nenhum tipo")
        void deveLancarExcecaoQuandoTipoNaoEncontrado() {
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> TipoUsuarioEnum.fromDescricao("ADMINISTRADOR_INEXISTENTE"));
            assertTrue(exception.getMessage().contains("Tipo de usuário inválido"));
        }
    }

    @Nested
    @DisplayName("Testes dos métodos de verificação de papel")
    class PapelTests {

        @Test
        @DisplayName("Deve verificar corretamente se é Dono de Restaurante")
        void deveVerificarSeEDonoRestaurante() {
            assertTrue(TipoUsuarioEnum.DONO_RESTAURANTE.isDonoRestaurante());
            assertFalse(TipoUsuarioEnum.CLIENTE.isDonoRestaurante());
        }

        @Test
        @DisplayName("Deve verificar corretamente se é Cliente")
        void deveVerificarSeECliente() {
            assertTrue(TipoUsuarioEnum.CLIENTE.isCliente());
            assertFalse(TipoUsuarioEnum.DONO_RESTAURANTE.isCliente());
        }

        @Test
        @DisplayName("Deve retornar a descrição correta de cada enum")
        void deveRetornarDescricao() {
            assertEquals("Dono de restaurante", TipoUsuarioEnum.DONO_RESTAURANTE.getDescricao());
            assertEquals("Cliente", TipoUsuarioEnum.CLIENTE.getDescricao());
        }
    }
}
