package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;

@DisplayName("Testes de Domínio: ItemCardapio")
class ItemCardapioTest {

    private ItemCardapio itemValido;

    @BeforeEach
    void setUp() {
        itemValido = ItemCardapio.builder()
                .id(UUID.randomUUID())
                .nome("Hambúrguer Artesanal")
                .descricao("Pão brioche, blend 180g, queijo cheddar e bacon")
                .preco(new BigDecimal("35.50"))
                .disponivelApenasRestaurante(false)
                .fotoPath("/imagens/itens/burger.jpg")
                .ativo(true)
                .dataCriacao(LocalDateTime.now().minusDays(1))
                .build();
    }

    @Nested
    @DisplayName("Validação para criação")
    class ValidarParaCriacaoTests {

        @Test
        @DisplayName("Deve validar com sucesso quando todos os campos obrigatórios estão corretos")
        void deveValidarComSucesso() {
            assertDoesNotThrow(() -> itemValido.validarParaCriacao());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve lançar exceção quando o nome for nulo ou em branco")
        void deveLancarExcecaoQuandoNomeInvalido(String nomeInvalido) {
            ItemCardapio item = ItemCardapio.builder()
                    .nome(nomeInvalido)
                    .descricao("Descrição válida")
                    .preco(new BigDecimal("20.00"))
                    .disponivelApenasRestaurante(true)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    item::validarParaCriacao
            );
            assertEquals("Nome do item é obrigatório.", ex.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve lançar exceção quando a descrição for nula ou em branco")
        void deveLancarExcecaoQuandoDescricaoInvalida(String descInvalida) {
            ItemCardapio item = ItemCardapio.builder()
                    .nome("Nome Válido")
                    .descricao(descInvalida)
                    .preco(new BigDecimal("20.00"))
                    .disponivelApenasRestaurante(true)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    item::validarParaCriacao
            );
            assertEquals("Descrição do item é obrigatória.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o preço for nulo")
        void deveLancarExcecaoQuandoPrecoNulo() {
            ItemCardapio item = ItemCardapio.builder()
                    .nome("Nome Válido")
                    .descricao("Descrição válida")
                    .preco(null)
                    .disponivelApenasRestaurante(true)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    item::validarParaCriacao
            );
            assertEquals("Preço deve ser maior que zero.", ex.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "0.00", "-0.01", "-10.00"})
        @DisplayName("Deve lançar exceção quando o preço for zero ou negativo")
        void deveLancarExcecaoQuandoPrecoZeroOuNegativo(String precoInvalido) {
            ItemCardapio item = ItemCardapio.builder()
                    .nome("Nome Válido")
                    .descricao("Descrição válida")
                    .preco(new BigDecimal(precoInvalido))
                    .disponivelApenasRestaurante(true)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    item::validarParaCriacao
            );
            assertEquals("Preço deve ser maior que zero.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando disponibilidade apenas restaurante for nula")
        void deveLancarExcecaoQuandoDisponibilidadeNula() {
            ItemCardapio item = ItemCardapio.builder()
                    .nome("Nome Válido")
                    .descricao("Descrição válida")
                    .preco(new BigDecimal("20.00"))
                    .disponivelApenasRestaurante(null)
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    item::validarParaCriacao
            );
            assertEquals("Informar se a disponibilidade é apenas no restaurante é obrigatória.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Atualização de dados")
    class AtualizacaoDadosTests {

        @Test
        @DisplayName("Deve atualizar dados com sucesso e atualizar timestamp")
        void deveAtualizarDadosComSucesso() {
            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

            itemValido.atualizarDados(
                    "Novo Hambúrguer",
                    "Nova descrição do prato",
                    new BigDecimal("42.00"),
                    true,
                    "/imagens/novo-burger.png"
            );

            assertEquals("Novo Hambúrguer", itemValido.getNome());
            assertEquals("Nova descrição do prato", itemValido.getDescricao());
            assertEquals(new BigDecimal("42.00"), itemValido.getPreco());
            assertTrue(itemValido.getDisponivelApenasRestaurante());
            assertEquals("/imagens/novo-burger.png", itemValido.getFotoPath());
            assertNotNull(itemValido.getDataAlteracao());
            assertTrue(itemValido.getDataAlteracao().isAfter(antes));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com dados inválidos")
        void deveLancarExcecaoAoAtualizarComDadosInvalidos() {
            assertThrows(
                    RegraNegocioException.class,
                    () -> itemValido.atualizarDados(
                            "",
                            "Nova descrição",
                            new BigDecimal("42.00"),
                            true,
                            null
                    )
            );
        }
    }

    @Nested
    @DisplayName("Desativação e validação de ativo")
    class AtivoTests {

        @Test
        @DisplayName("Deve desativar o item com sucesso")
        void deveDesativar() {
            assertTrue(itemValido.isAtivo());

            itemValido.desativar();

            assertFalse(itemValido.isAtivo());
            assertEquals(false, itemValido.getAtivo());
            assertNotNull(itemValido.getDataAlteracao());
        }

        @Test
        @DisplayName("isAtivo deve retornar false quando ativo for nulo")
        void isAtivoDeveRetornarFalseQuandoNulo() {
            ItemCardapio item = ItemCardapio.builder().ativo(null).build();
            assertFalse(item.isAtivo());
        }

        @Test
        @DisplayName("validarAtivo deve passar sem exceção quando ativo for true")
        void validarAtivoDevePassarQuandoAtivo() {
            assertDoesNotThrow(() -> itemValido.validarAtivo());
        }

        @Test
        @DisplayName("validarAtivo deve lançar exceção quando ativo for false")
        void validarAtivoDeveLancarExcecaoQuandoInativo() {
            itemValido.desativar();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> itemValido.validarAtivo()
            );
            assertEquals("Item de cardápio inativo.", ex.getMessage());
        }

        @Test
        @DisplayName("validarAtivo deve lançar exceção quando ativo for nulo")
        void validarAtivoDeveLancarExcecaoQuandoAtivoNulo() {
            ItemCardapio item = ItemCardapio.builder().ativo(null).build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    item::validarAtivo
            );
            assertEquals("Item de cardápio inativo.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Construtores e Getters")
    class ConstrutoresTests {

        @Test
        @DisplayName("Deve instanciar via construtor padrão vazio")
        void deveInstanciarConstrutorVazio() {
            ItemCardapio item = new ItemCardapio();
            assertNotNull(item);
            assertNull(item.getId());
            assertNull(item.getNome());
            assertNull(item.getPreco());
        }
    }
}
