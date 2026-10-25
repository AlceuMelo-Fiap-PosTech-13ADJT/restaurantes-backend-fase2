package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.AcessoNaoAutorizadoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: ValidarPermissaoUsuarioUsecase")
class ValidarPermissaoUsuarioUsecaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private ValidarPermissaoUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve lançar exceção quando inputDto for nulo")
    void deveLancarExcecaoQuandoInputNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(AcessoNaoAutorizadoException.class)
                .hasMessage("Identificação do usuário operador é obrigatória.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuarioOperadorId for nulo")
    void deveLancarExcecaoQuandoOperadorIdNulo() {
        var input = ValidarPermissaoUsuarioInputDto.builder().build();
        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(AcessoNaoAutorizadoException.class)
                .hasMessage("Identificação do usuário operador é obrigatória.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando operador não for encontrado")
    void deveLancarExcecaoQuandoOperadorNaoEncontrado() {
        var operadorId = UUID.randomUUID();
        when(usuarioGateway.buscarAtivoPorId(operadorId)).thenReturn(Optional.empty());

        var input = ValidarPermissaoUsuarioInputDto.builder()
                .usuarioOperadorId(operadorId)
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(AcessoNaoAutorizadoException.class)
                .hasMessage("Operador não encontrado ou inativo.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        var operadorId = UUID.randomUUID();
        var restauranteId = UUID.randomUUID();

        var operador = Usuario.builder()
                .id(operadorId)
                .ativo(true)
                .build();

        when(usuarioGateway.buscarAtivoPorId(operadorId)).thenReturn(Optional.of(operador));
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.empty());

        var input = ValidarPermissaoUsuarioInputDto.builder()
                .usuarioOperadorId(operadorId)
                .restauranteId(restauranteId)
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado ou inativo.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando requer dono e operador não é o dono")
    void deveLancarExcecaoQuandoRequerDonoEOperadorNaoEDono() {
        var operadorId = UUID.randomUUID();
        var outroDonoId = UUID.randomUUID();
        var restauranteId = UUID.randomUUID();

        var operador = Usuario.builder()
                .id(operadorId)
                .ativo(true)
                .build();

        var outroDono = Usuario.builder()
                .id(outroDonoId)
                .ativo(true)
                .build();

        var restaurante = Restaurante.builder()
                .id(restauranteId)
                .dono(outroDono)
                .ativo(true)
                .build();

        when(usuarioGateway.buscarAtivoPorId(operadorId)).thenReturn(Optional.of(operador));
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));

        var input = ValidarPermissaoUsuarioInputDto.builder()
                .usuarioOperadorId(operadorId)
                .restauranteId(restauranteId)
                .requerDono(true)
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(DonoRestauranteInvalidoException.class);
    }

    @Test
    @DisplayName("Deve validar com sucesso quando operador for o dono do restaurante")
    void deveValidarComSucessoQuandoOperadorForDono() {
        var operadorId = UUID.randomUUID();
        var restauranteId = UUID.randomUUID();

        var operador = Usuario.builder()
                .id(operadorId)
                .tipoUsuario(TipoUsuario.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build())
                .ativo(true)
                .build();

        var restaurante = Restaurante.builder()
                .id(restauranteId)
                .dono(operador)
                .ativo(true)
                .build();

        when(usuarioGateway.buscarAtivoPorId(operadorId)).thenReturn(Optional.of(operador));
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));

        var input = ValidarPermissaoUsuarioInputDto.builder()
                .usuarioOperadorId(operadorId)
                .restauranteId(restauranteId)
                .requerDono(true)
                .build();

        Usuario resultado = usecase.executar(input);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(operadorId);
    }
}
