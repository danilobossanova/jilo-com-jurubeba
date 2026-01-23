# jilo-com-jurubeba 🍲
### Tech Challenge - Fase 2 | Pós-Tech Java Architecture (FIAP)

**jilo-com-jurubeba** é um sistema de gestão robusto e compartilhado, desenvolvido para um grupo de restaurantes locais que busca eficiência operacional e redução de custos com sistemas individuais . A aplicação permite que restaurantes gerenciem seus cardápios enquanto clientes podem consultar informações e fazer pedidos online.

## 🎯 Objetivo da Fase 2
Esta etapa foca na expansão do sistema através da gestão de tipos de usuários (Dono vs. Cliente), cadastro de restaurantes e estruturação de cardápios,  diferencial deste projeto é a aplicação rigorosa de **Clean Architecture**, garantindo que o código seja testável, escalável e independente de frameworks.

---

## 🏗️ Arquitetura do Projeto
A estrutura segue os princípios da Arquitetura Limpa, dividindo-se em:

* **Domain (Domínio):** Contém as entidades de negócio, use cases e interfaces (gateways). É o núcleo do sistema, livre de dependências externas.
* **Application (Aplicação):** Camada de adaptação que orquestra os dados entre o domínio e as interfaces externas, incluindo DTOs e mapeadores.
* **Infrastructure (Infraestrutura):** Implementações técnicas como persistência de banco de dados, controladores REST (Spring Boot) e configurações de frameworks.

---

## 🛠️ Requisitos Funcionais (Backlog)

### 1. Gestão de Usuários e Permissões
* **Diferenciação de Perfis:** Implementação de CRUD para tipos de usuários: `DONO_RESTAURANTE` e `CLIENTE`.
* **Associação:** Cada usuário do sistema deve estar vinculado a um tipo específico.

### 2. Cadastro de Restaurantes
* **CRUD Completo:** Gestão de nome, endereço, tipo de cozinha e horário de funcionamento .
* **Vínculo de Propriedade:** Todo restaurante deve possuir um usuário responsável (Dono).

### 3. Cardápio e Itens
* **Gestão de Itens:** Cadastro de produtos com nome, descrição e preço.
* **Regras de Consumo:** Definição de disponibilidade para pedidos apenas no local e armazenamento do caminho da foto do prato.

---

## 🚦 Regras de Negócio (Autorização)
Embora o foco desta fase seja a estrutura arquitetural, o sistema segue as seguintes diretrizes lógicas:
* **Donos:** Têm permissão total para gerenciar o restaurante e seus itens de cardápio.
* **Clientes:** Permissão limitada a consultas de restaurantes e visualização de produtos.

---

## 🚀 Tecnologias e Entrega
* **Linguagem/Framework:** Java com Spring Boot.
* **Containerização:** Docker e Docker Compose para orquestração da aplicação e banco de dados.
* **Qualidade:** Cobertura mínima de 80% em testes unitários e testes de integração.
* **Documentação:** API documentada e disponível via Collections do Postman.

---

## 📦 Como Executar
1. Certifique-se de ter o Docker instalado.
2. Clone o repositório.
3. No terminal, execute:
   ```bash
   docker-compose up --build
   ```
4. A aplicação estará disponível em http://localhost:8080.
   
