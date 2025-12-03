-- Inserir Roles
-- INSERT INTO roles (id,authority) VALUES (1,'ORGANIZER');
-- INSERT INTO roles (id,authority) VALUES (2,'SUPPLIER_SERVICE');
-- INSERT INTO roles (id,authority) VALUES (3,'ADMIN');

INSERT INTO users (id,name,email,password_hash,phone, role,is_deleted, created_at, updated_at) VALUES ('159b53d7-4f74-41ad-b3af-ed512b806a32','juvencio','juvenciofigo@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235', 'ADMIN',false,'2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO users (id,name,email,password_hash,phone, role,is_deleted, created_at, updated_at) VALUES ('ec81164e-1f69-47ba-95c7-e011f0a682eb','juvencio','juvencio@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235', 'CLIENT',false,'2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO users (id,name,email,password_hash,phone, role,is_deleted, created_at, updated_at) VALUES ('ec81164e-1f69-47ba-95c7-e011f0a682er','figo','figo@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235', 'CLIENT',false,'2025-10-22 15:30:00','2025-10-22 15:30:00');


INSERT INTO suppliers (id, company_name, user_id,description,profile_picture,logo, created_at, updated_at) VALUES ('d787df6d-17e8-444a-88c2-ad21c4117139','Providences','ec81164e-1f69-47ba-95c7-e011f0a682eb','Sem nada a dizer','Foto do profile','Foto de logo','2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO suppliers (id, company_name, user_id,description,profile_picture,logo, created_at, updated_at) VALUES ('d787df6d-17e8-444a-88c2-ad21c411713','Levezza','ec81164e-1f69-47ba-95c7-e011f0a682er','Sem nada a dizer','Foto do profile','Foto de logo','2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO organizers (id, user_id, company_name,phone, created_at,updated_at)VALUES ('2bff41a2-eaad-485e-84fa-ddc1660f6965','ec81164e-1f69-47ba-95c7-e011f0a682eb','juvencio`s','84669548','2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO organizers (id, user_id, company_name,phone, created_at,updated_at)VALUES ('2bff41a2-eaad-485e-84fa-ddc1660f6997','ec81164e-1f69-47ba-95c7-e011f0a682er','figo','84669548','2025-10-22 15:30:00','2025-10-22 15:30:00');

-- -- services
INSERT INTO services (id, supplier_id,category,description, price_base,created_at, updated_at ) VALUES  ('ce6f991a-2343-49b3-aa06-a27c9f9183dc', 'd787df6d-17e8-444a-88c2-ad21c4117139', 'Alimentacao', 'Aqui devia ser a descricao', '100.0', '2025-10-22 15:30:00', '2025-10-22 15:30:00');

-- Event
INSERT INTO events (id,budget_estimated,budget_spent,cover_image,created_at,date_end,date_start,description,estimated_guest,event_status,is_public,organizer_id,title,category,updated_at) VALUES ('fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c',150000.00,0.00,'https://firebasestorage.googleapis.com/v0/b/lojaprovidences-fotos.firebasestorage.app/o/d869afb6-2631-4d3a-94dd-4f8b75d0a996-ChatGPT+Image+18_11_2025%2C+18_29_10.png?alt=media','2025-10-28 20:00:00','2025-12-20 22:00:00', '2025-12-20 15:00:00', 'Casamento luxuoso na praia de Maputo', 200, 'PLANNED',TRUE,'2bff41a2-eaad-485e-84fa-ddc1660f6965', 'Casamento João & Maria', 'Casamento', '2025-10-28 20:00:00');

INSERT INTO events (id,budget_estimated,budget_spent,cover_image,created_at,date_end,date_start,description,estimated_guest,event_status,is_public,organizer_id,title,category,updated_at) VALUES ('fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c',150000.00,0.00,'https://firebasestorage.googleapis.com/v0/b/lojaprovidences-fotos.firebasestorage.app/o/62f090ff-a22a-46cc-92a3-c80d3880d2b0-s (1).png?alt=media','2025-10-28 20:00:00','2025-12-20 22:00:00', '2025-12-20 15:00:00', 'Casamento luxuoso na praia de Maputo', 200, 'PLANNED',TRUE,'2bff41a2-eaad-485e-84fa-ddc1660f6965', 'Aniversário da Lana', 'Casamento', '2025-10-28 20:00:00');
INSERT INTO events (id,budget_estimated,budget_spent,cover_image,created_at,date_end,date_start,description,estimated_guest,event_status,is_public,organizer_id,title,category,updated_at) VALUES ('fe3c85b9-5ee5-4e88-b17c-38d65d7af134',150000.00,0.00,'https://firebasestorage.googleapis.com/v0/b/lojaprovidences-fotos.firebasestorage.app/o/d869afb6-2631-4d3a-94dd-4f8b75d0a996-ChatGPT+Image+18_11_2025%2C+18_29_10.png?alt=media','2025-10-28 20:00:00','2025-12-20 22:00:00', '2025-12-20 15:00:00', 'Casamento luxuoso na praia de Maputo', 200, 'PLANNED',TRUE,'2bff41a2-eaad-485e-84fa-ddc1660f6997', 'Casamento João & Maria', 'Casamento', '2025-10-28 20:00:00');

-- Seats
INSERT INTO seats (created_at, description, event_id, layout_position_x, layout_position_y, name, total_seats, updated_at, id, is_paid, price, available_seats) VALUES ('2025-11-05 00:02:36.834399	', 'Descricao do assento', 'fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c', null, null, 'Mesa A', 10, '2025-11-05 00:02:36.834399', 'ed961e6d-0f99-45a0-be9a-4c87a6c80af7', true, 200,8);
INSERT INTO seats (created_at, description, event_id, layout_position_x, layout_position_y, name, total_seats, updated_at, id, is_paid, price, available_seats) VALUES ('2025-11-05 00:02:36.834399	', 'Descricao do assento', 'fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c', null, null, 'Mesa B', 10, '2025-11-05 00:02:36.834399', 'ed961e6d-0f99-45a0-be9a-4c87a6c80', false, 200,10);

-- Ticket
INSERT INTO tickets (ticket_code,access_token,created_at, event_id, notes, responded_at, send_at, ticket_status, total_people, updated_at, id, seat_id) VALUES ('013e1843-1154-40a3-9cba-d4a818f18519','1', '2025-10-29 23:20:22.145442', 'fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c', 'notas do ticket', null, '2025-10-29 23:20:22.145442', 'PENDING', 2,'2025-10-29 23:20:22.145442', '1c7b184c-46cd-4ef6-9ae8-86e58c588f6d', 'ed961e6d-0f99-45a0-be9a-4c87a6c80af7');

-- Gests
INSERT INTO guests (created_at, email, name, phone, ticket_id, updated_at, id) VALUES ('2025-11-07 05:07:03.493465','juvenciofigo@gmail.com','juvrncio','845678954','1c7b184c-46cd-4ef6-9ae8-86e58c588f6d','2025-11-07 05:07:03.493465','6ea20085-7477-4c98-981d-64eecc8ff0f7');

-- Chats
INSERT INTO chats (created_at, event_id, title, type, updated_at, id) values ('2025-11-07 05:14:48.753768', 'fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c', 'Convidados', 'GUESTS', '2025-11-07 05:14:48.753768', '2aaff332-fb3a-4fa3-8bd9-ef550d25c262');

INSERT INTO chats (created_at, event_id, title, type, updated_at, id) values ('2025-11-07 05:14:48.753768','fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c', 'Fornecedor Leveza', 'SUPPLIER', '2025-11-07 05:14:48.753768', '2aaff332-fb3a-4fa-8bd9-ef550d25c26');

-- Participants
INSERT INTO participant_chat (chat_id, created_at, guest_id, organizer_id, supplier_id, type, updated_at, id, is_removed) values ('2aaff332-fb3a-4fa3-8bd9-ef550d25c262', '2025-11-07 05:14:48.753768','6ea20085-7477-4c98-981d-64eecc8ff0f7', null, null, 'GUEST', '2025-11-07 05:14:48.753768', '197eb488-761a-417e-9407-4aa0a16d3527', false);

INSERT INTO participant_chat (chat_id, created_at, guest_id, organizer_id, supplier_id, type, updated_at, id, is_removed) values ('2aaff332-fb3a-4fa3-8bd9-ef550d25c262', '2025-11-07 05:14:48.753768',null, '2bff41a2-eaad-485e-84fa-ddc1660f6965', null, 'ORGANIZER', '2025-11-07 05:14:48.753768', '197eb488-761a-417e-9407-4aa0a16d3523', false);

INSERT INTO participant_chat (chat_id, created_at, guest_id, organizer_id, supplier_id, type, updated_at, id, is_removed) values ('2aaff332-fb3a-4fa-8bd9-ef550d25c26', '2025-11-07 05:14:48.753768',null, '2bff41a2-eaad-485e-84fa-ddc1660f6965', null, 'ORGANIZER', '2025-11-07 05:14:48.753768', '197eb488-761a-417e-9407-4aa0a16d35', false);

INSERT INTO participant_chat (chat_id, created_at, guest_id, organizer_id, supplier_id, type, updated_at, id, is_removed) values ('2aaff332-fb3a-4fa-8bd9-ef550d25c26', '2025-11-07 05:14:48.753768' ,null, null, 'd787df6d-17e8-444a-88c2-ad21c411713', 'SUPPLIER', '2025-11-07 05:14:48.753768', '197eb488-761a-417e-9407-4aa0a6d3523', false);

-- Album
 INSERT INTO albums (created_at, description, service_id, title, updated_at, id) VALUES ('2025-10-30 02:35:51.156768', 'Descricao do algum', 'ce6f991a-2343-49b3-aa06-a27c9f9183dc', 'Título do album', '2025-10-30 02:35:51.156768', '341c9554-9dd5-478b-a11e-36c6a92887d9')

-- plan
INSERT INTO plans (created_at, description, features, level, name, price_monthly, price_yearly, resources, updated_at, id,plan_type) VALUES ('2025-10-30 18:49:04.672495', 'Acesso completo a todas as funcionalidades','{"maxEvents": "ilimitado", "analytics": true}', 3, 'Plano Premium', 199.99, 1999.0, 'Eventos ilimitados, suporte 24h','2025-10-30 18:49:04.672495','7c7cf4b1-0cd0-4b9e-ae26-22f0c96be8f5','ORGANIZER');
INSERT INTO plans (created_at, description, features, level, name, price_monthly, price_yearly, resources, updated_at, id,plan_type) VALUES ('2025-10-30 18:49:04.672495', 'Acesso completo a todas as funcionalidades','{"maxEvents": "ilimitado", "analytics": true}', 3, 'Plano Ouro', 199.99, 1999.0, 'Eventos ilimitados, suporte 24h','2025-10-30 18:49:04.672495','7c7cf4b1-0cd0-4b9e-ae26-22f0c96be8f','SUPPLIER');

-- Task
INSERT INTO tasks (created_at, description, due_date, event_id, priority, responsible_name, responsible_phone, task_status, title, updated_at, id) VALUES ('2025-11-13T17:45:57.1584211', 'tarefa', '2025-10-22T15:30:00', 'fe3c85b9-5ee5-4e88-b17c-38d65d7afc8c', 'MEDIUM', 'Nome do reponsavel', '856526698', 'IN_PROGRESS', 'tarefa', '2025-11-13T18:34:24.7599971', '2f50b8b6-8587-406b-bf0f-9004ec66d191');

-- medias
INSERT INTO medias_album (created_at, file_url, media_type, album_id, id, organizer_id, supplier_id) VALUES ('2025-11-13T17:45:57.1584211', 'https://firebasestorage.googleapis.com/v0/b/lojaprovidences-fotos.firebasestorage.app/o/a7e6f035-2781-4755-8427-3872c3aa8a81-20230818_172833.jpg?alt=media', 'IMAGE', '341c9554-9dd5-478b-a11e-36c6a92887d9', 'c9ec0138-9d14-4b34-b6ff-2e440b30e8fa', null, null);

INSERT INTO medias_album (created_at, file_url, media_type, album_id, id, organizer_id, supplier_id) VALUES ('2025-11-13T17:45:57.1584211', 'https://firebasestorage.googleapis.com/v0/b/lojaprovidences-fotos.firebasestorage.app/o/e3899ce7-df7b-402d-863e-ddc9157ed7ff-20230818_172833.jpg?alt=media', 'IMAGE', '341c9554-9dd5-478b-a11e-36c6a92887d9', 'c9ec0138-9d14-4b34-b6ff-2e440b30e8f', null, null);

-- ServiceEntity(id=, supplier=SupplierEntity(), category=hfgh, description=null, priceBase=10.0, unavailability=null, createdAt=2025-10-28T02:09:44.399494900, updatedAt=2025-10-28T02:09:44.399494900, payments=null, albums=null, servicesHasEvent=null, reviews=null)

-- -- service_unavailability
-- INSERT INTO service_unavailability (service_id, unavailable_date) VALUES (1, '2025-10-25'), (1, '2025-11-01'), (1, '2025-11-10');

-- -- addon
-- INSERT INTO supplier_plans (id, name, description, resources, price_monthly, price_yearly, features, level, created_at, updated_at) VALUES (1,'Backstage', 'Entre no palco do marketplace.', 'Até 3 serviços, 1 álbum, visibilidade básica', 500.00, 5000.00,  '{"limite_eventos": 5, "suporte": "básico"}', 1,'2025-10-22 15:30:00','2025-10-22 15:30:00');

-- Payment
INSERT INTO payments ( AMOUNT, RECEIVER_PLATFORM, CREATED_AT, UPDATE_AT, DESCRIPTION, ID, PAYER_GUEST_ID, PAYER_ORGANIZER_ID, PAYER_SUPPLIER_ID, RECEIVER_ORGANIZER_ID, RECEIVER_SUPPLIER_ID, SEAT_ID, SERVICES_ID, SUBSCRIPTION_ID, CURRENCY, PAYER_TYPE, PAYMENT_METHOD, PAYMENT_TARGET, RECEIVER_TYPE, STATUS) VALUES (400.00, null, '2025-11-27 00:56:46.3456', '2025-11-27 00:56:46.3456', 'Descrição', '2d79c44a-83da-4778-939b-f77aea03c68f', '6ea20085-7477-4c98-981d-64eecc8ff0f7', null, null, '2bff41a2-eaad-485e-84fa-ddc1660f6965', null, 'ed961e6d-0f99-45a0-be9a-4c87a6c80af7', null, null, 'MZN', 'GUEST', 'MPESA', 'SEAT', 'ORGANIZER', 'PENDING');

-- payment references
INSERT INTO payment_reference (created_at, gateway_response, payment_id, reference_code, transaction_reference, id) VALUES ('2025-11-13T17:45:57.1584211', '"{\"output_ResponseCode\":\"INS-0\",\"output_ResponseDesc\":\"Request processed successfully\",\"output_TransactionID\":\"2gk7uq2vad02\",\"output_ConversationID\":\"ea89db0122f24571a758e76b81b80dde\",\"output_ThirdPartyReference\":\"REFBC5BF7B3\"}"', '2d79c44a-83da-4778-939b-f77aea03c68f', 'REFBC5BF7B3', 'TXN302587B89CA94', 'e0c0df2c-795c-4d1b-8bbe-8e841f9a5fb8');




