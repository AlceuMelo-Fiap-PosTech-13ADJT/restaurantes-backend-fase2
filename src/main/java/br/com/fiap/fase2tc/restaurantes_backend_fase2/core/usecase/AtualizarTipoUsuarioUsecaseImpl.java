package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarTipoUsuarioUsecaseImpl implements AtualizarTipoUsuarioUsecase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public TipoUsuarioOutputDto executar(AtualizarTipoUsuarioInputDto inputDto) {
        if (inputDto == null || inputDto.getId() == null) {
            throw new RegraNegocioException("ID do tipo de usuário é obrigatório.");
        }
        if (inputDto.getNome() == null || inputDto.getNome().isBlank()) {
            throw new RegraNegocioException("Nome do tipo de usuário é obrigatório.");
        }

        if (inputDto.getUsuarioOperadorId() != null) {
            validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                    .usuarioOperadorId(inputDto.getUsuarioOperadorId())
                    .requerDono(true)
                    .build());
        }

        TipoUsuario tipoUsuario = tipoUsuarioGateway.buscarAtivoPorId(inputDto.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado."));

        TipoUsuarioEnum novoTipo;
        try {
            novoTipo = TipoUsuarioEnum.fromDescricao(inputDto.getNome());
        } catch (RuntimeException e) {
            throw new RegraNegocioException("Tipo de usuário inválido: " + inputDto.getNome());
        }

        var existente = tipoUsuarioGateway.buscarPorTipo(novoTipo);
        if (existente.isPresent() && !existente.get().getId().equals(inputDto.getId())) {
            throw new RegraNegocioException("Já existe outro tipo de usuário cadastrado com esse valor.");
        }

        tipoUsuario.atualizarTipo(novoTipo);
        TipoUsuario atualizado = tipoUsuarioGateway.atualizar(tipoUsuario);

        return TipoUsuarioOutputDto.fromDomain(atualizado);
    }
}
