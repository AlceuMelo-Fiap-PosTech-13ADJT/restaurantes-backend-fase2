package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: CriarTipoUsuarioUsecase")
class CriarTipoUsuarioUsecaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @InjectMocks
    private CriarTipoUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve criar TipoUsuario com sucesso para Dono de restaurante")
    void deveCriarTipoUsuarioComSucesso() {
        var input = CriarTipoUsuarioInputDto.builder()
                .nome("Dono de restaurante")
                .build();

        var salvo = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .build();

        when(tipoUsuarioGateway.existePorTipo(TipoUsuarioEnum.DONO_RESTAURANTE)).thenReturn(false);
        when(tipoUsuarioGateway.salvar(any(TipoUsuario.class))).thenReturn(salvo);

        TipoUsuarioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(salvo.getId());
        assertThat(output.getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
        verify(tipoUsuarioGateway).salvar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for nulo ou em branco")
    void deveLancarExcecaoQuandoNomeNuloOuBranco() {
        var input = CriarTipoUsuarioInputDto.builder().nome("   ").build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Nome do tipo de usuário é obrigatório.");

        verify(tipoUsuarioGateway, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo for inválido")
    void deveLancarExcecaoQuandoTipoInvalido() {
        var input = CriarTipoUsuarioInputDto.builder().nome("InvalidoTipo").build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("Tipo de usuário inválido");

        verify(tipoUsuarioGateway, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo já estiver cadastrado")
    void deveLancarExcecaoQuandoTipoJaCadastrado() {
        var input = CriarTipoUsuarioInputDto.builder().nome("Cliente").build();

        when(tipoUsuarioGateway.existePorTipo(TipoUsuarioEnum.CLIENTE)).thenReturn(true);

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Tipo de usuário já cadastrado.");

        verify(tipoUsuarioGateway, never()).salvar(any());
    }
}
