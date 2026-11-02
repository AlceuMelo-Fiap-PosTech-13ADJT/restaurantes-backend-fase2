-- ============================================================================
-- Restaurantes API - Script de Inicializacao e Seeds MySQL 8 (Docker Deploy)
-- Localizacao: infra/scripts/init.sql
-- Todos os identificadores sao obrigatoriamente UUID (VARCHAR 36)
-- ============================================================================

CREATE DATABASE IF NOT EXISTS restaurantes_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE restaurantes_db;

-- ----------------------------------------------------------------------------
-- 1. TABELA: tipos_usuario
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tipos_usuario (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao DATETIME NOT NULL,
    data_alteracao DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 2. TABELA: usuarios
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    restaurante_id VARCHAR(36),
    tipo_usuario_id VARCHAR(36) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao DATETIME NOT NULL,
    data_alteracao DATETIME,
    CONSTRAINT fk_usuarios_tipo_usuario
        FOREIGN KEY (tipo_usuario_id) REFERENCES tipos_usuario (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 3. TABELA: restaurantes
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS restaurantes (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    tipo_cozinha VARCHAR(100) NOT NULL,
    horario_funcionamento VARCHAR(100) NOT NULL,
    dono_id VARCHAR(36) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao DATETIME NOT NULL,
    data_alteracao DATETIME,
    CONSTRAINT fk_restaurantes_dono
        FOREIGN KEY (dono_id) REFERENCES usuarios (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 4. TABELA: itens_cardapio
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS itens_cardapio (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    restaurante_id VARCHAR(36) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    disponivel_apenas_restaurante BOOLEAN NOT NULL DEFAULT FALSE,
    foto_path VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao DATETIME NOT NULL,
    data_alteracao DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- SEEDS / CARGA INICIAL COM IDENTIFICADORES UUID
-- ----------------------------------------------------------------------------

-- 1. Tipos de Usuario
INSERT IGNORE INTO tipos_usuario (id, tipo, nome, ativo, data_criacao, data_alteracao) VALUES
('e1a8a992-1262-4f0e-b8d2-1c6d860d5b01', 'DONO_RESTAURANTE', 'Dono de restaurante', true, CURRENT_TIMESTAMP, NULL),
('e1a8a992-1262-4f0e-b8d2-1c6d860d5b02', 'CLIENTE', 'Cliente', true, CURRENT_TIMESTAMP, NULL);

-- 2. Usuarios de Teste Multi-Tenant
INSERT IGNORE INTO usuarios (id, nome, email, restaurante_id, tipo_usuario_id, ativo, data_criacao, data_alteracao) VALUES
('b1a0f443-4318-4720-bd44-df41865c1001', 'Carlos Silva (Dono R1)', 'carlos.silva@bellanapoli.com', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b01', true, CURRENT_TIMESTAMP, NULL),
('b1a0f443-4318-4720-bd44-df41865c1002', 'Mariana Costa (Cliente R1)', 'mariana.costa@email.com', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b02', true, CURRENT_TIMESTAMP, NULL),
('b1a0f443-4318-4720-bd44-df41865c1003', 'Jose Santos (Dono R2)', 'jose.santos@sabordosertao.com', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b01', true, CURRENT_TIMESTAMP, NULL),
('b1a0f443-4318-4720-bd44-df41865c1004', 'Fernanda Lima (Cliente R2)', 'fernanda.lima@email.com', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b02', true, CURRENT_TIMESTAMP, NULL);

-- 3. Restaurantes
INSERT IGNORE INTO restaurantes (id, nome, endereco, tipo_cozinha, horario_funcionamento, dono_id, ativo, data_criacao, data_alteracao) VALUES
('c3b01f92-563b-4f68-b7e1-88892df9a001', 'Cantina Bella Roma', 'Rua Augusta, 100 - Cerqueira Cesar, Sao Paulo - SP', 'Italiana', '11:30 as 23:00', 'b1a0f443-4318-4720-bd44-df41865c1001', true, CURRENT_TIMESTAMP, NULL),
('c3b01f92-563b-4f68-b7e1-88892df9a002', 'Sabor do Sertao', 'Av. Paulista, 500 - Bela Vista, Sao Paulo - SP', 'Nordestina', '11:00 as 22:00', 'b1a0f443-4318-4720-bd44-df41865c1003', true, CURRENT_TIMESTAMP, NULL);

-- 4. Itens do Cardapio
INSERT IGNORE INTO itens_cardapio (id, restaurante_id, nome, descricao, preco, disponivel_apenas_restaurante, foto_path, ativo, data_criacao, data_alteracao) VALUES
('f4c02a83-674c-4e79-c8f2-99903ef0b001', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'Pizza Margherita', 'Molho de tomate pelado italiano, mussarela de bufala fresca, manjericao organico e azeite extravirgem.', 65.00, false, '/imagens/cardapio/pizza-margherita.jpg', true, CURRENT_TIMESTAMP, NULL),
('f4c02a83-674c-4e79-c8f2-99903ef0b002', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'Lasanha Bolonhesa Classica', 'Massa fresca artesanal em camadas com tradicional ragu de carne bovina e suina, bechamel e parmesao.', 58.50, true, '/imagens/cardapio/lasanha-bolonhesa.jpg', true, CURRENT_TIMESTAMP, NULL),
('f4c02a83-674c-4e79-c8f2-99903ef0b003', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'Baiao de Dois Especial', 'Arroz da terra com feijao de corda fresco, queijo de coalho dourado na chapa, bacon e carne de sol desfiada.', 49.90, false, '/imagens/cardapio/baiao-de-dois.jpg', true, CURRENT_TIMESTAMP, NULL),
('f4c02a83-674c-4e79-c8f2-99903ef0b004', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'Carne de Sol com Macaxeira', 'Carne de sol maturada e grelhada na manteiga de garrafa, servida com mandioca cozida e dourada na brasa.', 62.00, false, '/imagens/cardapio/carne-de-sol.jpg', true, CURRENT_TIMESTAMP, NULL);
