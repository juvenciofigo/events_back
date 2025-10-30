-- Inserir Roles
-- INSERT INTO roles (id,authority) VALUES (1,'ORGANIZER');
-- INSERT INTO roles (id,authority) VALUES (2,'SUPPLIER_SERVICE');
-- INSERT INTO roles (id,authority) VALUES (3,'ADMIN');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role,is_deleted, created_at, updated_at) VALUES ('159b53d7-4f74-41ad-b3af-ed512b806a32','juvencio','juvenciofigo@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','ADMIN',false,'2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role,is_deleted, created_at, updated_at) VALUES ('ec81164e-1f69-47ba-95c7-e011f0a682eb','juvencio','juvencio@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','CLIENT',false,'2025-10-22 15:30:00','2025-10-22 15:30:00');


INSERT INTO suppliers (id, company_name, user_id,description,profile_picture,logo, created_at, updated_at) VALUES ('d787df6d-17e8-444a-88c2-ad21c4117139','Providences','ec81164e-1f69-47ba-95c7-e011f0a682eb','Sem nada a dizer','Foto do profile','Foto de logo','2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO organizers (id, user_id, name,phone, created_at,updated_at)VALUES ('2bff41a2-eaad-485e-84fa-ddc1660f6965','ec81164e-1f69-47ba-95c7-e011f0a682eb','juvencio','84669548','2025-10-22 15:30:00','2025-10-22 15:30:00');

-- -- services
INSERT INTO services (id, supplier_id,category,description, price_base,created_at, updated_at ) VALUES  ('ce6f991a-2343-49b3-aa06-a27c9f9183dc', 'd787df6d-17e8-444a-88c2-ad21c4117139', 'Alimentacao', 'Aqui devia ser a descricao', '100.0', '2025-10-22 15:30:00', '2025-10-22 15:30:00');

-- Event
INSERT INTO events (id,budget_estimated,budget_spent,cover_image,created_at,date_end,date_start,description,estimated_guest,event_status,is_public,organizer_id,title,type,updated_at) VALUES ('fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c',150000.00,0.00,'https://example.com/imagem-casamento.jpg','2025-10-28 20:00:00','2025-12-20 22:00:00', '2025-12-20 15:00:00', 'Casamento luxuoso na praia de Maputo', 200, 'PLANNED',TRUE,'2bff41a2-eaad-485e-84fa-ddc1660f6965', 'Casamento João & Maria', 'Casamento', '2025-10-28 20:00:00');

-- Ticket
INSERT INTO tickets (code, created_at, event_id, notes, price_paid, responded_at, seat, send_at, status, total_people, updated_at, id) VALUES ('013e1843-1154-40a3-9cba-d4a818f18519', '2025-10-29 23:20:22.145442', 'fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c', 'notas do ticket',0.00, null, 'Mesa x', '2025-10-29 23:20:22.145442', 'PENDING', 2,'2025-10-29 23:20:22.145442', '1c7b184c-46cd-4ef6-9ae8-86e58c588f6d')

-- Gests
INSERT INTO guests (created_at, email, name, phone, ticket_id, updated_at, id) VALUES ('2025-10-29 23:20:22.159661', 'juvenciofigo@gmail.com', 'juvenciofigo@gmail.com', 'juvenciofigo@gmail.com', '1c7b184c-46cd-4ef6-9ae8-86e58c588f6d', '2025-10-29 23:20:22.159661', '2025-10-29 23:20:22.159661')

-- Album
 INSERT INTO albums (created_at, description, service_id, title, updated_at, id) VALUES ('2025-10-30 02:35:51.156768', 'Descricao do algum', 'ce6f991a-2343-49b3-aa06-a27c9f9183dc', 'Título do album', '2025-10-30 02:35:51.156768', '341c9554-9dd5-478b-a11e-36c6a92887d9')

-- Organizer plan
INSERT INTO organizer_plans (created_at, description, features, level, name, price_monthly, price_yearly, resources, updated_at, id) VALUES ('2025-10-30 18:49:04.672495', 'Acesso completo a todas as funcionalidades','{"maxEvents": "ilimitado", "analytics": true}', 3, 'Plano Premium', 199.99, 1999.0, 'Eventos ilimitados, suporte 24h','2025-10-30 18:49:04.672495','7c7cf4b1-0cd0-4b9e-ae26-22f0c96be8f5');

-- ServiceEntity(id=, supplier=SupplierEntity(), category=hfgh, description=null, priceBase=10.0, unavailability=null, createdAt=2025-10-28T02:09:44.399494900, updatedAt=2025-10-28T02:09:44.399494900, payments=null, albums=null, servicesHasEvent=null, reviews=null)

-- -- service_unavailability
-- INSERT INTO service_unavailability (service_id, unavailable_date) VALUES (1, '2025-10-25'), (1, '2025-11-01'), (1, '2025-11-10');

-- -- addon
-- INSERT INTO supplier_plans (id, name, description, resources, price_monthly, price_yearly, features, level, created_at, updated_at) VALUES (1,'Backstage', 'Entre no palco do marketplace.', 'Até 3 serviços, 1 álbum, visibilidade básica', 500.00, 5000.00,  '{"limite_eventos": 5, "suporte": "básico"}', 1,'2025-10-22 15:30:00','2025-10-22 15:30:00');








-- Hibernate: 
--     select
--         se1_0.id,
--         se1_0.company_name,
--         se1_0.created_at,
--         se1_0.description,
--         l1_0.id,
--         l1_0.address,
--         l1_0.city,
--         l1_0.country,
--         l1_0.created_at,
--         l1_0.description,
--         l1_0.latitude,
--         l1_0.longitude,
--         l1_0.name,
--         l1_0.photo,
--         l1_0.province,
--         l1_0.update_at,
--         se1_0.logo,
--         se1_0.profile_picture,
--         se1_0.updated_at,
--         se1_0.user_id
--     from
--         suppliers se1_0
--     left join
--         locations l1_0
--             on l1_0.id=se1_0.location_id
--     where
--         se1_0.id=?
-- Hibernate: 
--     select
--         se1_0.id,
--         se1_0.category,
--         se1_0.created_at,
--         se1_0.description,
--         se1_0.price_base,
--         se1_0.supplier_id,
--         se1_0.updated_at
--     from
--         services se1_0
--     left join
--         suppliers s1_0
--             on s1_0.id=se1_0.supplier_id
--     where
--         s1_0.id=?