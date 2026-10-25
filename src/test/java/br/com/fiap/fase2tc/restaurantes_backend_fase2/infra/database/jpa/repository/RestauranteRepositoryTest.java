package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.RestauranteEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;

@SpringBootTest
@Transactional
@DisplayName("Testes de Repositório JPA: RestauranteRepository")
class RestauranteRepositoryTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Test
    @DisplayName("Deve salvar restaurante e buscar por dono e filtros")
    void deveSalvarEBuscarRestaurante() {
        var tipo = tipoUsuarioRepository.findByTipoAndAtivoTrue(TipoUsuarioEnum.DONO_RESTAURANTE)
                .orElseGet(() -> tipoUsuarioRepository.save(TipoUsuarioEntity.builder()
                        .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                        .nome("Dono de restaurante")
                        .ativo(true)
                        .build()));

        var dono = usuarioRepository.save(UsuarioEntity.builder()
                .nome("Chef Luigi")
                .email("luigi@trattoria.com")
                .tipoUsuario(tipo)
                .ativo(true)
                .build());

        var restaurante = restauranteRepository.save(RestauranteEntity.builder()
                .nome("Bistrô Parisien")
                .endereco("Av. Paulista, 1000")
                .tipoCozinha("Francesa")
                .horarioFuncionamento("11h às 23h")
                .dono(dono)
                .ativo(true)
                .build());

        assertThat(restaurante.getId()).isNotNull();

        Optional<RestauranteEntity> porId = restauranteRepository.findByIdAndAtivoTrue(restaurante.getId());
        assertThat(porId).isPresent();
        assertThat(porId.get().getNome()).isEqualTo("Bistrô Parisien");

        List<RestauranteEntity> porDono = restauranteRepository.findByDono_IdAndAtivoTrue(dono.getId());
        assertThat(porDono).hasSize(1);

        List<RestauranteEntity> porCozinha = restauranteRepository.findByTipoCozinhaIgnoreCaseAndAtivoTrue("francesa");
        assertThat(porCozinha).hasSize(1);

        boolean existeDono = restauranteRepository.existsByDono_IdAndAtivoTrue(dono.getId());
        assertThat(existeDono).isTrue();
    }
}
