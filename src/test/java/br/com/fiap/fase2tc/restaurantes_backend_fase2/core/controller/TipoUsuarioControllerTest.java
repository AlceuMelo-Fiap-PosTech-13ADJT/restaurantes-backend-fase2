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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AssociarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarTipoUsuarioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarTipoUsuarioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarTiposUsuarioUsecase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Controller Core: TipoUsuarioController")
class TipoUsuarioControllerTest {

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

    @InjectMocks
    private TipoUsuarioController controller;

    @Test
    @DisplayName("Deve delegar criação para CriarTipoUsuarioUsecase")
    void deveDelegarCriacao() {
        var input = CriarTipoUsuarioInputDto.builder().nome("Dono de restaurante").build();
        var expected = TipoUsuarioOutputDto.builder().id(UUID.randomUUID()).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).build();

        when(criarTipoUsuarioUsecase.executar(input)).thenReturn(expected);

        TipoUsuarioOutputDto result = controller.criar(input);

        assertThat(result).isSameAs(expected);
        verify(criarTipoUsuarioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar atualização para AtualizarTipoUsuarioUsecase")
    void deveDelegarAtualizacao() {
        var input = AtualizarTipoUsuarioInputDto.builder().id(UUID.randomUUID()).nome("Cliente").build();
        var expected = TipoUsuarioOutputDto.builder().id(input.getId()).tipo(TipoUsuarioEnum.CLIENTE).build();

        when(atualizarTipoUsuarioUsecase.executar(input)).thenReturn(expected);

        TipoUsuarioOutputDto result = controller.atualizar(input);

        assertThat(result).isSameAs(expected);
        verify(atualizarTipoUsuarioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar busca por ID para BuscarTipoUsuarioPorIdUsecase")
    void deveDelegarBuscaPorId() {
        var id = UUID.randomUUID();
        var expected = TipoUsuarioOutputDto.builder().id(id).tipo(TipoUsuarioEnum.CLIENTE).build();

        when(buscarTipoUsuarioPorIdUsecase.executar(id)).thenReturn(expected);

        TipoUsuarioOutputDto result = controller.buscarPorId(id);

        assertThat(result).isSameAs(expected);
        verify(buscarTipoUsuarioPorIdUsecase).executar(id);
    }

    @Test
    @DisplayName("Deve delegar listagem para ListarTiposUsuarioUsecase")
    void deveDelegarListagem() {
        var item1 = TipoUsuarioOutputDto.builder().id(UUID.randomUUID()).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).build();
        var item2 = TipoUsuarioOutputDto.builder().id(UUID.randomUUID()).tipo(TipoUsuarioEnum.CLIENTE).build();
        var expected = List.of(item1, item2);

        when(listarTiposUsuarioUsecase.executar()).thenReturn(expected);

        List<TipoUsuarioOutputDto> result = controller.listar();

        assertThat(result).isSameAs(expected);
        verify(listarTiposUsuarioUsecase).executar();
    }

    @Test
    @DisplayName("Deve delegar desativação para DesativarTipoUsuarioUsecase")
    void deveDelegarDesativacao() {
        var id = UUID.randomUUID();

        controller.desativar(id);

        verify(desativarTipoUsuarioUsecase).executar(id);
    }

    @Test
    @DisplayName("Deve delegar associação de tipo para AssociarTipoUsuarioUsecase")
    void deveDelegarAssociacao() {
        var input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(UUID.randomUUID())
                .tipoUsuarioId(UUID.randomUUID())
                .build();
        var expected = UsuarioOutputDto.builder().id(input.getUsuarioId()).build();

        when(associarTipoUsuarioUsecase.executar(input)).thenReturn(expected);

        UsuarioOutputDto result = controller.associar(input);

        assertThat(result).isSameAs(expected);
        verify(associarTipoUsuarioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve instanciar via construtor simplificado com 2 parâmetros")
    void deveInstanciarComDoisParametros() {
        var simpleController = new TipoUsuarioController(criarTipoUsuarioUsecase, listarTiposUsuarioUsecase);
        assertThat(simpleController).isNotNull();
    }
}
