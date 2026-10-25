-- ============================================================================
-- Restaurantes API - Script de Inicializacao e Seeds (data.sql)
-- Compativel com H2 (Modo MySQL) e MySQL 8.x
-- Todos os identificadores sao obrigatoriamente UUID (VARCHAR 36)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. TIPOS DE USUARIO (tipos_usuario)
-- Subtipos mapeados para TipoUsuarioEnum: DONO_RESTAURANTE e CLIENTE
-- ----------------------------------------------------------------------------
INSERT INTO tipos_usuario (id, tipo, nome, ativo, data_criacao, data_alteracao) VALUES
('e1a8a992-1262-4f0e-b8d2-1c6d860d5b01', 'DONO_RESTAURANTE', 'Dono de restaurante', true, CURRENT_TIMESTAMP, NULL),
('e1a8a992-1262-4f0e-b8d2-1c6d860d5b02', 'CLIENTE', 'Cliente', true, CURRENT_TIMESTAMP, NULL);

-- ----------------------------------------------------------------------------
-- 2. USUARIOS (usuarios)
-- Donos e Clientes vinculados aos restaurantes de teste para isolamento multi-tenant
-- ----------------------------------------------------------------------------
INSERT INTO usuarios (id, nome, email, restaurante_id, tipo_usuario_id, ativo, data_criacao, data_alteracao) VALUES
('b1a0f443-4318-4720-bd44-df41865c1001', 'Carlos Silva (Dono R1)', 'carlos.silva@bellanapoli.com', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b01', true, CURRENT_TIMESTAMP, NULL),
('b1a0f443-4318-4720-bd44-df41865c1002', 'Mariana Costa (Cliente R1)', 'mariana.costa@email.com', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b02', true, CURRENT_TIMESTAMP, NULL),
('b1a0f443-4318-4720-bd44-df41865c1003', 'Jose Santos (Dono R2)', 'jose.santos@sabordosertao.com', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b01', true, CURRENT_TIMESTAMP, NULL),
('b1a0f443-4318-4720-bd44-df41865c1004', 'Fernanda Lima (Cliente R2)', 'fernanda.lima@email.com', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'e1a8a992-1262-4f0e-b8d2-1c6d860d5b02', true, CURRENT_TIMESTAMP, NULL);

-- ----------------------------------------------------------------------------
-- 3. RESTAURANTES (restaurantes)
-- Vinculo com o dono_id correspondente
-- ----------------------------------------------------------------------------
INSERT INTO restaurantes (id, nome, endereco, tipo_cozinha, horario_funcionamento, dono_id, ativo, data_criacao, data_alteracao) VALUES
('c3b01f92-563b-4f68-b7e1-88892df9a001', 'Cantina Bella Roma', 'Rua Augusta, 100 - Cerqueira Cesar, Sao Paulo - SP', 'Italiana', '11:30 as 23:00', 'b1a0f443-4318-4720-bd44-df41865c1001', true, CURRENT_TIMESTAMP, NULL),
('c3b01f92-563b-4f68-b7e1-88892df9a002', 'Sabor do Sertao', 'Av. Paulista, 500 - Bela Vista, Sao Paulo - SP', 'Nordestina', '11:00 as 22:00', 'b1a0f443-4318-4720-bd44-df41865c1003', true, CURRENT_TIMESTAMP, NULL);

-- ----------------------------------------------------------------------------
-- 4. ITENS DO CARDAPIO (itens_cardapio)
-- Itens cadastrados para cada restaurante, respeitando o isolamento por restaurante_id
-- ----------------------------------------------------------------------------
INSERT INTO itens_cardapio (id, restaurante_id, nome, descricao, preco, disponivel_apenas_restaurante, foto_path, ativo, data_criacao, data_alteracao) VALUES
('f4c02a83-674c-4e79-c8f2-99903ef0b001', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'Pizza Margherita', 'Molho de tomate pelado italiano, mussarela de bufala fresca, manjericao organico e azeite extravirgem.', 65.00, false, '/imagens/cardapio/pizza-margherita.jpg', true, CURRENT_TIMESTAMP, NULL),
('f4c02a83-674c-4e79-c8f2-99903ef0b002', 'c3b01f92-563b-4f68-b7e1-88892df9a001', 'Lasanha Bolonhesa Classica', 'Massa fresca artesanal em camadas com tradicional ragu de carne bovina e suina, bechamel e parmesao.', 58.50, true, '/imagens/cardapio/lasanha-bolonhesa.jpg', true, CURRENT_TIMESTAMP, NULL),
('f4c02a83-674c-4e79-c8f2-99903ef0b003', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'Baiao de Dois Especial', 'Arroz da terra com feijao de corda fresco, queijo de coalho dourado na chapa, bacon e carne de sol desfiada.', 49.90, false, '/imagens/cardapio/baiao-de-dois.jpg', true, CURRENT_TIMESTAMP, NULL),
('f4c02a83-674c-4e79-c8f2-99903ef0b004', 'c3b01f92-563b-4f68-b7e1-88892df9a002', 'Carne de Sol com Macaxeira', 'Carne de sol maturada e grelhada na manteiga de garrafa, servida com mandioca cozida e dourada na brasa.', 62.00, false, '/imagens/cardapio/carne-de-sol.jpg', true, CURRENT_TIMESTAMP, NULL);
