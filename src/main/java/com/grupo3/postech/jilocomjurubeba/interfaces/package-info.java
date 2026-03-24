/**
 * Camada de Interfaces - Adaptadores de Entrada.
 *
 * <p>Esta camada contém os adaptadores que recebem requisições externas e as convertem para
 * chamadas aos casos de uso.
 *
 * <p>Componentes: - rest/: Controllers REST (endpoints HTTP) - rest/dto/: Request/Response DTOs
 * específicos da API - rest/mapper/: Conversores entre DTOs REST e DTOs de Application -
 * rest/handler/: Tratamento centralizado de erros
 *
 * <p>REGRAS IMPORTANTES: 1. Pode depender de application e domain 2. NÃO pode depender de
 * infrastructure 3. Controllers devem ser finos (apenas orquestração) 4. Validações de entrada com
 * Bean Validation (@Valid) 5. Documentação OpenAPI em cada endpoint
 *
 * @author Danilo Fernando
 * @see com.grupo3.postech.jilocomjurubeba.interfaces.rest Controllers REST
 * @see com.grupo3.postech.jilocomjurubeba.interfaces.rest.handler Tratamento de erros
 */
package com.grupo3.postech.jilocomjurubeba.interfaces;
