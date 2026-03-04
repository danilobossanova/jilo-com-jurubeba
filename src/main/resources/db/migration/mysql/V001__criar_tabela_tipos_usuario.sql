-- ============================================================
-- SEED INICIAL DO SISTEMA
-- Tipos de Usuário + Usuário Master
-- ============================================================

-- 1) Tipos de usuário básicos do sistema
INSERT INTO tipo_usuario (nome, ativo, descricao)
VALUES ('MASTER', 1, 'Administrador do sistema');

INSERT INTO tipo_usuario (nome, ativo, descricao)
VALUES ('DONO_RESTAURANTE', 1, 'Dono de restaurante');

INSERT INTO tipo_usuario (nome, ativo, descricao)
VALUES ('CLIENTE', 1, 'Usuário cliente da plataforma');

-- 2) Usuário Master do sistema
-- OBS: senha já deve estar criptografada com BCrypt
INSERT INTO usuario (nome, email, senha_hash, ativo, tipo_usuario_id, cpf)
VALUES (
    'Master',
    'master@jilo.com',
    '$2a$10$7QJ8m5zK9d1eGfWb3sL2uO8rP1aBcDeFgHiJkLmNoPqRsTuVwXyZa',
    1,
    1, -- MASTER
    '000.000.000-000'
);

-- ============================================================
-- SEED: 2 RESTAURANTES + 10 ITENS DE CARDÁPIO
-- ============================================================

START TRANSACTION;

-- 1) Define um dono existente
-- Aqui pegamos o menor ID da tabela usuario (Master)
SET @DONO_ID := (SELECT MIN(id) FROM usuario);

-- ============================================================
-- 2) Criação dos Restaurantes
-- ============================================================

INSERT INTO restaurante 
(nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo)
VALUES
('Jiló & Jurubeba - Centro', 
 'Av. Principal, 123 - Centro', 
 'BRASILEIRA', 
 '11:00:00', 
 '22:00:00', 
 @DONO_ID, 
 1),

('Jiló & Jurubeba - Praia',  
 'Rua da Orla, 45 - Praia',     
 'ITALIANA',   
 '10:00:00', 
 '23:00:00', 
 @DONO_ID, 
 1);

-- Captura dos IDs gerados
SET @REST2_ID := LAST_INSERT_ID();
SET @REST1_ID := @REST2_ID - 1;

-- ============================================================
-- 3) Cardápio Restaurante 1 (Centro)
-- ============================================================

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
VALUES
('Jiló Grelhado', 
 'Jiló grelhado com alho e ervas', 
 18.90, 
 0, 
 NULL, 
 1, 
 @REST1_ID),

('Jurubeba na Brasa', 
 'Jurubeba assada com azeite e sal grosso', 
 21.50, 
 0, 
 NULL, 
 1, 
 @REST1_ID),

('Escondidinho de Carne', 
 'Purê com carne bem temperada', 
 29.90, 
 0, 
 NULL, 
 1, 
 @REST1_ID),

('Baião do Sertão', 
 'Arroz, feijão, queijo coalho e carne de sol', 
 32.00, 
 0, 
 NULL, 
 1, 
 @REST1_ID),

('Suco da Casa', 
 'Suco natural 400ml', 
 9.50, 
 1, 
 NULL, 
 1, 
 @REST1_ID);

-- ============================================================
-- 4) Cardápio Restaurante 2 (Praia)
-- ============================================================

INSERT INTO cardapio
(nome, descricao, preco, apenas_no_local, caminho_foto, ativo, restaurante_id)
VALUES
('Bruschetta da Orla', 
 'Tomate, manjericão e azeite no pão', 
 16.90, 
 0, 
 NULL, 
 1, 
 @REST2_ID),

('Pasta al Pomodoro', 
 'Massa ao molho de tomate artesanal', 
 34.90, 
 0, 
 NULL, 
 1, 
 @REST2_ID),

('Frango à Parmegiana', 
 'Frango empanado com queijo e molho', 
 38.50, 
 0, 
 NULL, 
 1, 
 @REST2_ID),

('Pizza Margherita', 
 'Molho, muçarela e manjericão', 
 42.00, 
 0, 
 NULL, 
 1, 
 @REST2_ID),

('Refrigerante Lata', 
 'Lata 350ml', 
 6.50, 
 1, 
 NULL, 
 1, 
 @REST2_ID);

COMMIT;

-- ============================================================
-- CONSULTAS DE CONFERÊNCIA
-- ============================================================

-- Restaurantes criados
-- SELECT * FROM restaurante ORDER BY id DESC LIMIT 2;

-- Itens criados
-- SELECT * FROM cardapio 
-- WHERE restaurante_id IN (@REST1_ID, @REST2_ID)
-- ORDER BY restaurante_id, id;