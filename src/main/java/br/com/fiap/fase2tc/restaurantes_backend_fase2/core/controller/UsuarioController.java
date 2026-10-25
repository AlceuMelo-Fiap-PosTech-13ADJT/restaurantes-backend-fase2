package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller;

import java.util.List;
import java.util.UUID;

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
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UsuarioController {

    private final CriarUsuarioUsecase criarUsuarioUsecase;
    private final AtualizarUsuarioUsecase atualizarUsuarioUsecase;
    private final BuscarUsuarioPorIdUsecase buscarUsuarioPorIdUsecase;
    private final ListarUsuariosPorRestauranteUsecase listarUsuariosPorRestauranteUsecase;
    private final DesativarUsuarioUsecase desativarUsuarioUsecase;
    private final AssociarTipoUsuarioUsecase associarTipoUsuarioUsecase;

    public UsuarioOutputDto criar(CriarUsuarioInputDto inputDto) {
        return criarUsuarioUsecase.executar(inputDto);
    }

    public UsuarioOutputDto atualizar(AtualizarUsuarioInputDto inputDto) {
        return atualizarUsuarioUsecase.executar(inputDto);
    }

    public UsuarioOutputDto buscarPorId(UUID id) {
        return buscarUsuarioPorIdUsecase.executar(id);
    }

    public List<UsuarioOutputDto> listarPorRestaurante(UUID restauranteId) {
        return listarUsuariosPorRestauranteUsecase.executar(restauranteId);
    }

    public void desativar(UUID id) {
        desativarUsuarioUsecase.executar(id);
    }

    public UsuarioOutputDto associarTipoUsuario(AssociarTipoUsuarioInputDto inputDto) {
        return associarTipoUsuarioUsecase.executar(inputDto);
    }
}
