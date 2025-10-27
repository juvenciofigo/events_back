-- Inserir Roles
-- INSERT INTO roles (id,authority) VALUES (1,'ROLE_ORGANIZER');
-- INSERT INTO roles (id,authority) VALUES (2,'ROLE_SUPPLIER_SERVICE');
-- INSERT INTO roles (id,authority) VALUES (3,'ROLE_ADMIN');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role, created_at, updated_at) VALUES ('159b53d7-4f74-41ad-b3af-ed512b806a32','juvencio','juvenciofigo@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','ROLE_ADMIN','2025-10-22 15:30:00','2025-10-22 15:30:00');

INSERT INTO users (id,name,email,password_hash,phone,profile_picture,role, created_at, updated_at) VALUES ('ec81164e-1f69-47ba-95c7-e011f0a682eb','juvencio','juvencio@gmail.com', '$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a','845698235','fotodoperigl','ROLE_CLIENT','2025-10-22 15:30:00','2025-10-22 15:30:00');


-- INSERT INTO suppliers_services (id, company_name, user_id, created_at, updated_at) VALUES (1,'Providences',1,'2025-10-22 15:30:00','2025-10-22 15:30:00')

-- INSERT INTO organizers (id, user_id, nome, created_at,updated_at)VALUES (1,1,'Juvencio','2025-10-22 15:30:00','2025-10-22 15:30:00')

-- -- services
-- INSERT INTO services (id,type, service_supplier_id,  category, description, price_base, created_at, updated_at)  VALUES (1,'Catering', 1, 'Food', 'Buffet completo para eventos', 1500.00, NOW(), NOW());


-- -- service_unavailability
-- INSERT INTO service_unavailability (service_id, unavailable_date) VALUES (1, '2025-10-25'), (1, '2025-11-01'), (1, '2025-11-10');

-- -- addon
-- INSERT INTO supplier_plans (id, name, description, resources, price_monthly, price_yearly, features, level, created_at, updated_at) VALUES (1,'Backstage', 'Entre no palco do marketplace.', 'Até 3 serviços, 1 álbum, visibilidade básica', 500.00, 5000.00,  '{"limite_eventos": 5, "suporte": "básico"}', 1,'2025-10-22 15:30:00','2025-10-22 15:30:00');


-- {
-- 	"id": "448b208f-0e71-47c6-b4fb-829cfd29f827",
-- 	"user": {
-- 		"id": "ec81164e-1f69-47ba-95c7-e011f0a682eb",
-- 		"name": "juvencio",
-- 		"email": "juvencio@gmail.com",
-- 		"phone": "845698235",
-- 		"passwordHash": "$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a",
-- 		"profilePicture": "fotodoperigl",
-- 		"createdAt": "2025-10-22T15:30:00",
-- 		"updatedAt": "2025-10-22T15:30:00",
-- 		"role": "ROLE_CLIENT",
-- 		"supplier": null,
-- 		"organizer": null,
-- 		"enabled": true,
-- 		"password": "$2a$10$aZ2tgJGN870hTb8fdwmW7uBs8htiQykFzor3VJ9ZDNLP8q1rHtY1a",
-- 		"authorities": [
-- 			{
-- 				"authority": "ROLE_CLIENT"
-- 			}
-- 		],
-- 		"username": "juvencio@gmail.com",
-- 		"accountNonExpired": true,
-- 		"accountNonLocked": true,
-- 		"credentialsNonExpired": true
-- 	},
-- 	"name": "hfgh",
-- 	"profilePicture": "hgfhfgh",
-- 	"createdAt": "2025-10-27T18:25:13.12401",
-- 	"updatedAt": "2025-10-27T18:25:13.12401",
-- 	"payments": null,
-- 	"receivers": null,
-- 	"events": null,
-- 	"reviews": null
-- }







