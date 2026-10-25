package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: CriarRestauranteUsecase")
class CriarRestauranteUsecaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private CriarRestauranteUsecaseImpl usecase;

    @Test
    @DisplayName("Deve criar restaurante com sucesso quando usuário for dono de restaurante ativo")
    void deveCriarRestauranteComSucesso() {
        var donoId = UUID.randomUUID();
        var restauranteId = UUID.randomUUID();

        var input = CriarRestauranteInputDto.builder()
                .nome("Trattoria Bella")
                .endereco("Rua das Oliveiras, 50")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("18:00 às 23:00")
                .donoId(donoId)
                .build();

        var usuarioDono = Usuario.builder()
                .id(donoId)
                .tipoUsuario(TipoUsuario.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build())
                .ativo(true)
                .build();

        var salvo = Restaurante.builder()
                .id(restauranteId)
                .nome("Trattoria Bella")
                .dono(usuarioDono)
                .ativo(true)
                .build();

        when(usuarioGateway.buscarPorId(donoId)).thenReturn(Optional.of(usuarioDono));
        when(restauranteGateway.salvar(any(Restaurante.class))).thenReturn(salvo);

        CriarRestauranteOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(restauranteId);
        verify(restauranteGateway).salvar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando input ou donoId for nulo")
    void deveLancarExcecaoQuandoInputOuDonoNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Dados do restaurante são obrigatórios.");

        var inputSemDono = CriarRestauranteInputDto.builder().nome("Restaurante").build();
        assertThatThrownBy(() -> usecase.executar(inputSemDono))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Restaurante deve possuir um dono associado.");

        verify(restauranteGateway, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário informado não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        var donoId = UUID.randomUUID();
        var input = CriarRestauranteInputDto.builder().donoId(donoId).build();

        when(usuarioGateway.buscarPorId(donoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(DonoRestauranteInvalidoException.class)
                .hasMessage("Usuário informado não existe.");

        verify(restauranteGateway, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário informado estiver inativo")
    void deveLancarExcecaoQuandoUsuarioInativo() {
        var donoId = UUID.randomUUID();
        var input = CriarRestauranteInputDto.builder().donoId(donoId).build();

        var usuarioInativo = Usuario.builder()
                .id(donoId)
                .ativo(false)
                .build();

        when(usuarioGateway.buscarPorId(donoId)).thenReturn(Optional.of(usuarioInativo));

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(DonoRestauranteInvalidoException.class)
                .hasMessage("Usuário informado está inativo.");

        verify(restauranteGateway, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não tiver perfil de Dono de Restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoEDono() {
        var clienteId = UUID.randomUUID();
        var input = CriarRestauranteInputDto.builder().donoId(clienteId).build();

        var usuarioCliente = Usuario.builder()
                .id(clienteId)
                .tipoUsuario(TipoUsuario.builder().tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build())
                .ativo(true)
                .build();

        when(usuarioGateway.buscarPorId(clienteId)).thenReturn(Optional.of(usuarioCliente));

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(DonoRestauranteInvalidoException.class)
                .hasMessage("O usuário informado não possui o perfil de 'Dono de restaurante'.");

        verify(restauranteGateway, never()).salvar(any());
    }
}
