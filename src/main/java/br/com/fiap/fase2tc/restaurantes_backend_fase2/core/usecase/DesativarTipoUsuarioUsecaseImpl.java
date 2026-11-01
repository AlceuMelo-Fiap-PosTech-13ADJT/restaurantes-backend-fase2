package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesativarTipoUsuarioUsecaseImpl implements DesativarTipoUsuarioUsecase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public void executar(UUID id) {
        executar(id, null);
    }

    @Override
    public void executar(UUID id, UUID usuarioOperadorId) {
        if (id == null) {
            throw new RegraNegocioException("ID do tipo de usuário é obrigatório.");
        }

        if (usuarioOperadorId != null) {
            validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                    .usuarioOperadorId(usuarioOperadorId)
                    .requerDono(true)
                    .build());
        }

        TipoUsuario tipoUsuario = tipoUsuarioGateway.buscarAtivoPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado."));

        tipoUsuario.desativar();
        tipoUsuarioGateway.desativar(id);
    }
}
