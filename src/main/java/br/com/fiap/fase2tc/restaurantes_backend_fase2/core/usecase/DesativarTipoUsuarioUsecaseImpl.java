package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesativarTipoUsuarioUsecaseImpl implements DesativarTipoUsuarioUsecase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    @Override
    public void executar(UUID id) {
        if (id == null) {
            throw new RegraNegocioException("ID do tipo de usuário é obrigatório.");
        }

        TipoUsuario tipoUsuario = tipoUsuarioGateway.buscarAtivoPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado."));

        tipoUsuario.desativar();
        tipoUsuarioGateway.desativar(id);
    }
}
