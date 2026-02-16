-- =====================================================================
-- jilo-com-jurubeba - Estrutura Completa do Banco de Dados
-- Tech Challenge FIAP Pos-Tech Fase 2 - 11ADJT
-- =====================================================================
-- Compativel com: PostgreSQL 16+
-- Gerar MER/DER: DBeaver, pgAdmin, MySQL Workbench, dbdiagram.io
-- =====================================================================
-- IMPORTANTE: Este arquivo e a referencia do modelo de dados completo.
-- As migrations Flyway em src/main/resources/db/migration/ sao a fonte
-- da verdade para DDL executavel. Este arquivo serve para documentacao
-- e geracao de diagramas MER/DER.
-- =====================================================================

-- =====================================================================
-- ENUM TYPES
-- =====================================================================

CREATE TYPE tipo_cozinha AS ENUM (
    'BRASILEIRA',
    'ITALIANA',
    'JAPONESA',
    'CHINESA',
    'MEXICANA',
    'FRANCESA',
    'ARABE',
    'INDIANA',
    'TAILANDESA',
    'PORTUGUESA',
    'AMERICANA',
    'COREANA',
    'VEGANA',
    'VEGETARIANA',
    'CONTEMPORANEA',
    'FRUTOS_DO_MAR',
    'CHURRASCARIA',
    'PIZZARIA',
    'OUTRA'
);

CREATE TYPE dia_semana AS ENUM (
    'SEGUNDA',
    'TERCA',
    'QUARTA',
    'QUINTA',
    'SEXTA',
    'SABADO',
    'DOMINGO'
);

-- =====================================================================
-- TABELA: tipos_usuario
-- Define os papeis dos usuarios no sistema (MASTER, DONO_RESTAURANTE,
-- CLIENTE). Modelado como tabela (nao enum) para permitir CRUD e
-- extensibilidade sem alterar schema.
-- Migration: V001__criar_tabela_tipos_usuario.sql
-- =====================================================================

CREATE TABLE tipos_usuario (
    id              BIGSERIAL       PRIMARY KEY,
    nome            VARCHAR(50)     NOT NULL,
    descricao       VARCHAR(255)    NOT NULL,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT uk_tipos_usuario_nome UNIQUE (nome)
);

CREATE INDEX idx_tipos_usuario_nome ON tipos_usuario (nome);
CREATE INDEX idx_tipos_usuario_ativo ON tipos_usuario (ativo);

COMMENT ON TABLE tipos_usuario IS 'Tipos/papeis de usuario no sistema';
COMMENT ON COLUMN tipos_usuario.nome IS 'Nome unico do tipo (MASTER, DONO_RESTAURANTE, CLIENTE)';
COMMENT ON COLUMN tipos_usuario.descricao IS 'Descricao legivel do tipo de usuario';
COMMENT ON COLUMN tipos_usuario.ativo IS 'Soft delete - FALSE significa tipo desativado';

-- Dados iniciais (semeados pelo TipoUsuarioDataSeeder)
-- INSERT INTO tipos_usuario (nome, descricao) VALUES
--     ('MASTER', 'Administrador do sistema com acesso total'),
--     ('DONO_RESTAURANTE', 'Proprietario de restaurante que gerencia cardapios'),
--     ('CLIENTE', 'Cliente que consulta restaurantes e cardapios');

-- =====================================================================
-- TABELA: usuarios
-- Armazena os usuarios do sistema (donos e clientes)
-- =====================================================================

CREATE TABLE usuarios (
    id                  BIGSERIAL       PRIMARY KEY,
    nome                VARCHAR(150)    NOT NULL,
    email               VARCHAR(255)    NOT NULL,
    cpf                 VARCHAR(11)     NOT NULL,
    senha               VARCHAR(255)    NOT NULL,
    tipo_usuario_id     BIGINT          NOT NULL,
    ativo               BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       TIMESTAMP,

    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT uk_usuarios_cpf   UNIQUE (cpf),
    CONSTRAINT ck_usuarios_cpf_length CHECK (LENGTH(cpf) = 11),

    CONSTRAINT fk_usuarios_tipo_usuario
        FOREIGN KEY (tipo_usuario_id) REFERENCES tipos_usuario (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_usuarios_tipo_usuario_id ON usuarios (tipo_usuario_id);
CREATE INDEX idx_usuarios_email ON usuarios (email);
CREATE INDEX idx_usuarios_nome ON usuarios (nome);

COMMENT ON TABLE usuarios IS 'Usuarios do sistema - administradores, donos de restaurante e clientes';
COMMENT ON COLUMN usuarios.cpf IS 'CPF sem formatacao (apenas 11 digitos)';
COMMENT ON COLUMN usuarios.tipo_usuario_id IS 'FK para tipos_usuario - define o papel do usuario';
COMMENT ON COLUMN usuarios.senha IS 'Senha criptografada (BCrypt)';

-- =====================================================================
-- TABELA: enderecos
-- Enderecos reutilizaveis (restaurantes e futuramente usuarios)
-- =====================================================================

CREATE TABLE enderecos (
    id              BIGSERIAL       PRIMARY KEY,
    logradouro      VARCHAR(255)    NOT NULL,
    numero          VARCHAR(20)     NOT NULL,
    complemento     VARCHAR(100),
    bairro          VARCHAR(100)    NOT NULL,
    cidade          VARCHAR(100)    NOT NULL,
    estado          VARCHAR(2)      NOT NULL,
    cep             VARCHAR(8)      NOT NULL,
    latitude        DECIMAL(10, 8),
    longitude       DECIMAL(11, 8),
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT ck_enderecos_cep_length CHECK (LENGTH(cep) = 8),
    CONSTRAINT ck_enderecos_estado_length CHECK (LENGTH(estado) = 2)
);

CREATE INDEX idx_enderecos_cep ON enderecos (cep);
CREATE INDEX idx_enderecos_cidade ON enderecos (cidade);

COMMENT ON TABLE enderecos IS 'Enderecos dos restaurantes';
COMMENT ON COLUMN enderecos.cep IS 'CEP sem formatacao (apenas 8 digitos)';
COMMENT ON COLUMN enderecos.estado IS 'Sigla UF (ex: SP, RJ, MG)';

-- =====================================================================
-- TABELA: restaurantes
-- Restaurantes cadastrados no sistema
-- =====================================================================

CREATE TABLE restaurantes (
    id                  BIGSERIAL       PRIMARY KEY,
    nome                VARCHAR(200)    NOT NULL,
    descricao           TEXT,
    tipo_cozinha        tipo_cozinha    NOT NULL,
    capacidade          INTEGER,
    dono_id             BIGINT          NOT NULL,
    endereco_id         BIGINT          NOT NULL,
    ativo               BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       TIMESTAMP,

    CONSTRAINT fk_restaurantes_dono
        FOREIGN KEY (dono_id) REFERENCES usuarios (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_restaurantes_endereco
        FOREIGN KEY (endereco_id) REFERENCES enderecos (id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_restaurantes_capacidade CHECK (capacidade IS NULL OR capacidade > 0)
);

CREATE INDEX idx_restaurantes_dono_id ON restaurantes (dono_id);
CREATE INDEX idx_restaurantes_tipo_cozinha ON restaurantes (tipo_cozinha);
CREATE INDEX idx_restaurantes_nome ON restaurantes (nome);
CREATE INDEX idx_restaurantes_ativo ON restaurantes (ativo);

COMMENT ON TABLE restaurantes IS 'Restaurantes gerenciados pelos donos';
COMMENT ON COLUMN restaurantes.dono_id IS 'FK para usuarios - apenas tipo DONO_RESTAURANTE';
COMMENT ON COLUMN restaurantes.capacidade IS 'Capacidade maxima de pessoas no local';

-- =====================================================================
-- TABELA: horarios_funcionamento
-- Horarios de abertura/fechamento por dia da semana
-- =====================================================================

CREATE TABLE horarios_funcionamento (
    id              BIGSERIAL       PRIMARY KEY,
    restaurante_id  BIGINT          NOT NULL,
    dia_semana      dia_semana      NOT NULL,
    hora_abertura   TIME            NOT NULL,
    hora_fechamento TIME            NOT NULL,
    fechado         BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_horarios_restaurante
        FOREIGN KEY (restaurante_id) REFERENCES restaurantes (id)
        ON DELETE CASCADE,

    CONSTRAINT uk_horarios_restaurante_dia
        UNIQUE (restaurante_id, dia_semana),

    CONSTRAINT ck_horarios_abertura_fechamento
        CHECK (fechado = TRUE OR hora_abertura < hora_fechamento)
);

CREATE INDEX idx_horarios_restaurante_id ON horarios_funcionamento (restaurante_id);

COMMENT ON TABLE horarios_funcionamento IS 'Horarios de funcionamento por dia da semana';
COMMENT ON COLUMN horarios_funcionamento.fechado IS 'TRUE = restaurante nao abre neste dia';

-- =====================================================================
-- TABELA: itens_cardapio
-- Itens do cardapio de cada restaurante
-- =====================================================================

CREATE TABLE itens_cardapio (
    id              BIGSERIAL       PRIMARY KEY,
    restaurante_id  BIGINT          NOT NULL,
    nome            VARCHAR(200)    NOT NULL,
    descricao       TEXT,
    preco           DECIMAL(10, 2)  NOT NULL,
    foto_caminho    VARCHAR(500),
    disponivel      BOOLEAN         NOT NULL DEFAULT TRUE,
    apenas_local    BOOLEAN         NOT NULL DEFAULT TRUE,
    ordem_exibicao  INTEGER         NOT NULL DEFAULT 0,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT fk_itens_cardapio_restaurante
        FOREIGN KEY (restaurante_id) REFERENCES restaurantes (id)
        ON DELETE CASCADE,

    CONSTRAINT ck_itens_cardapio_preco CHECK (preco > 0)
);

CREATE INDEX idx_itens_cardapio_restaurante_id ON itens_cardapio (restaurante_id);
CREATE INDEX idx_itens_cardapio_disponivel ON itens_cardapio (disponivel);
CREATE INDEX idx_itens_cardapio_ativo ON itens_cardapio (ativo);

COMMENT ON TABLE itens_cardapio IS 'Produtos do cardapio de cada restaurante';
COMMENT ON COLUMN itens_cardapio.preco IS 'Preco em reais (BRL)';
COMMENT ON COLUMN itens_cardapio.foto_caminho IS 'Caminho/URL da foto do prato';
COMMENT ON COLUMN itens_cardapio.apenas_local IS 'TRUE = disponivel apenas para consumo no local';
COMMENT ON COLUMN itens_cardapio.ordem_exibicao IS 'Ordem de exibicao no cardapio (menor = primeiro)';

-- =====================================================================
-- TABELA: avaliacoes (Fase futura - ja modelada)
-- Avaliacoes dos clientes sobre restaurantes
-- =====================================================================

CREATE TABLE avaliacoes (
    id              BIGSERIAL       PRIMARY KEY,
    restaurante_id  BIGINT          NOT NULL,
    cliente_id      BIGINT          NOT NULL,
    nota            INTEGER         NOT NULL,
    comentario      TEXT,
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT fk_avaliacoes_restaurante
        FOREIGN KEY (restaurante_id) REFERENCES restaurantes (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_avaliacoes_cliente
        FOREIGN KEY (cliente_id) REFERENCES usuarios (id)
        ON DELETE CASCADE,

    CONSTRAINT uk_avaliacoes_restaurante_cliente
        UNIQUE (restaurante_id, cliente_id),

    CONSTRAINT ck_avaliacoes_nota CHECK (nota BETWEEN 1 AND 5)
);

CREATE INDEX idx_avaliacoes_restaurante_id ON avaliacoes (restaurante_id);
CREATE INDEX idx_avaliacoes_cliente_id ON avaliacoes (cliente_id);
CREATE INDEX idx_avaliacoes_nota ON avaliacoes (nota);

COMMENT ON TABLE avaliacoes IS 'Avaliacoes dos clientes (1 a 5 estrelas) - fase futura';
COMMENT ON COLUMN avaliacoes.nota IS 'Nota de 1 a 5 estrelas';

-- =====================================================================
-- TABELA: reservas (Fase futura - ja modelada)
-- Reservas de mesas nos restaurantes
-- =====================================================================

CREATE TABLE reservas (
    id                  BIGSERIAL       PRIMARY KEY,
    restaurante_id      BIGINT          NOT NULL,
    cliente_id          BIGINT          NOT NULL,
    data_reserva        DATE            NOT NULL,
    hora_reserva        TIME            NOT NULL,
    quantidade_pessoas  INTEGER         NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDENTE',
    observacao          TEXT,
    criado_em           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       TIMESTAMP,

    CONSTRAINT fk_reservas_restaurante
        FOREIGN KEY (restaurante_id) REFERENCES restaurantes (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_reservas_cliente
        FOREIGN KEY (cliente_id) REFERENCES usuarios (id)
        ON DELETE CASCADE,

    CONSTRAINT ck_reservas_quantidade CHECK (quantidade_pessoas > 0),
    CONSTRAINT ck_reservas_status CHECK (status IN ('PENDENTE', 'CONFIRMADA', 'CANCELADA', 'CONCLUIDA'))
);

CREATE INDEX idx_reservas_restaurante_id ON reservas (restaurante_id);
CREATE INDEX idx_reservas_cliente_id ON reservas (cliente_id);
CREATE INDEX idx_reservas_data ON reservas (data_reserva);
CREATE INDEX idx_reservas_status ON reservas (status);

COMMENT ON TABLE reservas IS 'Reservas de mesas - fase futura';
COMMENT ON COLUMN reservas.status IS 'PENDENTE | CONFIRMADA | CANCELADA | CONCLUIDA';

-- =====================================================================
-- FIM DO SCHEMA
-- =====================================================================
