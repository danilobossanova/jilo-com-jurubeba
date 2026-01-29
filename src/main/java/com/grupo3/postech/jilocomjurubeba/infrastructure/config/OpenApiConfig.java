package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuração do OpenAPI (Swagger).
 *
 * <p>Define metadados da API para documentação automática. Acessível em: /swagger-ui.html
 *
 * @author Danilo Fernando
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:jilo-com-jurubeba}")
    private String nomeAplicacao;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(nomeAplicacao)
                                .version("1.0.0")
                                .description(
                                        """
                                Sistema de Gestão de Restaurantes - Postech Fase 2 - 11ADJT

                                ## Arquitetura
                                Esta API segue os princípios de Clean Architecture, com separação clara entre:
                                - **Domain**: Regras de negócio
                                - **Application**: Casos de uso
                                - **Interfaces**: Controllers REST
                                - **Infrastructure**: Persistência e configurações

                                ## Padrões de Resposta
                                - Sucesso: Retorna o recurso solicitado
                                - Erro: Retorna ProblemDetail (RFC 7807)
                                """)
                                .contact(
                                        new Contact()
                                                .name("Grupo 3 - Postech")
                                                .email("contato@grupo3.postech.com.br"))
                                .license(
                                        new License()
                                                .name("Proprietário")
                                                .url("https://grupo3.postech.com.br")))
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:8080")
                                        .description("Ambiente de Desenvolvimento"),
                                new Server()
                                        .url("https://api.jilo-com-jurubeba.com.br")
                                        .description("Ambiente de Produção")));
    }
}
