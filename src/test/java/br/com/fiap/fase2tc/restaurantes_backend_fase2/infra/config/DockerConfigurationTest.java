package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Configuracao de Conteinerizacao: Dockerfile e docker-compose.yml")
class DockerConfigurationTest {

    @Test
    @DisplayName("Deve conter Dockerfile multi-stage configurado com Java 21 e usuario nao-root")
    void deveValidarEstruturaDoDockerfile() throws Exception {
        Path pathDockerfile = Path.of("Dockerfile");
        assertThat(Files.exists(pathDockerfile)).isTrue();

        String conteudo = Files.readString(pathDockerfile);
        assertThat(conteudo).contains("FROM maven:3.9-eclipse-temurin-21-alpine AS builder");
        assertThat(conteudo).contains("FROM eclipse-temurin:21-jre-alpine");
        assertThat(conteudo).contains("adduser -S appuser");
        assertThat(conteudo).contains("USER appuser");
        assertThat(conteudo).contains("ENV SPRING_PROFILES_ACTIVE=prod");
        assertThat(conteudo).contains("EXPOSE 8080");
        assertThat(conteudo).contains("ENTRYPOINT");
    }

    @Test
    @DisplayName("Deve conter docker-compose.yml com servicos mysql 8 e app com healthcheck e dependencias")
    void deveValidarEstruturaDoDockerCompose() throws Exception {
        Path pathCompose = Path.of("docker-compose.yml");
        assertThat(Files.exists(pathCompose)).isTrue();

        String conteudo = Files.readString(pathCompose);
        assertThat(conteudo).contains("mysql:8.0");
        assertThat(conteudo).contains("3306:3306");
        assertThat(conteudo).contains("mysqladmin");
        assertThat(conteudo).contains("ping");
        assertThat(conteudo).contains("./infra/scripts/init.sql:/docker-entrypoint-initdb.d/init.sql:ro");
        assertThat(conteudo).contains("8080:8080");
        assertThat(conteudo).contains("condition: service_healthy");
        assertThat(conteudo).contains("SPRING_PROFILES_ACTIVE: prod");
    }
}
