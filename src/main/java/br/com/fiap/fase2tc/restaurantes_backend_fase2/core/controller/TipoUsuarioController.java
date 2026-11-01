package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller;

import java.util.List;
import java.util.UUID;

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
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class TipoUsuarioController {

    private final CriarTipoUsuarioUsecase criarTipoUsuarioUsecase;
    private final AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase;
    private final BuscarTipoUsuarioPorIdUsecase buscarTipoUsuarioPorIdUsecase;
    private final ListarTiposUsuarioUsecase listarTiposUsuarioUsecase;
    private final DesativarTipoUsuarioUsecase desativarTipoUsuarioUsecase;
    private final AssociarTipoUsuarioUsecase associarTipoUsuarioUsecase;

    public TipoUsuarioController(
            CriarTipoUsuarioUsecase criarTipoUsuarioUsecase,
            ListarTiposUsuarioUsecase listarTiposUsuarioUsecase) {
        this(criarTipoUsuarioUsecase, null, null, listarTiposUsuarioUsecase, null, null);
    }

    public TipoUsuarioOutputDto criar(CriarTipoUsuarioInputDto inputDto) {
        return criarTipoUsuarioUsecase.executar(inputDto);
    }

    public TipoUsuarioOutputDto atualizar(AtualizarTipoUsuarioInputDto inputDto) {
        return atualizarTipoUsuarioUsecase.executar(inputDto);
    }

    public TipoUsuarioOutputDto buscarPorId(UUID id) {
        return buscarTipoUsuarioPorIdUsecase.executar(id);
    }

    public List<TipoUsuarioOutputDto> listar() {
        return listarTiposUsuarioUsecase.executar();
    }

    public void desativar(UUID id) {
        desativarTipoUsuarioUsecase.executar(id);
    }

    public void desativar(UUID id, UUID usuarioOperadorId) {
        desativarTipoUsuarioUsecase.executar(id, usuarioOperadorId);
    }

    public UsuarioOutputDto associar(AssociarTipoUsuarioInputDto inputDto) {
        return associarTipoUsuarioUsecase.executar(inputDto);
    }
}
