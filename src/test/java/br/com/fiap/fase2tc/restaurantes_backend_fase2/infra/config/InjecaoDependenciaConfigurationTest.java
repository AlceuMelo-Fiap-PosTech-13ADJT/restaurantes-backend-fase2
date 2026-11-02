package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.CardapioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.RestauranteController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.TipoUsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.UsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AssociarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarItemCardapioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarRestaurantePorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarTipoUsuarioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarUsuarioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarUsuariosPorRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarItensCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarRestaurantesUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarTiposUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ValidarPermissaoUsuarioUsecase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Configuração: InjecaoDependenciaConfiguration")
class InjecaoDependenciaConfigurationTest {

    private InjecaoDependenciaConfiguration configuration;

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    @Mock
    private ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Mock
    private CriarTipoUsuarioUsecase criarTipoUsuarioUsecase;

    @Mock
    private AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase;

    @Mock
    private BuscarTipoUsuarioPorIdUsecase buscarTipoUsuarioPorIdUsecase;

    @Mock
    private ListarTiposUsuarioUsecase listarTiposUsuarioUsecase;

    @Mock
    private DesativarTipoUsuarioUsecase desativarTipoUsuarioUsecase;

    @Mock
    private AssociarTipoUsuarioUsecase associarTipoUsuarioUsecase;

    @Mock
    private CriarUsuarioUsecase criarUsuarioUsecase;

    @Mock
    private AtualizarUsuarioUsecase atualizarUsuarioUsecase;

    @Mock
    private BuscarUsuarioPorIdUsecase buscarUsuarioPorIdUsecase;

    @Mock
    private ListarUsuariosPorRestauranteUsecase listarUsuariosPorRestauranteUsecase;

    @Mock
    private DesativarUsuarioUsecase desativarUsuarioUsecase;

    @Mock
    private CriarRestauranteUsecase criarRestauranteUsecase;

    @Mock
    private AtualizarRestauranteUsecase atualizarRestauranteUsecase;

    @Mock
    private BuscarRestaurantePorIdUsecase buscarRestaurantePorIdUsecase;

    @Mock
    private ListarRestaurantesUsecase listarRestaurantesUsecase;

    @Mock
    private DesativarRestauranteUsecase desativarRestauranteUsecase;

    @Mock
    private CriarItemCardapioUsecase criarItemCardapioUsecase;

    @Mock
    private AtualizarItemCardapioUsecase atualizarItemCardapioUsecase;

    @Mock
    private BuscarItemCardapioPorIdUsecase buscarItemCardapioPorIdUsecase;

    @Mock
    private ListarItensCardapioUsecase listarItensCardapioUsecase;

    @Mock
    private DesativarItemCardapioUsecase desativarItemCardapioUsecase;

    @BeforeEach
    void setUp() {
        configuration = new InjecaoDependenciaConfiguration();
    }

    @Test
    @DisplayName("Deve instanciar ValidarPermissaoUsuarioUsecase")
    void deveInstanciarValidarPermissaoUsuarioUsecase() {
        ValidarPermissaoUsuarioUsecase usecase = configuration.validarPermissaoUsuarioUsecase(usuarioGateway, restauranteGateway);
        assertThat(usecase).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar use cases de TipoUsuario")
    void deveInstanciarUseCasesTipoUsuario() {
        assertThat(configuration.criarTipoUsuarioUsecase(tipoUsuarioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.atualizarTipoUsuarioUsecase(tipoUsuarioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.buscarTipoUsuarioPorIdUsecase(tipoUsuarioGateway)).isNotNull();
        assertThat(configuration.listarTiposUsuarioUsecase(tipoUsuarioGateway)).isNotNull();
        assertThat(configuration.desativarTipoUsuarioUsecase(tipoUsuarioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.associarTipoUsuarioUsecase(usuarioGateway, tipoUsuarioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar TipoUsuarioController com suas dependências")
    void deveInstanciarTipoUsuarioController() {
        TipoUsuarioController controller = configuration.tipoUsuarioController(
                criarTipoUsuarioUsecase,
                atualizarTipoUsuarioUsecase,
                buscarTipoUsuarioPorIdUsecase,
                listarTiposUsuarioUsecase,
                desativarTipoUsuarioUsecase,
                associarTipoUsuarioUsecase
        );
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar use cases de Restaurante")
    void deveInstanciarUseCasesRestaurante() {
        assertThat(configuration.criarRestauranteUsecase(restauranteGateway, usuarioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.atualizarRestauranteUsecase(restauranteGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.buscarRestaurantePorIdUsecase(restauranteGateway)).isNotNull();
        assertThat(configuration.listarRestaurantesUsecase(restauranteGateway)).isNotNull();
        assertThat(configuration.desativarRestauranteUsecase(restauranteGateway, validarPermissaoUsuarioUsecase)).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar RestauranteController com suas dependências")
    void deveInstanciarRestauranteController() {
        RestauranteController controller = configuration.restauranteController(
                criarRestauranteUsecase,
                atualizarRestauranteUsecase,
                buscarRestaurantePorIdUsecase,
                listarRestaurantesUsecase,
                desativarRestauranteUsecase
        );
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar use cases de Cardapio")
    void deveInstanciarUseCasesCardapio() {
        assertThat(configuration.criarItemCardapioUsecase(itemCardapioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.atualizarItemCardapioUsecase(itemCardapioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.atualizarCardapioUsecase(atualizarItemCardapioUsecase)).isNotNull();
        assertThat(configuration.buscarItemCardapioPorIdUsecase(itemCardapioGateway, restauranteGateway)).isNotNull();
        assertThat(configuration.listarItensCardapioUsecase(itemCardapioGateway, restauranteGateway)).isNotNull();
        assertThat(configuration.desativarItemCardapioUsecase(itemCardapioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar CardapioController com suas dependências")
    void deveInstanciarCardapioController() {
        CardapioController controller = configuration.cardapioController(
                criarItemCardapioUsecase,
                atualizarItemCardapioUsecase,
                buscarItemCardapioPorIdUsecase,
                listarItensCardapioUsecase,
                desativarItemCardapioUsecase
        );
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar use cases de Usuario")
    void deveInstanciarUseCasesUsuario() {
        assertThat(configuration.criarUsuarioUsecase(usuarioGateway, tipoUsuarioGateway, restauranteGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.atualizarUsuarioUsecase(usuarioGateway, tipoUsuarioGateway, restauranteGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.buscarUsuarioPorIdUsecase(usuarioGateway)).isNotNull();
        assertThat(configuration.listarUsuariosPorRestauranteUsecase(usuarioGateway, restauranteGateway, validarPermissaoUsuarioUsecase)).isNotNull();
        assertThat(configuration.desativarUsuarioUsecase(usuarioGateway, validarPermissaoUsuarioUsecase)).isNotNull();
    }

    @Test
    @DisplayName("Deve instanciar UsuarioController com suas dependências")
    void deveInstanciarUsuarioController() {
        UsuarioController controller = configuration.usuarioController(
                criarUsuarioUsecase,
                atualizarUsuarioUsecase,
                buscarUsuarioPorIdUsecase,
                listarUsuariosPorRestauranteUsecase,
                desativarUsuarioUsecase,
                associarTipoUsuarioUsecase
        );
        assertThat(controller).isNotNull();
    }
}
