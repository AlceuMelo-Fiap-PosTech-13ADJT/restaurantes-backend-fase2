package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AssociarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarUsuarioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarUsuariosPorRestauranteUsecase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Controller Core: UsuarioController")
class UsuarioControllerTest {

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
    private AssociarTipoUsuarioUsecase associarTipoUsuarioUsecase;

    @InjectMocks
    private UsuarioController controller;

    @Test
    @DisplayName("Deve delegar criação para CriarUsuarioUsecase")
    void deveDelegarCriacao() {
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder().nome("Carlos").email("carlos@email.com").build();
        UsuarioOutputDto expected = UsuarioOutputDto.builder().id(UUID.randomUUID()).nome("Carlos").build();
        when(criarUsuarioUsecase.executar(input)).thenReturn(expected);

        UsuarioOutputDto result = controller.criar(input);

        assertThat(result).isSameAs(expected);
        verify(criarUsuarioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar atualização para AtualizarUsuarioUsecase")
    void deveDelegarAtualizacao() {
        AtualizarUsuarioInputDto input = AtualizarUsuarioInputDto.builder().id(UUID.randomUUID()).nome("Carlos").build();
        UsuarioOutputDto expected = UsuarioOutputDto.builder().id(input.getId()).nome("Carlos").build();
        when(atualizarUsuarioUsecase.executar(input)).thenReturn(expected);

        UsuarioOutputDto result = controller.atualizar(input);

        assertThat(result).isSameAs(expected);
        verify(atualizarUsuarioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar busca por ID para BuscarUsuarioPorIdUsecase")
    void deveDelegarBuscarPorId() {
        UUID id = UUID.randomUUID();
        UsuarioOutputDto expected = UsuarioOutputDto.builder().id(id).nome("Usuario Teste").build();
        when(buscarUsuarioPorIdUsecase.executar(id)).thenReturn(expected);

        UsuarioOutputDto result = controller.buscarPorId(id);

        assertThat(result).isSameAs(expected);
        verify(buscarUsuarioPorIdUsecase).executar(id);
    }

    @Test
    @DisplayName("Deve delegar listagem por restaurante para ListarUsuariosPorRestauranteUsecase")
    void deveDelegarListarPorRestaurante() {
        UUID restauranteId = UUID.randomUUID();
        List<UsuarioOutputDto> expected = List.of(UsuarioOutputDto.builder().id(UUID.randomUUID()).build());
        when(listarUsuariosPorRestauranteUsecase.executar(restauranteId)).thenReturn(expected);

        List<UsuarioOutputDto> result = controller.listarPorRestaurante(restauranteId);

        assertThat(result).isSameAs(expected);
        verify(listarUsuariosPorRestauranteUsecase).executar(restauranteId);
    }

    @Test
    @DisplayName("Deve delegar desativação para DesativarUsuarioUsecase")
    void deveDelegarDesativacao() {
        UUID id = UUID.randomUUID();

        controller.desativar(id);

        verify(desativarUsuarioUsecase).executar(id);
    }

    @Test
    @DisplayName("Deve delegar associação de tipo para AssociarTipoUsuarioUsecase")
    void deveDelegarAssociarTipoUsuario() {
        AssociarTipoUsuarioInputDto input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(UUID.randomUUID())
                .tipoUsuarioId(UUID.randomUUID())
                .build();
        UsuarioOutputDto expected = UsuarioOutputDto.builder().id(input.getUsuarioId()).build();
        when(associarTipoUsuarioUsecase.executar(input)).thenReturn(expected);

        UsuarioOutputDto result = controller.associarTipoUsuario(input);

        assertThat(result).isSameAs(expected);
        verify(associarTipoUsuarioUsecase).executar(input);
    }
}
