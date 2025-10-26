-- Inserir Roles
-- INSERT INTO roles (id,authority) VALUES (1,'ROLE_ORGANIZER');
-- INSERT INTO roles (id,authority) VALUES (2,'ROLE_SUPPLIER_SERVICE');
-- INSERT INTO roles (id,authority) VALUES (3,'ROLE_ADMIN');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role, created_at, updated_at) VALUES (RANDOM_UUID(),'juvencio','juvenciofigo@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','ROLE_ADMIN','2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role, created_at, updated_at) VALUES (RANDOM_UUID(),'juvencio','juvencio@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','ROLE_CLIENT','2025-10-22 15:30:00','2025-10-22 15:30:00');


-- INSERT INTO suppliers_services (id, company_name, user_id, created_at, updated_at) VALUES (1,'Providences',1,'2025-10-22 15:30:00','2025-10-22 15:30:00')

-- INSERT INTO organizers (id, user_id, nome, created_at,updated_at)VALUES (1,1,'Juvencio','2025-10-22 15:30:00','2025-10-22 15:30:00')

-- -- services
-- INSERT INTO services (id,type, service_supplier_id,  category, description, price_base, created_at, updated_at)  VALUES (1,'Catering', 1, 'Food', 'Buffet completo para eventos', 1500.00, NOW(), NOW());


-- -- service_unavailability
-- INSERT INTO service_unavailability (service_id, unavailable_date) VALUES (1, '2025-10-25'), (1, '2025-11-01'), (1, '2025-11-10');

-- -- addon
-- INSERT INTO supplier_plans (id, name, description, resources, price_monthly, price_yearly, features, level, created_at, updated_at) VALUES (1,'Backstage', 'Entre no palco do marketplace.', 'Até 3 serviços, 1 álbum, visibilidade básica', 500.00, 5000.00,  '{"limite_eventos": 5, "suporte": "básico"}', 1,'2025-10-22 15:30:00','2025-10-22 15:30:00');










