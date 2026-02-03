# jilo-com-jurubeba

### Tech Challenge - Fase 2 | Pos-Tech Java Architecture (FIAP)

**jilo-com-jurubeba** e um sistema de gestao robusto e compartilhado, desenvolvido para um grupo de restaurantes locais que busca eficiencia operacional e reducao de custos com sistemas individuais. A aplicacao permite que restaurantes gerenciem seus cardapios enquanto clientes podem consultar informacoes do cardapio.

---

## Indice

- [Objetivo da Fase 2](#objetivo-da-fase-2)
- [Arquitetura do Projeto](#arquitetura-do-projeto)
- [Estrutura de Pacotes](#estrutura-de-pacotes)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Como Executar](#como-executar)
- [Ferramentas de Qualidade de Codigo](#ferramentas-de-qualidade-de-codigo)
- [Testes](#testes)
- [Docker](#docker)
- [Documentacao da API](#documentacao-da-api)
- [Requisitos Funcionais](#requisitos-funcionais)

---

## Objetivo da Fase 2

Esta etapa foca na expansao do sistema atraves da gestao de tipos de usuarios (Dono vs. Cliente), cadastro de restaurantes e estruturacao de cardapios. O diferencial deste projeto e a aplicacao rigorosa de **Clean Architecture**, garantindo que o codigo seja testavel, escalavel e independente de frameworks.

---

## Arquitetura do Projeto

O projeto segue os principios da **Clean Architecture** (Robert C. Martin), dividindo-se em 4 camadas:

```
+-----------------------------------------------------------+
|                      INTERFACES                           |
|              (Controllers REST, Handlers)                 |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                      APPLICATION                          |
|                (Use Cases, DTOs)                          |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                        DOMAIN                             |
|        (Entities, Value Objects, Gateways, Exceptions)    |
+-----------------------------------------------------------+
                            ^
                            |
+-----------------------------------------------------------+
|                    INFRASTRUCTURE                         |
|         (Persistence, Config, External Services)          |
+-----------------------------------------------------------+
```

### Regras de Dependencia

| Camada         | Pode depender de           | NAO pode depender de          |
|----------------|----------------------------|-------------------------------|
| Domain         | Nada (e o nucleo)          | Application, Interfaces, Infrastructure |
| Application    | Domain                     | Interfaces, Infrastructure    |
| Interfaces     | Application, Domain        | Infrastructure                |
| Infrastructure | Application, Domain        | Interfaces                    |

> Documentacao detalhada: [docs/architecture/README.md](docs/architecture/README.md)

---

## Estrutura de Pacotes

```
com.grupo3.postech.jilocomjurubeba
|-- domain/                    # Camada de Dominio (sem dependencias externas)
|   |-- entity/                # Entidades de negocio
|   |-- valueobject/           # Value Objects imutaveis
|   |-- gateway/               # Interfaces para mundo externo (ports)
|   +-- exception/             # Excecoes de dominio
|
|-- application/               # Camada de Aplicacao
|   |-- usecase/               # Casos de uso (regras de aplicacao)
|   +-- dto/                   # DTOs de entrada/saida
|
|-- interfaces/                # Camada de Interfaces (adapters de entrada)
|   +-- rest/                  # Adaptadores REST
|       |-- dto/               # Request/Response DTOs
|       |-- mapper/            # Conversores REST <-> Application
|       +-- handler/           # Exception handlers (RFC 7807)
|
+-- infrastructure/            # Camada de Infraestrutura (adapters de saida)
    |-- persistence/           # Implementacoes de persistencia
    |   |-- entity/            # Entidades JPA
    |   |-- repository/        # Repositorios Spring Data
    |   |-- gateway/           # Implementacoes dos Gateways
    |   +-- mapper/            # Conversores JPA <-> Domain
    +-- config/                # Configuracoes Spring
```

---

## Tecnologias

| Tecnologia       | Versao   | Proposito                          |
|------------------|----------|------------------------------------|
| Java             | 21       | Linguagem principal                |
| Spring Boot      | 3.5.10   | Framework web                      |
| Spring Security  | 6.x      | Autenticacao e autorizacao         |
| Spring Data JPA  | 3.x      | Persistencia de dados              |
| PostgreSQL       | 16       | Banco de dados principal           |
| MySQL            | 8        | Banco de dados alternativo         |
| MongoDB          | 7        | Banco NoSQL (opcional)             |
| H2               | 2.x      | Banco em memoria para testes       |
| Flyway           | 10.x     | Migracao de banco de dados         |
| MapStruct        | 1.5.5    | Mapeamento de objetos              |
| Lombok           | 1.18.32  | Reducao de boilerplate             |
| SpringDoc OpenAPI| 2.5.0    | Documentacao da API (Swagger)      |
| ArchUnit         | 1.2.1    | Testes de arquitetura              |
| JaCoCo           | 0.8.12   | Cobertura de testes                |
| Spotless         | 2.43.0   | Formatacao automatica de codigo    |
| Testcontainers   | 1.19.7   | Testes de integracao com containers|
| Docker           | 24+      | Containerizacao                    |

---

## Requisitos

- **Java 21** ou superior
- **Maven 3.9+** (ou use o Maven Wrapper incluso)
- **Docker** e **Docker Compose** (para execucao com containers)

---

## Como Executar

### Desenvolvimento Local (H2 em memoria)

```bash
# Usando Maven Wrapper
./mvnw spring-boot:run

# Ou Maven instalado
mvn spring-boot:run
```

A aplicacao estara disponivel em: http://localhost:8080

### Com PostgreSQL (Docker)

```bash
# Subir apenas o banco de dados
docker-compose -f docker-compose.dev.yml up -d

# Executar aplicacao com profile postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Com MySQL (Docker)

```bash
# Subir MySQL
docker-compose -f docker-compose.dev.yml --profile mysql up -d

# Executar aplicacao com profile mysql
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

### Com MongoDB (Docker)

```bash
# Subir MongoDB
docker-compose -f docker-compose.dev.yml --profile mongodb up -d

# Executar aplicacao com profile mongodb
./mvnw spring-boot:run -Dspring-boot.run.profiles=mongodb
```

### Aplicacao Completa (Docker Compose)

```bash
# Build e execucao completa
docker-compose up --build

# Em background
docker-compose up -d --build
```

### Desativar Spring Security (Desenvolvimento)

Para desenvolvimento sem autenticacao:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=nosecurity
```

Ou adicione ao `application.properties`:
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```

---

## Ferramentas de Qualidade de Codigo

### Spotless (Formatacao Automatica)

O projeto usa **Spotless** com **Google Java Format** (estilo AOSP) para formatacao automatica.

```bash
# Verificar se o codigo esta formatado
./mvnw spotless:check

# Aplicar formatacao automaticamente
./mvnw spotless:apply
```

**Configuracao aplicada:**
- Google Java Format (estilo AOSP - 4 espacos de indentacao)
- Organizacao de imports: `java|javax, jakarta, org, com, com.grupo3.postech`
- Remocao de imports nao utilizados
- Trim de whitespace e newline ao final

> O Spotless executa automaticamente na fase `validate` do Maven. Se o codigo nao estiver formatado, o build falha.

### EditorConfig

O arquivo `.editorconfig` padroniza formatacao entre diferentes editores/IDEs:

- **Java**: 4 espacos, max 120 caracteres por linha
- **YAML**: 2 espacos
- **JSON**: 2 espacos
- **Markdown**: preserva trailing whitespace
- **Line endings**: LF (exceto .cmd/.bat que usam CRLF)

> A maioria das IDEs (IntelliJ, VSCode, Eclipse) reconhece automaticamente o `.editorconfig`.

### JaCoCo (Cobertura de Testes)

Cobertura minima configurada: **80%**

```bash
# Gerar relatorio de cobertura
./mvnw test jacoco:report

# Relatorio disponivel em:
# target/site/jacoco/index.html
```

### ArchUnit (Validacao de Arquitetura)

Testes automatizados garantem que as regras da Clean Architecture sao respeitadas:

```bash
# Executar apenas testes de arquitetura
./mvnw test -Dtest=CleanArchitectureTest
```

**Regras validadas:**
- Domain nao depende de outras camadas
- Domain nao usa Spring ou JPA
- Application nao depende de Infrastructure ou Interfaces
- Application nao usa Spring Web ou JPA
- Nomenclatura: UseCases terminam com `UseCase`, Controllers com `Controller`, etc.

---

## Testes

### Executar Todos os Testes

```bash
./mvnw test
```

### Executar Testes Especificos

```bash
# Testes de arquitetura
./mvnw test -Dtest=CleanArchitectureTest

# Testes de um pacote
./mvnw test -Dtest="com.grupo3.postech.jilocomjurubeba.application.**"

# Teste especifico
./mvnw test -Dtest=VerificarSaudeUseCaseTest
```

### Testes de Integracao

```bash
./mvnw verify
```

### Cobertura de Testes

```bash
./mvnw test jacoco:report
# Abrir: target/site/jacoco/index.html
```

---

## Docker

### Comandos Uteis

```bash
# Build da imagem
docker build -t jilo-com-jurubeba .

# Executar container
docker run -p 8080:8080 jilo-com-jurubeba

# Subir stack completa
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Parar tudo
docker-compose down

# Limpar volumes (CUIDADO: apaga dados)
docker-compose down -v
```

### Profiles Disponiveis (docker-compose.dev.yml)

| Profile   | Servicos              | Comando                                      |
|-----------|-----------------------|----------------------------------------------|
| (default) | PostgreSQL            | `docker-compose -f docker-compose.dev.yml up` |
| mysql     | PostgreSQL + MySQL    | `docker-compose -f docker-compose.dev.yml --profile mysql up` |
| mongodb   | PostgreSQL + MongoDB  | `docker-compose -f docker-compose.dev.yml --profile mongodb up` |

---

## Documentacao da API

### Swagger UI

Apos iniciar a aplicacao, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

### Endpoints Disponiveis

| Metodo | Endpoint      | Descricao              | Autenticacao |
|--------|---------------|------------------------|--------------|
| GET    | /v1/health    | Health check da API    | Nao          |
| GET    | /actuator/health | Health do Spring    | Nao          |
| GET    | /actuator/info   | Info da aplicacao   | Nao          |

### Console H2 (Desenvolvimento)

- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:jilocomjurubeba`
- **Usuario**: `sa`
- **Senha**: (vazio)

---

## Requisitos Funcionais

### 1. Gestao de Usuarios e Permissoes
- Implementacao de CRUD para tipos de usuarios: `DONO_RESTAURANTE` e `CLIENTE`
- Cada usuario deve estar vinculado a um tipo especifico

### 2. Cadastro de Restaurantes
- CRUD completo: nome, endereco, tipo de cozinha, horario de funcionamento
- Todo restaurante deve possuir um usuario responsavel (Dono)

### 3. Cardapio e Itens
- Cadastro de produtos com nome, descricao e preco
- Definicao de disponibilidade para pedidos apenas no local
- Armazenamento do caminho da foto do prato

---

## Regras de Negocio

| Perfil              | Permissoes                                              |
|---------------------|---------------------------------------------------------|
| DONO_RESTAURANTE    | CRUD completo de restaurantes e itens de cardapio       |
| CLIENTE             | Consulta de restaurantes e visualizacao de produtos     |

---

## Contribuicao

Consulte o guia de contribuicao: [CONTRIBUTING.md](CONTRIBUTING.md)

---

## Licenca

Este projeto e parte do Tech Challenge da Pos-Tech FIAP e destina-se a fins educacionais.

---

## Autores

### Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian

- **Thiago de Jesus cordeiro** - Desenvolvimento e Arquitetura
- **Juliana Maria Dal Olio Braz** - Desenvolvimento e Arquitetura
- **Luis Henrique SIlveira Borges** - Desenvolvimento e Arquitetura
- **Gilmar da Costa Moraes Júnior** - Desenvolvimento e Arquitetura
- **Danilo Fernando** - Desenvolvimento e Arquitetura

