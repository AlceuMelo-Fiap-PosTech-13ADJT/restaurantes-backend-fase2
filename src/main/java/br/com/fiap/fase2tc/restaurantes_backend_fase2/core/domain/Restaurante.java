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
    private Usuario dono;
    private List<Usuario> clientes;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public void validarParaCriacao() {
        if (this.nome == null || this.nome.isBlank()) {
            throw new RegraNegocioException("Nome do restaurante é obrigatório.");
        }
        if (this.dono == null || !this.dono.isDonoRestaurante()) {
            throw new RegraNegocioException(
                    "Restaurante deve possuir um dono associado.");
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

    public void adicionaCliente(Usuario clienteNovo) {
        if (clienteNovo == null) {
            throw new RegraNegocioException("Cliente é obrigatório.");
        }
        if (this.clientes == null) {
            this.clientes = new ArrayList<>();
        }
        if (this.clientes.stream().anyMatch(cliente -> Objects.equals(clienteNovo.getId(), cliente.getId()))) {
            throw new RegraNegocioException("Cliente já está associado ao restaurante.");
        }
        this.clientes.add(clienteNovo);
        this.dataAlteracao = LocalDateTime.now();
    }

    public void removeCliente(UUID clienteId) {
        if (clienteId == null ||
                !this.clientes.removeIf(usuario -> Objects.equals(usuario.getId(), clienteId))) {
            throw new RegraNegocioException("Cliente não encontrado.");
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
        if (operadorId == null || this.dono == null || this.dono.getId() == null ||
                !Objects.equals(this.dono.getId(), operadorId)) {
            throw new DonoRestauranteInvalidoException("Operador não é o dono deste restaurante.");
        }
    }

}
