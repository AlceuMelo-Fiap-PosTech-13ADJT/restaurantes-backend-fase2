package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarUsuarioPorIdUsecaseImpl implements BuscarUsuarioPorIdUsecase {

    private final UsuarioGateway usuarioGateway;

    @Override
    public UsuarioOutputDto executar(UUID id) {
        if (id == null) {
            throw new RegraNegocioException("ID do usuário é obrigatório.");
        }

        Usuario usuario = usuarioGateway.buscarAtivoPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        return UsuarioOutputDto.fromDomain(usuario);
    }
}
