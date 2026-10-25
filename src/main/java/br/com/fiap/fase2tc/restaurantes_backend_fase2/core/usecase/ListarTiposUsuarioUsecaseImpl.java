package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.List;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarTiposUsuarioUsecaseImpl implements ListarTiposUsuarioUsecase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    @Override
    public List<TipoUsuarioOutputDto> executar() {
        return tipoUsuarioGateway.listarAtivos()
                .stream()
                .map(TipoUsuarioOutputDto::fromDomain)
                .toList();
    }
}
