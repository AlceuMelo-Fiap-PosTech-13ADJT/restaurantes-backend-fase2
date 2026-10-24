package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restaurante {

    private UUID id;
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    private List<Usuario> usuarios;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public void validarParaCriacao() {
        if (this.nome == null || this.nome.isBlank()) {
            throw new RegraNegocioException("Nome do restaurante é obrigatório.");
        }
        if (this.usuarios == null || this.usuarios.isEmpty()) {
            throw new RegraNegocioException("Restaurante deve possuir pelo menos um usuário associado.");
        }
        if (this.usuarios.stream().noneMatch(Usuario::isDonoRestaurante)) {
            throw new RegraNegocioException(
                    "Restaurante deve possuir pelo menos um usuário do tipo DONO_RESTAURANTE associado.");
        }
        if (this.endereco == null || this.endereco.isBlank()) {
            throw new RegraNegocioException("Endereço do restaurante é obrigatório.");
        }
        if (this.tipoCozinha == null || this.tipoCozinha.isBlank()) {
            throw new RegraNegocioException("Tipo de cozinha é obrigatório.");
        }
        if (this.horarioFuncionamento == null || this.horarioFuncionamento.isBlank()) {
            throw new RegraNegocioException("Horário de funcionamento é obrigatório.");
        }
    }

    public void validarParaAtualizacao() {
        if (this.nome == null || this.nome.isBlank()) {
            throw new RegraNegocioException("Nome do restaurante é obrigatório.");
        }
        if (this.endereco == null || this.endereco.isBlank()) {
            throw new RegraNegocioException("Endereço do restaurante é obrigatório.");
        }
        if (this.tipoCozinha == null || this.tipoCozinha.isBlank()) {
            throw new RegraNegocioException("Tipo de cozinha é obrigatório.");
        }
        if (this.horarioFuncionamento == null || this.horarioFuncionamento.isBlank()) {
            throw new RegraNegocioException("Horário de funcionamento é obrigatório.");
        }
    }

    public void atualizarDados(String nome, String endereco, String tipoCozinha, String horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
        validarParaAtualizacao();
        this.dataAlteracao = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
        this.dataAlteracao = LocalDateTime.now();
    }

    public void validarDono(UUID operadorId) {
        if (operadorId == null || this.usuarios == null || this.usuarios.isEmpty() ||
                this.usuarios.stream().noneMatch(
                        usuario -> usuario.isDonoRestaurante() &&
                                Objects.equals(usuario.getId(), operadorId))) {
            throw new DonoRestauranteInvalidoException("Operador não é o dono deste restaurante.");
        }
    }

}
