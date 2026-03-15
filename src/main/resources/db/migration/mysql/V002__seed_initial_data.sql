-- =====================================================================
-- V001 - Schema inicial MySQL + seed
-- Tabelas alinhadas com as entidades JPA:
--   tipo_usuario, usuario, restaurante, cardapio
-- =====================================================================

CREATE TABLE IF NOT EXISTS tipo_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(19, 2) NOT NULL,
    apenas_no_local BOOLEAN NOT NULL,
    caminho_foto VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_cardapio_restaurante
        FOREIGN KEY (restaurante_id) REFERENCES restaurante (id)
);

CREATE INDEX idx_usuario_tipo_usuario_id ON usuario (tipo_usuario_id);
CREATE INDEX idx_restaurante_dono_id ON restaurante (dono_id);
CREATE INDEX idx_cardapio_restaurante_id ON cardapio (restaurante_id);

-- =====================================================================
-- Seed inicial
-- senha do master: fornecida via placeholder Flyway
-- =====================================================================

INSERT INTO tipo_usuario (nome, ativo, descricao)
SELECT 'MASTER', 1, 'Administrador do sistema'
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'MASTER');

INSERT INTO tipo_usuario (nome, ativo, descricao)
SELECT 'DONO_RESTAURANTE', 1, 'Dono de restaurante'
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'DONO_RESTAURANTE');

INSERT INTO tipo_usuario (nome, ativo, descricao)
SELECT 'CLIENTE', 1, 'Usuário cliente da plataforma'
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'CLIENTE');

SET @TIPO_MASTER_ID := (SELECT id FROM tipo_usuario WHERE nome = 'MASTER' LIMIT 1);

INSERT INTO usuario (nome, email, senha_hash, ativo, tipo_usuario_id, cpf, telefone)
SELECT
    'MASTER',
    'master@jilo.com',
    '${master_password_hash}',
    1,
    @TIPO_MASTER_ID,
    '00000000000',
    '79999999999'
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE email = 'master@jilo.com'
);

-- ============================================================
-- 4) DEFINE UM DONO EXISTENTE
-- Preferência: usuário MASTER
-- ============================================================

SET @DONO_ID := (
    SELECT id FROM usuario WHERE email = 'master@jilo.com' LIMIT 1
);

-- ============================================================
-- 5) RESTAURANTES
-- ============================================================

INSERT INTO restaurante
(nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo)
SELECT
    'JILÓ & JURUBEBA - CENTRO',
    'Av. Principal, 123 - Centro',
    'BRASILEIRA',
    '11:00:00',
    '22:00:00',
    @DONO_ID,
    1
WHERE NOT EXISTS (
    SELECT 1 FROM restaurante WHERE nome = 'JILÓ & JURUBEBA - CENTRO'
);

INSERT INTO restaurante
(nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo)
SELECT
    'JILÓ & JURUBEBA - PRAIA',
    'Rua da Orla, 45 - Praia',
    'ITALIANA',
    '10:00:00',
    '23:00:00',
    @DONO_ID,
    1
WHERE NOT EXISTS (
    SELECT 1 FROM restaurante WHERE nome = 'JILÓ & JURUBEBA - PRAIA'
);

-- ============================================================
-- 6) CAPTURA IDs DOS RESTAURANTES
-- ============================================================

SET @REST1_ID := (
    SELECT id FROM restaurante WHERE nome = 'JILÓ & JURUBEBA - CENTRO' LIMIT 1
);

SET @REST2_ID := (
    SELECT id FROM restaurante WHERE nome = 'JILÓ & JURUBEBA - PRAIA' LIMIT 1
);

-- ============================================================
-- 7) CARDÁPIO RESTAURANTE 1
-- ============================================================

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'JILÓ GRELHADO',
    'Jiló grelhado com alho e ervas',
    18.90,
    0,
    NULL,
    1,
    @REST1_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'JILÓ GRELHADO' AND restaurante_id = @REST1_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'JURUBEBA NA BRASA',
    'Jurubeba assada com azeite e sal grosso',
    21.50,
    0,
    NULL,
    1,
    @REST1_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'JURUBEBA NA BRASA' AND restaurante_id = @REST1_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'ESCONDIDINHO DE CARNE',
    'Purê com carne bem temperada',
    29.90,
    0,
    NULL,
    1,
    @REST1_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'ESCONDIDINHO DE CARNE' AND restaurante_id = @REST1_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'BAIÃO DO SERTÃO',
    'Arroz, feijão, queijo coalho e carne de sol',
    32.00,
    0,
    NULL,
    1,
    @REST1_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'BAIÃO DO SERTÃO' AND restaurante_id = @REST1_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'SUCO DA CASA',
    'Suco natural 400ml',
    9.50,
    1,
    NULL,
    1,
    @REST1_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'SUCO DA CASA' AND restaurante_id = @REST1_ID
);

-- ============================================================
-- 8) CARDÁPIO RESTAURANTE 2
-- ============================================================

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'BRUSCHETTA DA ORLA',
    'Tomate, manjericão e azeite no pão',
    16.90,
    0,
    NULL,
    1,
    @REST2_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'BRUSCHETTA DA ORLA' AND restaurante_id = @REST2_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'PASTA AL POMODORO',
    'Massa ao molho de tomate artesanal',
    34.90,
    0,
    NULL,
    1,
    @REST2_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'PASTA AL POMODORO' AND restaurante_id = @REST2_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'FRANGO À PARMEGIANA',
    'Frango empanado com queijo e molho',
    38.50,
    0,
    NULL,
    1,
    @REST2_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'FRANGO À PARMEGIANA' AND restaurante_id = @REST2_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'PIZZA MARGHERITA',
    'Molho, muçarela e manjericão',
    42.00,
    0,
    NULL,
    1,
    @REST2_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'PIZZA MARGHERITA' AND restaurante_id = @REST2_ID
);

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
SELECT
    'REFRIGERANTE LATA',
    'Lata 350ml',
    6.50,
    1,
    NULL,
    1,
    @REST2_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cardapio
    WHERE nome = 'REFRIGERANTE LATA' AND restaurante_id = @REST2_ID
);

COMMIT;
