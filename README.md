# jilo-com-jurubeba

### Tech Challenge - Fase 2 | Pos-Tech Arquitetura e Desenvolvimento Java (FIAP)

**jilo-com-jurubeba** e um sistema de gestao de restaurantes desenvolvido como projeto academico do Tech Challenge FIAP. O sistema permite que donos gerenciem seus restaurantes e cardapios, enquanto clientes consultam informacoes. Desenvolvido com **Clean Architecture** em 4 camadas, autenticacao JWT e cobertura de testes acima de 80%.

---

## Video de Apresentacao

O video com a demonstracao do projeto executando e funcionando esta disponivel no repositorio:

[Assistir video de apresentacao (13-42-16.mp4)](13-42-16.mp4)

---

## Indice

- [Objetivo da Fase 2](#objetivo-da-fase-2)
- [Arquitetura do Projeto](#arquitetura-do-projeto)
- [Estrutura de Pacotes](#estrutura-de-pacotes)
- [Tecnologias](#tecnologias)
- [Pre-requisitos](#pre-requisitos)
- [Como Executar](#como-executar)
- [Endpoints da API](#endpoints-da-api)
- [Postman Collection](#postman-collection)
- [Testes](#testes)
- [Docker](#docker)
- [Regras de Negocio](#regras-de-negocio)
- [Autores](#autores)

---

## Objetivo da Fase 2

Expandir o sistema com:
- **CRUD de Tipo de Usuario** (MASTER, DONO_RESTAURANTE, CLIENTE)
- **CRUD de Restaurante** (nome, endereco, tipo de cozinha, horario, dono)
- **CRUD de Item de Cardapio** (nome, descricao, preco, disponibilidade local, foto)
- **Clean Architecture** com separacao rigorosa em 4 camadas
- **Testes automatizados** com cobertura minima de 80%
- **Docker Compose** para execucao integrada
- **Documentacao completa** da API (Swagger + Postman)

---

## Arquitetura do Projeto

O projeto segue os principios da **Clean Architecture** (Robert C. Martin), com 4 camadas:

```
+-----------------------------------------------------------+
|                      INTERFACES                           |
|       (Controllers REST, Request/Response DTOs, Mappers)  |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                      APPLICATION                          |
|             (Use Cases, Input/Output DTOs)                |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                        DOMAIN                             |
|    (Entities, Value Objects, Gateway Interfaces, Exceptions)|
+-----------------------------------------------------------+
                            ^
                            |
+-----------------------------------------------------------+
|                    INFRASTRUCTURE                         |
|      (JPA, Repositories, Spring Security, Config)         |
+-----------------------------------------------------------+
```

### Regras de Dependencia (validadas por ArchUnit com 35 testes)

| Camada         | Pode depender de           | NAO pode depender de                    |
|----------------|----------------------------|-----------------------------------------|
| Domain         | Nada (nucleo puro)         | Application, Interfaces, Infrastructure |
| Application    | Domain                     | Interfaces, Infrastructure              |
| Interfaces     | Application, Domain        | Infrastructure                          |
| Infrastructure | Application, Domain        | Interfaces                              |

---

## Estrutura de Pacotes

```
com.grupo3.postech.jilocomjurubeba
|-- domain/                         # Camada de Dominio (POJO puro, sem framework)
|   |-- entity/
|   |   |-- tipousuario/            # TipoUsuario.java
|   |   |-- usuario/                # Usuario.java
|   |   |-- restaurante/            # Restaurante.java
|   |   +-- cardapio/               # Cardapio.java
|   |-- valueobject/                # Cpf, Email, TipoCozinha (enum)
|   |-- gateway/                    # Interfaces (ports): TipoUsuarioGateway, UsuarioGateway, etc.
|   +-- exception/                  # DominioException, ValidacaoException, EntidadeNaoEncontradaException
|
|-- application/                    # Camada de Aplicacao (sem Spring/JPA)
|   |-- usecase/                    # 21 UseCases organizados por entidade + autenticacao + saude
|   +-- dto/                        # Records puros: CriarXInput, AtualizarXInput, XOutput
|
|-- interfaces/                     # Camada de Interfaces (REST adapters)
|   +-- rest/
|       |-- {entidade}/             # Controllers (TipoUsuario, Usuario, Restaurante, Cardapio, Auth)
|       |-- dto/{entidade}/         # Request/Response DTOs com @Schema, @NotBlank, @Valid
|       |-- mapper/{entidade}/      # RestMappers com MapStruct
|       +-- handler/                # GlobalExceptionHandler (RFC 7807 ProblemDetail)
|
+-- infrastructure/                 # Camada de Infraestrutura (framework adapters)
    |-- persistence/{entidade}/     # JpaEntity, Repository, PersistenceMapper (MapStruct), GatewayJpa
    |-- config/                     # UseCaseConfig, OpenApiConfig, TipoUsuarioDataSeeder
    +-- security/                   # SecurityConfig, JwtService, JwtAuthFilter
```

---

## Tecnologias

| Tecnologia       | Versao   | Proposito                          |
|------------------|----------|------------------------------------|
| Java             | 21       | Linguagem principal                |
| Spring Boot      | 3.5.10   | Framework web                      |
| Spring Security  | 6.x      | Autenticacao JWT                   |
| Spring Data JPA  | 3.x      | Persistencia de dados              |
| MySQL            | 8.0      | Banco de dados (Docker)            |
| H2               | 2.x      | Banco em memoria (dev/testes)      |
| MapStruct        | 1.5.5    | Mapeamento de objetos (compile-time)|
| Lombok           | 1.18.32  | Reducao de boilerplate             |
| SpringDoc OpenAPI| 2.5.0    | Documentacao da API (Swagger)      |
| ArchUnit         | 1.2.1    | Validacao de arquitetura (35 testes)|
| JaCoCo           | 0.8.12   | Cobertura de testes (min 80%)      |
| Spotless         | 2.43.0   | Formatacao automatica (AOSP)       |
| Docker           | 24+      | Containerizacao                    |

---

## Pre-requisitos

- **Java 21** ou superior
- **Maven 3.9+** (ou use o Maven Wrapper incluso: `./mvnw`)
- **Docker** e **Docker Compose** (apenas para execucao com MySQL)

---

## Como Executar

### Opcao 1: Desenvolvimento Local (H2 em memoria - mais rapido)

```bash
# Clonar o repositorio
git clone https://github.com/danilobossanova/jilo-com-jurubeba.git
cd jilo-com-jurubeba

# Executar (H2 em memoria, sem Docker)
./mvnw spring-boot:run
```

A aplicacao estara disponivel em: **http://localhost:8080**

- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, sem senha)

### Opcao 2: Docker Compose (API + MySQL)

```bash
# Clonar e subir a stack completa
git clone https://github.com/danilobossanova/jilo-com-jurubeba.git
cd jilo-com-jurubeba

# Build e execucao
docker-compose up --build -d

# Verificar se subiu
docker-compose ps

# Ver logs da API
docker-compose logs -f api

# Parar
docker-compose down
```

A API estara em **http://localhost:8080** e o MySQL em **localhost:3307**.

### Opcao 3: Profile dev (MySQL local instalado)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Requer MySQL rodando em `localhost:3306` com banco `jilocomjurubeba`.

---

## Endpoints da API

### Autenticacao

| Metodo | Endpoint        | Descricao           | Auth |
|--------|-----------------|---------------------|------|
| POST   | /auth/login     | Login (retorna JWT) | Nao  |

**Exemplo de login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@jilocomjurubeba.com", "senha": "admin123"}'
```

Use o token retornado no header: `Authorization: Bearer <token>`

### Saude

| Metodo | Endpoint         | Descricao       | Auth |
|--------|------------------|-----------------|------|
| GET    | /v1/health       | Health check    | Nao  |
| GET    | /actuator/health | Health Spring   | Nao  |

### Tipo de Usuario (CRUD)

| Metodo | Endpoint                   | Descricao              | Auth     |
|--------|----------------------------|------------------------|----------|
| POST   | /v1/tipos-usuario          | Criar tipo             | Sim (JWT)|
| GET    | /v1/tipos-usuario          | Listar todos           | Sim (JWT)|
| GET    | /v1/tipos-usuario/{id}     | Buscar por ID          | Sim (JWT)|
| PUT    | /v1/tipos-usuario/{id}     | Atualizar              | Sim (JWT)|
| DELETE | /v1/tipos-usuario/{id}     | Desativar (soft delete)| Sim (JWT)|

### Usuario (CRUD)

| Metodo | Endpoint             | Descricao              | Auth     |
|--------|----------------------|------------------------|----------|
| POST   | /v1/usuarios         | Criar usuario          | Sim (JWT)|
| GET    | /v1/usuarios         | Listar todos           | Sim (JWT)|
| GET    | /v1/usuarios/{id}    | Buscar por ID          | Sim (JWT)|
| PUT    | /v1/usuarios/{id}    | Atualizar              | Sim (JWT)|
| DELETE | /v1/usuarios/{id}    | Desativar (soft delete)| Sim (JWT)|

### Restaurante (CRUD)

| Metodo | Endpoint                          | Descricao                  | Auth     |
|--------|-----------------------------------|----------------------------|----------|
| POST   | /v1/restaurantes                  | Criar restaurante          | Sim (JWT)|
| GET    | /v1/restaurantes                  | Listar todos               | Sim (JWT)|
| GET    | /v1/restaurantes/{id}             | Buscar por ID              | Sim (JWT)|
| PUT    | /v1/restaurantes/{id}?donoId={id} | Atualizar (somente o dono) | Sim (JWT)|
| DELETE | /v1/restaurantes/{id}             | Desativar (soft delete)    | Sim (JWT)|

### Cardapio - Itens do Menu (CRUD)

| Metodo | Endpoint                        | Descricao                          | Auth     |
|--------|---------------------------------|------------------------------------|----------|
| POST   | /v1/cardapios                   | Criar item                         | Sim (JWT)|
| GET    | /v1/cardapios                   | Listar todos                       | Sim (JWT)|
| GET    | /v1/cardapios/{id}              | Buscar por ID                      | Sim (JWT)|
| PUT    | /v1/cardapios/{id}?donoId={id}  | Atualizar (somente dono do rest.)  | Sim (JWT)|
| DELETE | /v1/cardapios/{id}              | Desativar (soft delete)            | Sim (JWT)|

### Swagger UI

Documentacao interativa completa disponivel em: **http://localhost:8080/swagger-ui.html**

---

## Postman Collection

Uma collection completa esta disponivel para importacao:

1. Abra o Postman
2. Importe o arquivo: `docs/postman/Jilo-com-Jurubeba-API.postman_collection.json`
3. Configure a variavel `baseUrl` para `http://localhost:8080`
4. Execute primeiro o request **"Login"** - o token JWT sera salvo automaticamente
5. Todos os demais requests usarao o token automaticamente

---

## Testes

O projeto possui **193 testes** cobrindo todas as camadas:

| Tipo               | Camada           | Ferramentas                | Qtd   |
|--------------------|------------------|----------------------------|-------|
| Unitario           | Domain           | JUnit 5, AssertJ           | ~40   |
| Unitario           | Application      | JUnit 5, Mockito           | ~60   |
| Unitario           | DTOs             | JUnit 5, ObjectMapper      | ~30   |
| Integracao         | Controllers      | MockMvc, @SpringBootTest   | ~27   |
| Arquitetura        | Transversal      | ArchUnit (35 regras)       | 35    |

### Comandos

```bash
# Executar todos os testes
./mvnw test

# Build completo (Spotless + Testes + JaCoCo)
./mvnw clean verify

# Gerar relatorio de cobertura
./mvnw test jacoco:report
# Abrir: target/site/jacoco/index.html

# Formatar codigo (obrigatorio antes de commit)
./mvnw spotless:apply
```

Cobertura minima configurada: **80%**

---

## Docker

### docker-compose.yml

Sobe a stack completa com:
- **MySQL 8.0** (porta 3307 externa, 3306 interna)
- **API Spring Boot** (porta 8080) com health check via `/actuator/health`

```bash
# Subir tudo
docker-compose up --build -d

# Ver logs da API
docker-compose logs -f api

# Parar e limpar dados
docker-compose down -v
```

### Variaveis de Ambiente

| Variavel                 | Padrao               | Descricao                        |
|--------------------------|----------------------|----------------------------------|
| `DB_HOST`                | `mysql`              | Host do banco de dados           |
| `DB_PORT`                | `3306`               | Porta do MySQL                   |
| `DB_NAME`                | `jilocomjurubeba`    | Nome do banco de dados           |
| `DB_USER`                | `jilocomjurubeba`    | Usuario do banco                 |
| `DB_PASSWORD`            | `jilocomjurubeba123` | Senha do banco                   |
| `JWT_SECRET`             | _(fallback interno)_ | Segredo para tokens JWT         |
| `SPRING_PROFILES_ACTIVE` | _(nenhum = H2)_      | `dev` para MySQL                 |

---

## Regras de Negocio

| Regra                                   | Descricao                                                    |
|-----------------------------------------|--------------------------------------------------------------|
| Soft Delete                             | Entidades nunca sao deletadas fisicamente - campo `ativo`    |
| Validacao de Propriedade (Restaurante)  | Apenas o dono pode atualizar seu restaurante                 |
| Validacao de Propriedade (Cardapio)     | Apenas o dono do restaurante pode atualizar seus itens       |
| Unicidade de CPF/Email                  | CPF e email devem ser unicos entre usuarios                  |
| Unicidade de Nome (TipoUsuario)         | Nome de tipo de usuario deve ser unico                       |
| Normalizacao                            | Nomes sao normalizados para UPPERCASE                        |

---

## Troubleshooting

| Problema                          | Solucao                                              |
|-----------------------------------|------------------------------------------------------|
| Build falha na fase `validate`    | Execute `./mvnw spotless:apply`                      |
| Erro de conexao com MySQL         | Verifique se Docker esta rodando: `docker-compose ps` |
| Testes de arquitetura falhando    | Verifique imports entre camadas: `./mvnw test -Dtest=CleanArchitectureTest` |
| H2 Console nao acessivel         | So funciona sem profile `dev`. URL: `http://localhost:8080/h2-console` |
| Token JWT expirado                | Faca login novamente via `POST /auth/login`           |

---

## Documentacao Complementar

| Documento                                                    | Descricao                          |
|--------------------------------------------------------------|------------------------------------|
| [docs/DOCUMENTACAO_PROJETO.md](docs/DOCUMENTACAO_PROJETO.md) | Documentacao tecnica completa      |
| [docs/architecture/README.md](docs/architecture/README.md)   | Arquitetura com diagramas Mermaid  |
| [docs/adr/ADR-0001-clean-architecture.md](docs/adr/ADR-0001-clean-architecture.md) | Decisao arquitetural |
| [docs/standards/conventions.md](docs/standards/conventions.md) | Convencoes de codigo             |

---

## Autores

### Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian

- **Thiago de Jesus Cordeiro** - Desenvolvimento e Arquitetura
- **Juliana Maria Dal Olio Braz** - Desenvolvimento e Arquitetura
- **Luis Henrique Silveira Borges** - Desenvolvimento e Arquitetura
- **Gilmar da Costa Moraes Junior** - Desenvolvimento e Arquitetura
- **Danilo Fernando** - Desenvolvimento e Arquitetura

---

## Licenca

Este projeto e parte do Tech Challenge da Pos-Tech FIAP e destina-se a fins educacionais.
