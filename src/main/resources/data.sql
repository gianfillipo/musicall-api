INSERT INTO instrument (name) VALUES
('Guitarra'),
('Baixo'),
('Bateria'),
('Teclado'),
('Piano'),
('Violino'),
('Trompete'),
('Saxofone'),
('Clarinete'),
('Flauta'),
('Violão'),
('Cavaquinho'),
('Pandeiro'),
('Voz'),
('Tamborim');

INSERT INTO user (birth_date, confirmation_token, cpf, email, is_confirmed, is_password_reset_requested, name, number_of_events, password, password_reset_token, telephone, type)
VALUES ('2000-01-01', 'c111cf08-a6e8-4611-b624-e1d4c4a45978', '12345678901', 'john.doe@example.com', true, false, 'John', 0, '$2a$10$H9BHHHpnGo2lrMCeGGcRWuZC5Pt9pL9K6dGz3yNCorqw0LoM1l.fS', '', '99999999999', 'ORG'),
       ('2000-01-01', 'c111cf08-a6e8-4611-b624-e1d4c4a45978', '12345678902', 'john3.doe@example.com', true, false, 'John2   ', 0, '$2a$10$H9BHHHpnGo2lrMCeGGcRWuZC5Pt9pL9K6dGz3yNCorqw0LoM1l.fS', '', '99929999999', 'ORG'),
       ('2000-01-01', 'c111cf08-a6e8-4611-b624-e1d4c4a45978', '12345678903', 'john2.doe@example.com', true, false, 'John', 0, '$2a$10$IgJaWyQfnW1Vu7huiZ5Ht.hrWb0xKhGWIrTPciOzriBdxslN.4qXK', '', '99919999999', 'MSC'),
       ('1994-07-21', 's5t7u9v1w3x5', '12345678913', 'musician4@example.com', true, false, 'Roberto Andrade', 0, '$2a$10$igjawyqfnw1vu7huiz5ht.hrwb0xkhgwirtpciozribdxsln.4qxk', '', '99919999989', 'MSC'),
       ('1991-03-17', 'y0z1a2b3c4d5', '12345678914', 'musician5@example.com', true, false, 'Erick Mendes', 0, '$2a$10$igjawyqfnw1vu7huiz5ht.hrwb0xkhgwirtpciozribdxsln.4qxk', '', '98919999999', 'MSC'),
       ('1988-11-04', 'e6f8g0h2i4j6', '12345678915', 'musician6@example.com', true, false, 'Jefferson Santana', 0, '$2a$10$igjawyqfnw1vu7huiz5ht.hrwb0xkhgwirtpciozribdxsln.4qxk', '', '99919999949', 'MSC'),
       ('1993-02-28', 'k8l0m2n4o6p8', '12345678916', 'musician7@example.com', true, false, 'Paula Nobre', 0, '$2a$10$igjawyqfnw1vu7huiz5ht.hrwb0xkhgwirtpciozribdxsln.4qxk', '', '99919999929', 'MSC'),
       ('1987-06-12', 'q0r2s4t6u8v0', '12345678917', 'musician8@example.com', true, false, 'Manuel Oliveira', 0, '$2a$10$igjawyqfnw1vu7huiz5ht.hrwb0xkhgwirtpciozribdxsln.4qxk', '', '99919999923', 'MSC'),
       ('1996-10-03', 'w2x4y6z8a0b2', '12345678918', 'musician9@example.com', true, false, 'Lívia Anjos', 0, '$2a$10$igjawyqfnw1vu7huiz5ht.hrwb0xkhgwirtpciozribdxsln.4qx', '', '99959999929', 'MSC'),
       ('1999-08-19', 'c4d6e8f0g2h4', '12345678919', 'musician10@example.com', true, false, 'Lucas Navasconi', 0, '$2a$10$igjawyqfnw1vu7huiz5ht.hrwb0xkhgwirtpciozribdxsln.4qx', '', '92919999923', 'MSC');


INSERT INTO musician (user_id, cep, description, image_url)
VALUES (3, '41336610', 'Tá ligado né pai', 'https://images.pexels.com/photos/210922/pexels-photo-210922.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'),
       (4, '57045752', 'Tá ligado né pai', 'https://images.pexels.com/photos/3771793/pexels-photo-3771793.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'),
       (5, '38067360', 'Tá ligado né pai', 'https://images.pexels.com/photos/2531728/pexels-photo-2531728.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'),
       (6, '64600100', 'Tá ligado né pai', 'https://images.pexels.com/photos/243989/pexels-photo-243989.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'),
       (7, '59082100', 'Tá ligado né pai', 'https://images.pexels.com/photos/1885213/pexels-photo-1885213.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'),
       (8, '59124380', 'Tá ligado né pai', 'https://images.pexels.com/photos/325688/pexels-photo-325688.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1');

INSERT INTO event (user_id, name, about_event, cep, number, complement, event_date, duration_hours, start_hour, finalized, image_url)
VALUES
  (1, 'Show de Rock', 'sei lá', '01406000', 2, 'My Complement', '2024-01-02', 2, '00:00:00', true, 'https://images.pexels.com/photos/2592179/pexels-photo-2592179.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'),
  (1, 'Barzinho do seu Zé', 'sei lá', '01406000', 2, 'My Complement', '2024-11-02', 2, '00:00:00', true, 'https://images.pexels.com/photos/2774556/pexels-photo-2774556.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'John Irish Pub', 'sei lá', '01406000', 2, 'My Complement', '2024-01-04', 2, '00:00:00', false, 'https://images.pexels.com/photos/587741/pexels-photo-587741.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Balada Blitz', 'sei lá', '01406000', 2, 'My Complement', '2024-01-06', 2, '00:00:00', false, 'https://images.pexels.com/photos/1190297/pexels-photo-1190297.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Assembleia de Deus', 'sei lá', '01406000', 2, 'My Complement', '2023-12-09', 2, '00:00:00', false, 'https://images.pexels.com/photos/1190297/pexels-photo-1190297.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Jazz Bar', 'sei lá', '01406000', 2, 'My Complement', '2023-12-10', 2, '00:00:00', false, 'https://images.pexels.com/photos/919734/pexels-photo-919734.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Emocenter', 'sei lá', '01406000', 2, 'My Complement', '2023-12-15', 2, '00:00:00', false, 'https://images.pexels.com/photos/2747446/pexels-photo-2747446.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Reggaetron', 'sei lá', '01406000', 2, 'My Complement', '2023-12-17', 2, '00:00:00', false, 'https://images.pexels.com/photos/313707/pexels-photo-313707.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Barzinho do Patrik', 'sei lá', '01406000', 2, 'My Complement', '2023-12-11', 2, '00:00:00', false, 'https://images.pexels.com/photos/919734/pexels-photo-919734.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Vila JK', 'sei lá', '01406000', 2, 'My Complement', '2023-12-19', 2, '00:00:00', false, 'https://images.pexels.com/photos/265947/pexels-photo-265947.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Avenue', 'sei lá', '01406000', 2, 'My Complement', '2023-12-18', 2, '00:00:00', false, 'https://images.pexels.com/photos/265947/pexels-photo-265947.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Vip Station', 'sei lá', '01406000', 2, 'My Complement', '2023-12-20', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Seu Clemente Bar', 'sei lá', '01406000', 2, 'My Complement', '2023-12-21', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Emo Bar', 'sei lá', '01406000', 2, 'My Complement', '2023-12-21', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Igreja da Sé', 'sei lá', '01406000', 2, 'My Complement', '2023-09-15', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Cristo Redentor', 'sei lá', '01406000', 2, 'My Complement', '2023-09-16', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'SPTECH Music Talent', 'sei lá', '01406000', 2, 'My Complement', '2023-09-19', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Bar do Jaú', 'sei lá', '01406000', 2, 'My Complement', '2023-10-19', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Blitz', 'sei lá', '01406000', 2, 'My Complement', '2023-10-21', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Bonjour', 'sei lá', '01406000', 2, 'My Complement', '2023-11-21', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600'),
  (1, 'Baile da rua 2', 'sei lá', '01406000', 2, 'My Complement', '2023-10-10', 2, '00:00:00', false, 'https://images.pexels.com/photos/1652353/pexels-photo-1652353.jpeg?auto=compress&cs=tinysrgb&w=1600');


INSERT INTO event_job (event_id, instrument_id, musician_id, payment)
VALUES
  (2, 2, 1, 10.0),
  (1, 3, 1, 10.0),
  (3, 4, 1, 10.0),
  (1, 5, 3, 10.0),
  (1, 6, 4, 10.0),
  (2, 2, 1, 10.0),
  (2, 3, 2, 10.0),
  (2, 4, 3, 10.0),
  (2, 5, 4, 10.0),
  (2, 6, NULL, 10.0),
  (3, 2, NULL, 10.0),
  (3, 3, NULL, 10.0),
  (3, 4, NULL, 10.0),
  (3, 5, NULL, 10.0),
  (3, 6, NULL, 10.0),
  (4, 10, NULL, 10.0),
  (4, 12, NULL, 10.0),
  (4, 5, NULL, 10.0),
  (4, 7, NULL, 10.0),
  (4, 8, NULL, 10.0),
    (5, 3, NULL, 10.0),
    (5, 2, NULL, 10.0),
    (5, 1, NULL, 10.0),
    (5, 9, NULL, 10.0),
      (6, 11, NULL, 10.0),
      (6, 13, NULL, 10.0),
      (6, 15, NULL, 10.0),
      (4, 6, NULL, 10.0),
        (7, 13, NULL, 10.0),
        (7, 2, NULL, 10.0),
        (7, 1, NULL, 10.0),
        (7, 9, NULL, 10.0),
          (8, 7, NULL, 10.0),
          (8, 8, NULL, 10.0),
          (8, 10, NULL, 10.0),
          (8, 12, NULL, 10.0),
            (9, 12, NULL, 10.0),
            (9, 14, NULL, 10.0),
            (9, 9, NULL, 10.0),
            (9, 8, NULL, 10.0),
              (10, 15, NULL, 10.0),
              (10, 13, NULL, 10.0),
              (10, 12, NULL, 10.0),
  (11, 2, NULL, 10.0),
  (11, 3, NULL, 10.0),
  (14, 2, NULL, 10.0),
  (14, 2, NULL, 10.0),
  (14, 3, NULL, 10.0),
  (14, 3, NULL, 10.0),
  (14, 3, NULL, 10.0),
  (14, 3, NULL, 10.0),
  (11, 3, NULL, 10.0);

INSERT INTO musician_instrument (musician_id, instrument_id) VALUES
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(2, 2),
(2, 3),
(3, 2),
(4, 2),
(5, 2),
(6, 2);


INSERT INTO job_request (event_job_id, musician_id, organizer_confirmed, musician_confirmed) VALUES
(1, 3, 1, 1),
(3, 1, 1, 1),
(2, 1, 1, 1),
(6, 3, 1, 0);


INSERT INTO notification (job_request_id, notification_type, user_id) VALUES
(1, 1, 1),
(2, 1, 1),
(3, 1, 1),
(4, 1, 3);

INSERT INTO prospect (email, name, telefone, midia) VALUES
('teste@gmail.com', 'Thiago Silva', '11 92599-5591', 'Facebook')
