package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.CardapioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.RestauranteController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.TipoUsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.UsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AssociarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AssociarTipoUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarCardapioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarItemCardapioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarRestauranteUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarTipoUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarItemCardapioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarItemCardapioPorIdUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarRestaurantePorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarRestaurantePorIdUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarTipoUsuarioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarTipoUsuarioPorIdUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarUsuarioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarUsuarioPorIdUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarItemCardapioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarRestauranteUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarTipoUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarItemCardapioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarRestauranteUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarTipoUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarItensCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarItensCardapioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarRestaurantesUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarRestaurantesUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarTiposUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarTiposUsuarioUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarUsuariosPorRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarUsuariosPorRestauranteUsecaseImpl;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ValidarPermissaoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ValidarPermissaoUsuarioUsecaseImpl;

@Configuration
public class InjecaoDependenciaConfiguration {

    // Cross-cutting / Permissão
    @Bean
    public ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase(
            UsuarioGateway usuarioGateway,
            RestauranteGateway restauranteGateway) {
        return new ValidarPermissaoUsuarioUsecaseImpl(usuarioGateway, restauranteGateway);
    }

    // TipoUsuario
    @Bean
    public CriarTipoUsuarioUsecase criarTipoUsuarioUsecase(
            TipoUsuarioGateway tipoUsuarioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new CriarTipoUsuarioUsecaseImpl(tipoUsuarioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase(
            TipoUsuarioGateway tipoUsuarioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new AtualizarTipoUsuarioUsecaseImpl(tipoUsuarioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public BuscarTipoUsuarioPorIdUsecase buscarTipoUsuarioPorIdUsecase(TipoUsuarioGateway tipoUsuarioGateway) {
        return new BuscarTipoUsuarioPorIdUsecaseImpl(tipoUsuarioGateway);
    }

    @Bean
    public ListarTiposUsuarioUsecase listarTiposUsuarioUsecase(TipoUsuarioGateway tipoUsuarioGateway) {
        return new ListarTiposUsuarioUsecaseImpl(tipoUsuarioGateway);
    }

    @Bean
    public DesativarTipoUsuarioUsecase desativarTipoUsuarioUsecase(
            TipoUsuarioGateway tipoUsuarioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new DesativarTipoUsuarioUsecaseImpl(tipoUsuarioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public AssociarTipoUsuarioUsecase associarTipoUsuarioUsecase(
            UsuarioGateway usuarioGateway,
            TipoUsuarioGateway tipoUsuarioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new AssociarTipoUsuarioUsecaseImpl(usuarioGateway, tipoUsuarioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public TipoUsuarioController tipoUsuarioController(
            CriarTipoUsuarioUsecase criarTipoUsuarioUsecase,
            AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase,
            BuscarTipoUsuarioPorIdUsecase buscarTipoUsuarioPorIdUsecase,
            ListarTiposUsuarioUsecase listarTiposUsuarioUsecase,
            DesativarTipoUsuarioUsecase desativarTipoUsuarioUsecase,
            AssociarTipoUsuarioUsecase associarTipoUsuarioUsecase) {
        return new TipoUsuarioController(
                criarTipoUsuarioUsecase,
                atualizarTipoUsuarioUsecase,
                buscarTipoUsuarioPorIdUsecase,
                listarTiposUsuarioUsecase,
                desativarTipoUsuarioUsecase,
                associarTipoUsuarioUsecase
        );
    }

    // Restaurante
    @Bean
    public CriarRestauranteUsecase criarRestauranteUsecase(
            RestauranteGateway restauranteGateway,
            UsuarioGateway usuarioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new CriarRestauranteUsecaseImpl(restauranteGateway, usuarioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public AtualizarRestauranteUsecase atualizarRestauranteUsecase(
            RestauranteGateway restauranteGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new AtualizarRestauranteUsecaseImpl(restauranteGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public BuscarRestaurantePorIdUsecase buscarRestaurantePorIdUsecase(RestauranteGateway restauranteGateway) {
        return new BuscarRestaurantePorIdUsecaseImpl(restauranteGateway);
    }

    @Bean
    public ListarRestaurantesUsecase listarRestaurantesUsecase(RestauranteGateway restauranteGateway) {
        return new ListarRestaurantesUsecaseImpl(restauranteGateway);
    }

    @Bean
    public DesativarRestauranteUsecase desativarRestauranteUsecase(
            RestauranteGateway restauranteGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new DesativarRestauranteUsecaseImpl(restauranteGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public RestauranteController restauranteController(
            CriarRestauranteUsecase criarRestauranteUsecase,
            AtualizarRestauranteUsecase atualizarRestauranteUsecase,
            BuscarRestaurantePorIdUsecase buscarRestaurantePorIdUsecase,
            ListarRestaurantesUsecase listarRestaurantesUsecase,
            DesativarRestauranteUsecase desativarRestauranteUsecase) {
        return new RestauranteController(
                criarRestauranteUsecase,
                atualizarRestauranteUsecase,
                buscarRestaurantePorIdUsecase,
                listarRestaurantesUsecase,
                desativarRestauranteUsecase
        );
    }

    // Cardapio
    @Bean
    public CriarItemCardapioUsecase criarItemCardapioUsecase(
            ItemCardapioGateway itemCardapioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new CriarItemCardapioUsecaseImpl(itemCardapioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public AtualizarItemCardapioUsecase atualizarItemCardapioUsecase(
            ItemCardapioGateway itemCardapioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new AtualizarItemCardapioUsecaseImpl(itemCardapioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public AtualizarCardapioUsecase atualizarCardapioUsecase(
            AtualizarItemCardapioUsecase atualizarItemCardapioUsecase) {
        return new AtualizarCardapioUsecaseImpl(atualizarItemCardapioUsecase);
    }

    @Bean
    public BuscarItemCardapioPorIdUsecase buscarItemCardapioPorIdUsecase(
            ItemCardapioGateway itemCardapioGateway,
            RestauranteGateway restauranteGateway) {
        return new BuscarItemCardapioPorIdUsecaseImpl(itemCardapioGateway, restauranteGateway);
    }

    @Bean
    public ListarItensCardapioUsecase listarItensCardapioUsecase(
            ItemCardapioGateway itemCardapioGateway,
            RestauranteGateway restauranteGateway) {
        return new ListarItensCardapioUsecaseImpl(itemCardapioGateway, restauranteGateway);
    }

    @Bean
    public DesativarItemCardapioUsecase desativarItemCardapioUsecase(
            ItemCardapioGateway itemCardapioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new DesativarItemCardapioUsecaseImpl(itemCardapioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public CardapioController cardapioController(
            CriarItemCardapioUsecase criarItemCardapioUsecase,
            AtualizarItemCardapioUsecase atualizarItemCardapioUsecase,
            BuscarItemCardapioPorIdUsecase buscarItemCardapioPorIdUsecase,
            ListarItensCardapioUsecase listarItensCardapioUsecase,
            DesativarItemCardapioUsecase desativarItemCardapioUsecase) {
        return new CardapioController(
                criarItemCardapioUsecase,
                atualizarItemCardapioUsecase,
                buscarItemCardapioPorIdUsecase,
                listarItensCardapioUsecase,
                desativarItemCardapioUsecase
        );
    }

    // Usuario
    @Bean
    public CriarUsuarioUsecase criarUsuarioUsecase(
            UsuarioGateway usuarioGateway,
            TipoUsuarioGateway tipoUsuarioGateway,
            RestauranteGateway restauranteGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new CriarUsuarioUsecaseImpl(usuarioGateway, tipoUsuarioGateway, restauranteGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public AtualizarUsuarioUsecase atualizarUsuarioUsecase(
            UsuarioGateway usuarioGateway,
            TipoUsuarioGateway tipoUsuarioGateway,
            RestauranteGateway restauranteGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new AtualizarUsuarioUsecaseImpl(usuarioGateway, tipoUsuarioGateway, restauranteGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public BuscarUsuarioPorIdUsecase buscarUsuarioPorIdUsecase(UsuarioGateway usuarioGateway) {
        return new BuscarUsuarioPorIdUsecaseImpl(usuarioGateway);
    }

    @Bean
    public ListarUsuariosPorRestauranteUsecase listarUsuariosPorRestauranteUsecase(
            UsuarioGateway usuarioGateway,
            RestauranteGateway restauranteGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new ListarUsuariosPorRestauranteUsecaseImpl(usuarioGateway, restauranteGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public DesativarUsuarioUsecase desativarUsuarioUsecase(
            UsuarioGateway usuarioGateway,
            ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase) {
        return new DesativarUsuarioUsecaseImpl(usuarioGateway, validarPermissaoUsuarioUsecase);
    }

    @Bean
    public UsuarioController usuarioController(
            CriarUsuarioUsecase criarUsuarioUsecase,
            AtualizarUsuarioUsecase atualizarUsuarioUsecase,
            BuscarUsuarioPorIdUsecase buscarUsuarioPorIdUsecase,
            ListarUsuariosPorRestauranteUsecase listarUsuariosPorRestauranteUsecase,
            DesativarUsuarioUsecase desativarUsuarioUsecase,
            AssociarTipoUsuarioUsecase associarTipoUsuarioUsecase) {
        return new UsuarioController(
                criarUsuarioUsecase,
                atualizarUsuarioUsecase,
                buscarUsuarioPorIdUsecase,
                listarUsuariosPorRestauranteUsecase,
                desativarUsuarioUsecase,
                associarTipoUsuarioUsecase
        );
    }
}
