package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;

@SpringBootTest
@Transactional
@DisplayName("Testes de Repositório JPA: TipoUsuarioRepository")
class TipoUsuarioRepositoryTest {

    @Autowired
    private TipoUsuarioRepository repository;

    @Test
    @DisplayName("Deve salvar e buscar TipoUsuario por tipo e status ativo")
    void deveSalvarEBuscarPorTipo() {
        Optional<TipoUsuarioEntity> busca = repository.findByTipoAndAtivoTrue(TipoUsuarioEnum.DONO_RESTAURANTE);
        assertThat(busca).isPresent();
        assertThat(busca.get().getId()).isNotNull();
        assertThat(busca.get().getDataCriacao()).isNotNull();
        assertThat(busca.get().getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);

        boolean existe = repository.existsByTipoAndAtivoTrue(TipoUsuarioEnum.DONO_RESTAURANTE);
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve buscar por nome ignorando maiúsculas e minúsculas")
    void deveBuscarPorNome() {
        Optional<TipoUsuarioEntity> busca = repository.findByNomeIgnoreCaseAndAtivoTrue("cLIENTe");
        assertThat(busca).isPresent();
        assertThat(busca.get().getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
    }

    @Test
    @DisplayName("Deve salvar e atualizar status ativo de TipoUsuario")
    void deveSalvarEAtualizarTipoUsuario() {
        var entity = TipoUsuarioEntity.builder()
                .tipo(TipoUsuarioEnum.CLIENTE)
                .nome("Cliente Temporário")
                .ativo(false)
                .build();

        var salvo = repository.save(entity);
        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getAtivo()).isFalse();
    }
}
