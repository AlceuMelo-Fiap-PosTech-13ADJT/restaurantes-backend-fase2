package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurantes API - Fase 2")
                        .version("1.0.0")
                        .description("API REST com Clean Architecture para gestão de restaurantes, cardápios e tipos de usuário."));
    }

    @Bean
    public OperationCustomizer customizarHeaderUsuarioLogado() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(
                    new Parameter()
                            .name("usuario-logado-id")
                            .description("UUID do usuário logado realizando a operação (controle de acesso e tenant)")
                            .required(true)
                            .in("header")
                            .schema(new StringSchema().format("uuid").example("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"))
            );
            return operation;
        };
    }
}
