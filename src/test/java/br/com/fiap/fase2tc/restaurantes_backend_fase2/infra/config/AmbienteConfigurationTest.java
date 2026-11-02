package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

@DisplayName("Testes de Configuração de Ambientes e Multi-Banco (H2 e MySQL)")
class AmbienteConfigurationTest {

    @Test
    @DisplayName("Deve validar as propriedades do perfil dev em application.yml")
    void deveValidarPropriedadesDevEmApplicationYml() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties props = yaml.getObject();

        assertThat(props).isNotNull();
        assertThat(props.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:h2:mem:restaurantes_db;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE");
        assertThat(props.getProperty("spring.datasource.driver-class-name"))
                .isEqualTo("org.h2.Driver");
        assertThat(props.getProperty("spring.h2.console.enabled"))
                .isEqualTo("true");
        assertThat(props.getProperty("spring.h2.console.path"))
                .isEqualTo("/h2-console");
        assertThat(props.getProperty("spring.jpa.hibernate.ddl-auto"))
                .isEqualTo("create-drop");
        assertThat(props.getProperty("spring.jpa.defer-datasource-initialization"))
                .isEqualTo("true");
        assertThat(props.getProperty("spring.jpa.database-platform"))
                .isEqualTo("org.hibernate.dialect.H2Dialect");
    }

    @Test
    @DisplayName("Deve validar as propriedades do perfil prod em application-prod.yml")
    void deveValidarPropriedadesProdEmApplicationProdYml() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application-prod.yml"));
        Properties props = yaml.getObject();

        assertThat(props).isNotNull();
        assertThat(props.getProperty("spring.datasource.url"))
                .contains("jdbc:mysql://mysql:3306/restaurantes_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        assertThat(props.getProperty("spring.jpa.database-platform"))
                .isEqualTo("org.hibernate.dialect.MySQLDialect");
        assertThat(props.getProperty("spring.jpa.hibernate.ddl-auto"))
                .isEqualTo("update");
    }
}
