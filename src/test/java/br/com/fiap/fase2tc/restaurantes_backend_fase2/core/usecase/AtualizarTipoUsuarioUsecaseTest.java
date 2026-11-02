package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: AtualizarTipoUsuarioUsecase")
class AtualizarTipoUsuarioUsecaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @InjectMocks
    private AtualizarTipoUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve atualizar tipo de usuário com sucesso")
    void deveAtualizarTipoComSucesso() {
        var id = UUID.randomUUID();
        var input = AtualizarTipoUsuarioInputDto.builder()
                .id(id)
                .nome("Cliente")
                .build();

        var existente = TipoUsuario.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .build();

        var atualizado = TipoUsuario.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        when(tipoUsuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.of(existente));
        when(tipoUsuarioGateway.buscarPorTipo(TipoUsuarioEnum.CLIENTE)).thenReturn(Optional.empty());
        when(tipoUsuarioGateway.atualizar(any(TipoUsuario.class))).thenReturn(atualizado);

        TipoUsuarioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
        verify(tipoUsuarioGateway).atualizar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não for encontrado")
    void deveLancarExcecaoQuandoNaoEncontrado() {
        var id = UUID.randomUUID();
        var input = AtualizarTipoUsuarioInputDto.builder()
                .id(id)
                .nome("Cliente")
                .build();

        when(tipoUsuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Tipo de usuário não encontrado.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando outro registro já possuir o mesmo tipo")
    void deveLancarExcecaoQuandoTipoDuplicado() {
        var id = UUID.randomUUID();
        var outroId = UUID.randomUUID();
        var input = AtualizarTipoUsuarioInputDto.builder()
                .id(id)
                .nome("Cliente")
                .build();

        var existente = TipoUsuario.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .build();

        var outroExistente = TipoUsuario.builder()
                .id(outroId)
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        when(tipoUsuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.of(existente));
        when(tipoUsuarioGateway.buscarPorTipo(TipoUsuarioEnum.CLIENTE)).thenReturn(Optional.of(outroExistente));

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Já existe outro tipo de usuário cadastrado com esse valor.");
    }
}
