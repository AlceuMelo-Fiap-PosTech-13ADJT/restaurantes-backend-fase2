package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CriarRestauranteUsecaseImpl implements CriarRestauranteUsecase {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;

    @Override
    public CriarRestauranteOutputDto executar(CriarRestauranteInputDto inputDto) {
        if (inputDto == null) {
            throw new RegraNegocioException("Dados do restaurante são obrigatórios.");
        }
        if (inputDto.getDonoId() == null) {
            throw new RegraNegocioException("Restaurante deve possuir um dono associado.");
        }

        Usuario usuarioDono = usuarioGateway.buscarPorId(inputDto.getDonoId())
                .orElseThrow(() -> new DonoRestauranteInvalidoException("Usuário informado não existe."));

        if (!usuarioDono.isAtivo()) {
            throw new DonoRestauranteInvalidoException("Usuário informado está inativo.");
        }

        if (!usuarioDono.isDonoRestaurante()) {
            throw new DonoRestauranteInvalidoException("O usuário informado não possui o perfil de 'Dono de restaurante'.");
        }

        Restaurante restaurante = Restaurante.builder()
                .nome(inputDto.getNome())
                .endereco(inputDto.getEndereco())
                .tipoCozinha(inputDto.getTipoCozinha())
                .horarioFuncionamento(inputDto.getHorarioFuncionamento())
                .dono(usuarioDono)
                .ativo(true)
                .build();

        restaurante.validarParaCriacao();
        Restaurante salvo = restauranteGateway.salvar(restaurante);

        return new CriarRestauranteOutputDto(salvo != null ? salvo.getId() : null);
    }
}
