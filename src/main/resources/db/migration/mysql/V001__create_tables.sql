CREATE TABLE tipo_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL
);

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    cpf VARCHAR(20),
    email VARCHAR(150),
    telefone VARCHAR(20),
    tipo_usuario_id BIGINT,
    senha_hash VARCHAR(255),
    ativo BOOLEAN,
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);

CREATE TABLE restaurante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200),
    endereco VARCHAR(255),
    type_cozinha VARCHAR(50),
    hora_abertura TIME,
    hora_fechamento TIME,
    dono_id BIGINT,
    ativo BOOLEAN,
    FOREIGN KEY (dono_id) REFERENCES usuario(id)
);

CREATE TABLE cardapio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200),
    descricao TEXT,
    preco DECIMAL(10,2),
    apenas_no_local BOOLEAN,
    caminho_foto VARCHAR(255),
    ativo BOOLEAN,
    restaurante_id BIGINT,
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
);
