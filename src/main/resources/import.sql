-- Inserir Roles
-- INSERT INTO roles (id,authority) VALUES (1,'ROLE_ORGANIZER');
-- INSERT INTO roles (id,authority) VALUES (2,'ROLE_SUPPLIER_SERVICE');
-- INSERT INTO roles (id,authority) VALUES (3,'ROLE_ADMIN');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role,is_deleted, created_at, updated_at) VALUES ('159b53d7-4f74-41ad-b3af-ed512b806a32','juvencio','juvenciofigo@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','ROLE_ADMIN',false,'2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role,is_deleted, created_at, updated_at) VALUES ('ec81164e-1f69-47ba-95c7-e011f0a682eb','juvencio','juvencio@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','ROLE_CLIENT',false,'2025-10-22 15:30:00','2025-10-22 15:30:00');


INSERT INTO suppliers (id, company_name, user_id,description,profile_picture,logo, created_at, updated_at) VALUES ('d787df6d-17e8-444a-88c2-ad21c4117139','Providences','ec81164e-1f69-47ba-95c7-e011f0a682eb','Sem nada a dizer','Foto do profile','Foto de logo','2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO organizers (id, user_id, name,phone, created_at,updated_at)VALUES ('2bff41a2-eaad-485e-84fa-ddc1660f6965','ec81164e-1f69-47ba-95c7-e011f0a682eb','juvencio','84669548','2025-10-22 15:30:00','2025-10-22 15:30:00');

-- -- services
-- INSERT INTO services (id, supplier_id,category,description, price_base,created_at, updated_at ) VALUES  ('ce6f991a-2343-49b3-aa06-a27c9f9183dc', 'd787df6d-17e8-444a-88c2-ad21c4117139', 'Alimentacao', 'Aqui devia ser a descricao', '100.0', '2025-10-22 15:30:00', '2025-10-22 15:30:00');

-- ServiceEntity(id=, supplier=SupplierEntity(), category=hfgh, description=null, priceBase=10.0, unavailability=null, createdAt=2025-10-28T02:09:44.399494900, updatedAt=2025-10-28T02:09:44.399494900, payments=null, albums=null, servicesHasEvent=null, reviews=null)

-- -- service_unavailability
-- INSERT INTO service_unavailability (service_id, unavailable_date) VALUES (1, '2025-10-25'), (1, '2025-11-01'), (1, '2025-11-10');

-- -- addon
-- INSERT INTO supplier_plans (id, name, description, resources, price_monthly, price_yearly, features, level, created_at, updated_at) VALUES (1,'Backstage', 'Entre no palco do marketplace.', 'Até 3 serviços, 1 álbum, visibilidade básica', 500.00, 5000.00,  '{"limite_eventos": 5, "suporte": "básico"}', 1,'2025-10-22 15:30:00','2025-10-22 15:30:00');








