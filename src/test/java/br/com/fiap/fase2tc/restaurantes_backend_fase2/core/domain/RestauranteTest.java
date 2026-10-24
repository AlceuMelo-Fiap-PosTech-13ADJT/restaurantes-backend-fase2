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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;

@DisplayName("Testes de Domínio: Restaurante")
class RestauranteTest {

    private Restaurante restaurante;
    private Usuario usuarioDono;
    private Usuario usuarioCliente;
    private ItemCardapio itemCardapioPadrao;

    @BeforeEach
    void setUp() {
        TipoUsuario tipoDono = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .build();

        TipoUsuario tipoCliente = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        usuarioDono = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Carlos Dono")
                .email("carlos@restaurante.com")
                .tipoUsuario(tipoDono)
                .ativo(true)
                .build();

        usuarioCliente = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Maria Cliente")
                .email("maria@cliente.com")
                .tipoUsuario(tipoCliente)
                .ativo(true)
                .build();

        itemCardapioPadrao = ItemCardapio.builder()
                .id(UUID.randomUUID())
                .nome("Pizza Margherita")
                .descricao("Molho de tomate, mussarela e manjericão fresco")
                .preco(new BigDecimal("45.00"))
                .disponivelApenasRestaurante(false)
                .ativo(true)
                .build();

        restaurante = Restaurante.builder()
                .id(UUID.randomUUID())
                .nome("Cantina Bella Italia")
                .endereco("Rua das Flores, 123 - Centro")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("18:00 às 23:00")
                .itensCardapio(new ArrayList<>(List.of(itemCardapioPadrao)))
                .usuarios(new ArrayList<>(List.of(usuarioDono)))
                .ativo(true)
                .dataCriacao(LocalDateTime.now().minusDays(5))
                .build();
    }

    @Nested
    @DisplayName("Validação para criação")
    class ValidarParaCriacaoTests {

        @Test
        @DisplayName("Deve validar com sucesso quando todos os dados estão corretos")
        void deveValidarComSucesso() {
            assertDoesNotThrow(() -> restaurante.validarParaCriacao());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve lançar exceção quando o nome for nulo ou em branco")
        void deveLancarExcecaoQuandoNomeInvalido(String nomeInvalido) {
            restaurante.setNome(nomeInvalido);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Nome do restaurante é obrigatório.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista de itens de cardápio for nula")
        void deveLancarExcecaoQuandoItensCardapioNulo() {
            restaurante.setItensCardapio(null);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Restaurante deve possuir pelo menos um item de cardápio associado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista de itens de cardápio for vazia")
        void deveLancarExcecaoQuandoItensCardapioVazia() {
            restaurante.setItensCardapio(new ArrayList<>());

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Restaurante deve possuir pelo menos um item de cardápio associado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista de usuários for nula")
        void deveLancarExcecaoQuandoUsuariosNulo() {
            restaurante.setUsuarios(null);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Restaurante deve possuir pelo menos um usuário associado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista de usuários for vazia")
        void deveLancarExcecaoQuandoUsuariosVazia() {
            restaurante.setUsuarios(new ArrayList<>());

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Restaurante deve possuir pelo menos um usuário associado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não houver nenhum usuário do tipo DONO_RESTAURANTE")
        void deveLancarExcecaoQuandoSemDonoAssociado() {
            restaurante.setUsuarios(new ArrayList<>(List.of(usuarioCliente)));

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals(
                    "Restaurante deve possuir pelo menos um usuário do tipo DONO_RESTAURANTE associado.",
                    ex.getMessage()
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve lançar exceção quando o endereço for nulo ou em branco")
        void deveLancarExcecaoQuandoEnderecoInvalido(String enderecoInvalido) {
            restaurante.setEndereco(enderecoInvalido);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Endereço do restaurante é obrigatório.", ex.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve lançar exceção quando o tipo de cozinha for nulo ou em branco")
        void deveLancarExcecaoQuandoTipoCozinhaInvalido(String tipoCozinhaInvalido) {
            restaurante.setTipoCozinha(tipoCozinhaInvalido);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Tipo de cozinha é obrigatório.", ex.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve lançar exceção quando o horário de funcionamento for nulo ou em branco")
        void deveLancarExcecaoQuandoHorarioInvalido(String horarioInvalido) {
            restaurante.setHorarioFuncionamento(horarioInvalido);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaCriacao()
            );
            assertEquals("Horário de funcionamento é obrigatório.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Validação para atualização")
    class ValidarParaAtualizacaoTests {

        @Test
        @DisplayName("Deve validar com sucesso quando todos os campos para atualização estão preenchidos")
        void deveValidarAtualizacaoComSucesso() {
            assertDoesNotThrow(() -> restaurante.validarParaAtualizacao());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("Deve lançar exceção ao atualizar com nome nulo ou em branco")
        void deveLancarExcecaoAtualizacaoNomeInvalido(String nome) {
            restaurante.setNome(nome);
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaAtualizacao()
            );
            assertEquals("Nome do restaurante é obrigatório.", ex.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("Deve lançar exceção ao atualizar com endereço nulo ou em branco")
        void deveLancarExcecaoAtualizacaoEnderecoInvalido(String endereco) {
            restaurante.setEndereco(endereco);
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaAtualizacao()
            );
            assertEquals("Endereço do restaurante é obrigatório.", ex.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("Deve lançar exceção ao atualizar com tipo de cozinha nulo ou em branco")
        void deveLancarExcecaoAtualizacaoTipoCozinhaInvalido(String tipo) {
            restaurante.setTipoCozinha(tipo);
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaAtualizacao()
            );
            assertEquals("Tipo de cozinha é obrigatório.", ex.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("Deve lançar exceção ao atualizar com horário nulo ou em branco")
        void deveLancarExcecaoAtualizacaoHorarioInvalido(String horario) {
            restaurante.setHorarioFuncionamento(horario);
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarParaAtualizacao()
            );
            assertEquals("Horário de funcionamento é obrigatório.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Atualização de dados")
    class AtualizacaoDadosTests {

        @Test
        @DisplayName("Deve atualizar os dados e a data de alteração com sucesso")
        void deveAtualizarDados() {
            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

            restaurante.atualizarDados(
                    "Novo Nome",
                    "Novo Endereço, 456",
                    "Japonesa",
                    "11:00 às 22:00"
            );

            assertEquals("Novo Nome", restaurante.getNome());
            assertEquals("Novo Endereço, 456", restaurante.getEndereco());
            assertEquals("Japonesa", restaurante.getTipoCozinha());
            assertEquals("11:00 às 22:00", restaurante.getHorarioFuncionamento());
            assertNotNull(restaurante.getDataAlteracao());
            assertTrue(restaurante.getDataAlteracao().isAfter(antes));
        }

        @Test
        @DisplayName("Deve lançar exceção se atualizar dados com campos inválidos")
        void deveLancarExcecaoAtualizarDadosInvalidos() {
            assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.atualizarDados("", "Endereço", "Cozinha", "Horário")
            );
        }
    }

    @Nested
    @DisplayName("Gerenciamento de Itens de Cardápio")
    class ItensCardapioTests {

        @Test
        @DisplayName("Deve adicionar novo item de cardápio com sucesso")
        void deveAdicionarItemCardapioComSucesso() {
            ItemCardapio novoItem = ItemCardapio.builder()
                    .id(UUID.randomUUID())
                    .nome("Lasanha Bolonhesa")
                    .preco(new BigDecimal("50.00"))
                    .build();

            restaurante.adicionaItemCardapio(novoItem);

            assertEquals(2, restaurante.getItensCardapio().size());
            assertTrue(restaurante.getItensCardapio().contains(novoItem));
            assertNotNull(restaurante.getDataAlteracao());
        }

        @Test
        @DisplayName("Deve inicializar lista e adicionar item quando lista for nula")
        void deveInicializarListaQuandoNulaAoAdicionarItem() {
            restaurante.setItensCardapio(null);

            ItemCardapio novoItem = ItemCardapio.builder()
                    .id(UUID.randomUUID())
                    .nome("Risoto")
                    .build();

            restaurante.adicionaItemCardapio(novoItem);

            assertNotNull(restaurante.getItensCardapio());
            assertEquals(1, restaurante.getItensCardapio().size());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar adicionar item de cardápio nulo")
        void deveLancarExcecaoAoAdicionarItemNulo() {
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.adicionaItemCardapio(null)
            );
            assertEquals("Item de cardápio é obrigatório.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar adicionar item de cardápio já existente")
        void deveLancarExcecaoAoAdicionarItemDuplicado() {
            ItemCardapio itemDuplicado = ItemCardapio.builder()
                    .id(itemCardapioPadrao.getId())
                    .nome("Mesma pizza")
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.adicionaItemCardapio(itemDuplicado)
            );
            assertEquals("Item de cardápio já existe.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve remover item de cardápio com sucesso")
        void deveRemoverItemCardapioComSucesso() {
            UUID idParaRemover = itemCardapioPadrao.getId();

            restaurante.removeItemCardapio(idParaRemover);

            assertEquals(0, restaurante.getItensCardapio().size());
            assertNotNull(restaurante.getDataAlteracao());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar remover item com id nulo")
        void deveLancarExcecaoAoRemoverItemComIdNulo() {
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.removeItemCardapio(null)
            );
            assertEquals("Item de cardápio não encontrado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar remover item inexistente")
        void deveLancarExcecaoAoRemoverItemInexistente() {
            UUID idInexistente = UUID.randomUUID();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.removeItemCardapio(idInexistente)
            );
            assertEquals("Item de cardápio não encontrado.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Gerenciamento de Usuários")
    class UsuariosTests {

        @Test
        @DisplayName("Deve adicionar novo usuário com sucesso")
        void deveAdicionarUsuarioComSucesso() {
            restaurante.adicionaUsuario(usuarioCliente);

            assertEquals(2, restaurante.getUsuarios().size());
            assertTrue(restaurante.getUsuarios().contains(usuarioCliente));
            assertNotNull(restaurante.getDataAlteracao());
        }

        @Test
        @DisplayName("Deve inicializar lista e adicionar usuário quando lista for nula")
        void deveInicializarListaQuandoNulaAoAdicionarUsuario() {
            restaurante.setUsuarios(null);

            restaurante.adicionaUsuario(usuarioDono);

            assertNotNull(restaurante.getUsuarios());
            assertEquals(1, restaurante.getUsuarios().size());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar adicionar usuário nulo")
        void deveLancarExcecaoAoAdicionarUsuarioNulo() {
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.adicionaUsuario(null)
            );
            assertEquals("Usuário é obrigatório.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar adicionar usuário já existente")
        void deveLancarExcecaoAoAdicionarUsuarioDuplicado() {
            Usuario usuarioDuplicado = Usuario.builder()
                    .id(usuarioDono.getId())
                    .nome("Carlos Clone")
                    .build();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.adicionaUsuario(usuarioDuplicado)
            );
            assertEquals("Usuário já existe.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve remover usuário com sucesso")
        void deveRemoverUsuarioComSucesso() {
            UUID idParaRemover = usuarioDono.getId();

            restaurante.removeUsuario(idParaRemover);

            assertEquals(0, restaurante.getUsuarios().size());
            assertNotNull(restaurante.getDataAlteracao());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar remover usuário com id nulo")
        void deveLancarExcecaoAoRemoverUsuarioIdNulo() {
            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.removeUsuario(null)
            );
            assertEquals("Usuario não encontrado.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar remover usuário não encontrado")
        void deveLancarExcecaoAoRemoverUsuarioInexistente() {
            UUID idInexistente = UUID.randomUUID();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.removeUsuario(idInexistente)
            );
            assertEquals("Usuario não encontrado.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Desativação e validação de ativo")
    class AtivoTests {

        @Test
        @DisplayName("Deve desativar restaurante com sucesso")
        void deveDesativar() {
            assertTrue(restaurante.isAtivo());

            restaurante.desativar();

            assertFalse(restaurante.isAtivo());
            assertEquals(false, restaurante.getAtivo());
            assertNotNull(restaurante.getDataAlteracao());
        }

        @Test
        @DisplayName("isAtivo deve retornar false quando ativo for nulo")
        void isAtivoDeveRetornarFalseQuandoNulo() {
            restaurante.setAtivo(null);
            assertFalse(restaurante.isAtivo());
        }

        @Test
        @DisplayName("validarAtivo deve passar sem exceção quando ativo for true")
        void validarAtivoDevePassarQuandoAtivo() {
            assertDoesNotThrow(() -> restaurante.validarAtivo());
        }

        @Test
        @DisplayName("validarAtivo deve lançar exceção quando ativo for false")
        void validarAtivoDeveLancarExcecaoQuandoInativo() {
            restaurante.desativar();

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarAtivo()
            );
            assertEquals("Restaurante inativo.", ex.getMessage());
        }

        @Test
        @DisplayName("validarAtivo deve lançar exceção quando ativo for nulo")
        void validarAtivoDeveLancarExcecaoQuandoNulo() {
            restaurante.setAtivo(null);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> restaurante.validarAtivo()
            );
            assertEquals("Restaurante inativo.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Validação de Dono (validarDono)")
    class ValidarDonoTests {

        @Test
        @DisplayName("Deve validar com sucesso quando operadorId for o dono associado")
        void deveValidarComSucessoQuandoOperadorForDono() {
            assertDoesNotThrow(() -> restaurante.validarDono(usuarioDono.getId()));
        }

        @Test
        @DisplayName("Deve lançar DonoRestauranteInvalidoException quando operadorId for nulo")
        void deveLancarExcecaoQuandoOperadorIdNulo() {
            DonoRestauranteInvalidoException ex = assertThrows(
                    DonoRestauranteInvalidoException.class,
                    () -> restaurante.validarDono(null)
            );
            assertEquals("Operador não é o dono deste restaurante.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar DonoRestauranteInvalidoException quando lista de usuários for nula")
        void deveLancarExcecaoQuandoUsuariosNulo() {
            restaurante.setUsuarios(null);

            DonoRestauranteInvalidoException ex = assertThrows(
                    DonoRestauranteInvalidoException.class,
                    () -> restaurante.validarDono(usuarioDono.getId())
            );
            assertEquals("Operador não é o dono deste restaurante.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar DonoRestauranteInvalidoException quando lista de usuários for vazia")
        void deveLancarExcecaoQuandoUsuariosVazia() {
            restaurante.setUsuarios(new ArrayList<>());

            DonoRestauranteInvalidoException ex = assertThrows(
                    DonoRestauranteInvalidoException.class,
                    () -> restaurante.validarDono(usuarioDono.getId())
            );
            assertEquals("Operador não é o dono deste restaurante.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar DonoRestauranteInvalidoException quando operador não pertencer ao restaurante")
        void deveLancarExcecaoQuandoOperadorNaoPertencerAoRestaurante() {
            UUID operadorNaoAssociado = UUID.randomUUID();

            DonoRestauranteInvalidoException ex = assertThrows(
                    DonoRestauranteInvalidoException.class,
                    () -> restaurante.validarDono(operadorNaoAssociado)
            );
            assertEquals("Operador não é o dono deste restaurante.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar DonoRestauranteInvalidoException quando operador pertencer ao restaurante mas for CLIENTE")
        void deveLancarExcecaoQuandoOperadorForClienteENaoDono() {
            restaurante.adicionaUsuario(usuarioCliente);

            DonoRestauranteInvalidoException ex = assertThrows(
                    DonoRestauranteInvalidoException.class,
                    () -> restaurante.validarDono(usuarioCliente.getId())
            );
            assertEquals("Operador não é o dono deste restaurante.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Construtores e Getters/Setters")
    class ConstrutoresTests {

        @Test
        @DisplayName("Deve instanciar via construtor padrão vazio")
        void deveInstanciarConstrutorVazio() {
            Restaurante instancia = new Restaurante();
            assertNotNull(instancia);
            assertNull(instancia.getId());
            assertNull(instancia.getNome());
        }
    }
}
