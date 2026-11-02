package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: ListarTiposUsuarioUsecase")
class ListarTiposUsuarioUsecaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @InjectMocks
    private ListarTiposUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve listar todos os tipos de usuário ativos")
    void deveListarTiposAtivos() {
        var tipo1 = TipoUsuario.builder().id(UUID.randomUUID()).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();
        var tipo2 = TipoUsuario.builder().id(UUID.randomUUID()).tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build();

        when(tipoUsuarioGateway.listarAtivos()).thenReturn(List.of(tipo1, tipo2));

        List<TipoUsuarioOutputDto> lista = usecase.executar();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
        assertThat(lista.get(1).getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
    }
}
