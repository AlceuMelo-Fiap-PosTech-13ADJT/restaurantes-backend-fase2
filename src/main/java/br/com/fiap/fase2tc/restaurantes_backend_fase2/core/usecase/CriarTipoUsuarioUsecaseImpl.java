package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CriarTipoUsuarioUsecaseImpl implements CriarTipoUsuarioUsecase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    @Override
    public TipoUsuarioOutputDto executar(CriarTipoUsuarioInputDto inputDto) {
        if (inputDto == null || inputDto.getNome() == null || inputDto.getNome().isBlank()) {
            throw new RegraNegocioException("Nome do tipo de usuário é obrigatório.");
        }

        TipoUsuarioEnum tipoEnum;
        try {
            tipoEnum = TipoUsuarioEnum.fromDescricao(inputDto.getNome());
        } catch (RuntimeException e) {
            throw new RegraNegocioException("Tipo de usuário inválido: " + inputDto.getNome());
        }

        if (tipoUsuarioGateway.existePorTipo(tipoEnum)) {
            throw new RegraNegocioException("Tipo de usuário já cadastrado.");
        }

        TipoUsuario tipoUsuario = TipoUsuario.builder()
                .tipo(tipoEnum)
                .ativo(true)
                .build();

        tipoUsuario.validarParaCriacao();
        TipoUsuario salvo = tipoUsuarioGateway.salvar(tipoUsuario);

        return TipoUsuarioOutputDto.fromDomain(salvo);
    }
}
