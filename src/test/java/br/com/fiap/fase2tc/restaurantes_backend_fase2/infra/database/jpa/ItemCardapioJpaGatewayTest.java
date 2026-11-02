package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.ItemCardapioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.ItemCardapioMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.ItemCardapioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários: ItemCardapioJpaGateway")
class ItemCardapioJpaGatewayTest {

    @Mock
    private ItemCardapioRepository repository;

    @Mock
    private ItemCardapioMapper mapper;

    @InjectMocks
    private ItemCardapioJpaGateway gateway;

    @Test
    @DisplayName("Deve salvar item do cardápio com sucesso")
    void deveSalvarComSucesso() {
        UUID restId = UUID.randomUUID();
        ItemCardapio domain = ItemCardapio.builder().nome("Pizza").preco(BigDecimal.TEN).build();
        ItemCardapioEntity entity = ItemCardapioEntity.builder().nome("Pizza").preco(BigDecimal.TEN).build();
        ItemCardapioEntity salvo = ItemCardapioEntity.builder().id(UUID.randomUUID()).nome("Pizza").build();
        ItemCardapio domainSalvo = ItemCardapio.builder().id(salvo.getId()).nome("Pizza").build();

        when(mapper.toEntity(restId, domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(salvo);
        when(mapper.toDomain(salvo)).thenReturn(domainSalvo);

        ItemCardapio resultado = gateway.salvar(restId, domain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(salvo.getId());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve atualizar item do cardápio com sucesso")
    void deveAtualizarComSucesso() {
        UUID restId = UUID.randomUUID();
        ItemCardapio domain = ItemCardapio.builder().id(UUID.randomUUID()).nome("Burger").build();
        ItemCardapioEntity entity = ItemCardapioEntity.builder().id(domain.getId()).nome("Burger").build();

        when(mapper.toEntity(restId, domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        ItemCardapio resultado = gateway.atualizar(restId, domain);

        assertThat(resultado).isNotNull();
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve buscar por ID e buscar ativo por ID com sucesso")
    void deveBuscarPorId() {
        UUID restId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        ItemCardapioEntity entity = ItemCardapioEntity.builder().id(id).restauranteId(restId).build();
        ItemCardapio domain = ItemCardapio.builder().id(id).build();

        when(repository.findByIdAndRestauranteId(id, restId)).thenReturn(Optional.of(entity));
        when(repository.findByIdAndRestauranteIdAndAtivoTrue(id, restId)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        assertThat(gateway.buscarPorId(restId, id)).isPresent();
        assertThat(gateway.buscarAtivoPorId(restId, id)).isPresent();
    }

    @Test
    @DisplayName("Deve listar por restaurante e listar ativos por restaurante")
    void deveListarPorRestaurante() {
        UUID restId = UUID.randomUUID();
        ItemCardapioEntity entity = ItemCardapioEntity.builder().build();
        ItemCardapio domain = ItemCardapio.builder().build();

        when(repository.findByRestauranteId(restId)).thenReturn(List.of(entity));
        when(repository.findByRestauranteIdAndAtivoTrue(restId)).thenReturn(List.of(entity));
        when(mapper.toDomainList(any())).thenReturn(List.of(domain));

        assertThat(gateway.listarPorRestaurante(restId)).hasSize(1);
        assertThat(gateway.listarAtivosPorRestaurante(restId)).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar por nome e restaurante")
    void deveBuscarPorNomeERestaurante() {
        UUID restId = UUID.randomUUID();
        ItemCardapioEntity entity = ItemCardapioEntity.builder().build();
        ItemCardapio domain = ItemCardapio.builder().build();

        when(repository.findByRestauranteIdAndNomeContainingIgnoreCaseAndAtivoTrue(restId, "Pizza"))
                .thenReturn(List.of(entity));
        when(mapper.toDomainList(any())).thenReturn(List.of(domain));

        assertThat(gateway.buscarPorNomeERestaurante(restId, "Pizza")).hasSize(1);
    }

    @Test
    @DisplayName("Deve verificar existência por ID e restaurante")
    void deveVerificarExistencia() {
        UUID restId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(repository.existsByIdAndRestauranteIdAndAtivoTrue(id, restId)).thenReturn(true);

        assertThat(gateway.existePorIdERestaurante(restId, id)).isTrue();
    }

    @Test
    @DisplayName("Deve desativar item com sucesso")
    void deveDesativar() {
        UUID restId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        ItemCardapioEntity entity = ItemCardapioEntity.builder().id(id).restauranteId(restId).ativo(true).build();

        when(repository.findByIdAndRestauranteId(id, restId)).thenReturn(Optional.of(entity));

        gateway.desativar(restId, id);

        assertThat(entity.getAtivo()).isFalse();
        verify(repository).save(entity);
    }
}
