-- =====================================================================
-- V001 - Schema inicial PostgreSQL + seed
-- Tabelas alinhadas com as entidades JPA:
--   tipo_usuario, usuario, restaurante, cardapio
-- =====================================================================

CREATE TABLE IF NOT EXISTS tipo_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    tipo_usuario_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    senha_hash VARCHAR(255) NOT NULL,
    CONSTRAINT fk_usuario_tipo_usuario
        FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario (id)
);

CREATE TABLE IF NOT EXISTS restaurante (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    type_cozinha VARCHAR(40) NOT NULL,
    hora_abertura TIME NOT NULL,
    hora_fechamento TIME NOT NULL,
    dono_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_restaurante_dono
        FOREIGN KEY (dono_id) REFERENCES usuario (id)
);

CREATE TABLE IF NOT EXISTS cardapio (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco NUMERIC(19, 2) NOT NULL,
    apenas_no_local BOOLEAN NOT NULL,
    caminho_foto VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_cardapio_restaurante
        FOREIGN KEY (restaurante_id) REFERENCES restaurante (id)
);

CREATE INDEX IF NOT EXISTS idx_usuario_tipo_usuario_id ON usuario (tipo_usuario_id);
CREATE INDEX IF NOT EXISTS idx_restaurante_dono_id ON restaurante (dono_id);
CREATE INDEX IF NOT EXISTS idx_cardapio_restaurante_id ON cardapio (restaurante_id);

-- =====================================================================
-- Seed inicial
-- senha do master: fornecida via placeholder Flyway
-- =====================================================================

INSERT INTO tipo_usuario (nome, ativo, descricao)
SELECT 'MASTER', TRUE, 'Administrador do sistema'
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'MASTER');

INSERT INTO tipo_usuario (nome, ativo, descricao)
SELECT 'DONO_RESTAURANTE', TRUE, 'Dono de restaurante'
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'DONO_RESTAURANTE');

INSERT INTO tipo_usuario (nome, ativo, descricao)
SELECT 'CLIENTE', TRUE, 'Usuário cliente da plataforma'
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'CLIENTE');

INSERT INTO usuario (nome, email, senha_hash, ativo, tipo_usuario_id, cpf, telefone)
SELECT
    'MASTER',
    'admin@jilocomjurubeba.com',
    '${master_password_hash}',
    TRUE,
    (SELECT id FROM tipo_usuario WHERE nome = 'MASTER' LIMIT 1),
    '00000000000',
    '79999999999'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@jilocomjurubeba.com');
