/**
 * Configurações Spring.
 *
 * <p>Classes @Configuration para setup do framework.
 *
 * <p>Configurações esperadas: - OpenApiConfig: Configuração do Swagger/OpenAPI - JpaConfig:
 * Configuração do JPA - MongoConfig: Configuração do MongoDB (quando ativo) - UseCaseConfig:
 * Registro de beans de casos de uso - SecurityConfig: Configuração de segurança
 *
 * <p>Importante: - Separar configurações por responsabilidade - Usar @Profile para configurações
 * específicas por ambiente
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.infrastructure.config;
