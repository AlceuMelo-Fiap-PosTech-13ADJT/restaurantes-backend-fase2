package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<ItemCardapio> itensCardapio;
    private String horarioFuncionamento;
    private List<Usuario> usuarios;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public void validarParaCriacao() {
        if (this.nome == null || this.nome.isBlank()) {
            throw new RegraNegocioException("Nome do restaurante é obrigatório.");
        }
        if (this.itensCardapio == null || this.itensCardapio.isEmpty()) {
            throw new RegraNegocioException("Restaurante deve possuir pelo menos um item de cardápio associado.");
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

    public void adicionaItemCardapio(ItemCardapio itemCardapio) {
        if (itemCardapio == null) {
            throw new RegraNegocioException("Item de cardápio é obrigatório.");
        }
        if (this.itensCardapio == null) {
            this.itensCardapio = new ArrayList<>();
        }
        if (this.itensCardapio.stream().anyMatch(item -> Objects.equals(itemCardapio.getId(), item.getId()))) {
            throw new RegraNegocioException("Item de cardápio já existe.");
        }
        this.itensCardapio.add(itemCardapio);
        this.dataAlteracao = LocalDateTime.now();
    }

    public void removeItemCardapio(UUID itemCardapioId) {
        if (itemCardapioId == null ||
                !this.itensCardapio.removeIf(item -> Objects.equals(item.getId(), itemCardapioId))) {
            throw new RegraNegocioException("Item de cardápio não encontrado.");
        }
        this.dataAlteracao = LocalDateTime.now();
    }

    public void adicionaUsuario(Usuario usuarioNovo) {
        if (usuarioNovo == null) {
            throw new RegraNegocioException("Usuário é obrigatório.");
        }
        if (this.usuarios == null) {
            this.usuarios = new ArrayList<>();
        }
        if (this.usuarios.stream().anyMatch(usuario -> Objects.equals(usuarioNovo.getId(), usuario.getId()))) {
            throw new RegraNegocioException("Usuário já existe.");
        }
        this.usuarios.add(usuarioNovo);
        this.dataAlteracao = LocalDateTime.now();
    }

    public void removeUsuario(UUID usuarioId) {
        if (usuarioId == null ||
                !this.usuarios.removeIf(usuario -> Objects.equals(usuario.getId(), usuarioId))) {
            throw new RegraNegocioException("Usuario não encontrado.");
        }
        this.dataAlteracao = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
        this.dataAlteracao = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return this.ativo != null && Boolean.TRUE.equals(this.ativo);
    }

    public void validarAtivo() {
        if (this.ativo == null || Boolean.FALSE.equals(this.ativo)) {
            throw new RegraNegocioException("Restaurante inativo.");
        }
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
