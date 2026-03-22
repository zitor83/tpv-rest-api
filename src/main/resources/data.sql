INSERT INTO products (id, name, price, category, description, image_url, available, default_ingredients) VALUES
('11111111-1111-1111-1111-111111111111', 'Hamburguesa Clasica', 8.50, 'Principal', 'Hamburguesa clasica con pan brioche y carne de vacuno.', 'https://cdn.tpv.local/img/hamburguesa-clasica.jpg', TRUE, '["Pan brioche", "Carne de vacuno", "Lechuga", "Tomate"]'),
('22222222-2222-2222-2222-222222222222', 'Cheeseburger', 9.50, 'Principal', 'Hamburguesa con doble queso cheddar y salsa especial.', 'https://cdn.tpv.local/img/cheeseburger.jpg', TRUE, '["Pan brioche", "Carne de vacuno", "Queso cheddar", "Salsa especial"]'),
('33333333-3333-3333-3333-333333333333', 'Patatas Fritas', 3.50, 'Entrante', 'Racion de patatas fritas crujientes con sal fina.', 'https://cdn.tpv.local/img/patatas-fritas.jpg', TRUE, '["Patatas", "Aceite", "Sal"]'),
('44444444-4444-4444-4444-444444444444', 'Refresco de Cola', 2.50, 'Bebida', 'Bebida refrescante de cola servida fria.', 'https://cdn.tpv.local/img/refresco-cola.jpg', TRUE, '["Refresco de cola", "Hielo"]'),
('55555555-5555-5555-5555-555555555555', 'Tarta de Queso', 4.50, 'Postre', 'Porcion de tarta de queso horneada con base de galleta.', 'https://cdn.tpv.local/img/tarta-queso.jpg', TRUE, '["Queso crema", "Huevos", "Azucar", "Galleta"]');

INSERT INTO users (username, password, role) VALUES
('admin', '$2b$10$Au8yB./sR8ePqOCR0..tUex5ckRFlFYXOD6RsW/U6Q/I2GG36xlaK', 'ROLE_ADMIN');

