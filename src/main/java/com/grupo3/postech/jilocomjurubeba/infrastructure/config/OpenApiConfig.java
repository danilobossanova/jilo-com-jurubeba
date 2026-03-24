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
 * Configuracao do OpenAPI (Swagger).
 *
 * <p>Define metadados da API para documentacao automatica, incluindo titulo, versao, descricao,
 * contato, licenca e servidores disponiveis. Acessivel em: /swagger-ui.html
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:jilo-com-jurubeba}")
    private String nomeAplicacao;

    /**
     * Cria o bean de configuracao customizada do OpenAPI.
     *
     * <p>Configura informacoes gerais da API como titulo (obtido da propriedade {@code
     * spring.application.name}), versao, descricao da arquitetura, dados de contato, licenca e os
     * servidores de desenvolvimento e producao.
     *
     * @return instancia configurada de {@link OpenAPI}
     */
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
