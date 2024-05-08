INSERT INTO TB_CITY (NAME) VALUES ('New Gotham');
INSERT INTO TB_CITY (NAME) VALUES ('Ocean Bay');
INSERT INTO TB_CITY (NAME) VALUES ('Emerald City');
INSERT INTO TB_CITY (NAME) VALUES ('Crystal Lake');
INSERT INTO TB_CITY (NAME) VALUES ('Silverwood');
INSERT INTO TB_CITY (NAME) VALUES ('River Heights');
INSERT INTO TB_CITY (NAME) VALUES ('Maple Grove');
INSERT INTO TB_CITY (NAME) VALUES ('Thunder Ridge');
INSERT INTO TB_CITY (NAME) VALUES ('Sapphire Isle');
INSERT INTO TB_CITY (NAME) VALUES ('Golden Valley');

INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-05-05', 1, 'Music Festival', 'http://musicfestival.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-06-15', 2, 'Food Fair', 'http://foodfair.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-07-10', 3, 'Art Exhibition', 'http://artexhibition.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-08-20', 4, 'Theater Play', 'http://theaterplay.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-09-25', 5, 'Tech Conference', 'http://techconference.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-10-05', 6, 'Sports Tournament', 'http://sportstournament.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-11-15', 7, 'Literary Festival', 'http://literaryfestival.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2024-12-10', 8, 'Jazz Night', 'http://jazznight.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2025-01-20', 9, 'Film Festival', 'http://filmfestival.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2025-02-05', 10, 'Dance Performance', 'http://danceperformance.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2025-03-15', 1, 'Cultural Parade', 'http://culturalparade.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2025-04-10', 2, 'Book Fair', 'http://bookfair.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2025-05-20', 3, 'Comic Convention', 'http://comicconvention.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2025-06-25', 4, 'Food Truck Festival', 'http://foodtruckfestival.example.com');
INSERT INTO TB_EVENT (DATE, CITY_ID, NAME, URL) VALUES ('2025-07-10', 5, 'Beer Festival', 'http://beerfestival.example.com');

INSERT INTO TB_ROLE (AUTHORITY) VALUES ('ROLE_CLIENT');
INSERT INTO TB_ROLE (AUTHORITY) VALUES ('ROLE_ADMIN');

INSERT INTO TB_USER (EMAIL, PASSWORD) VALUES ('alice@example.com', '$2a$10$mqX7JvLOL5IpWCIiHn0SL.EDHmq8iprg2bgvt2G2G1e8W1hSbbdOS');
INSERT INTO TB_USER (EMAIL, PASSWORD) VALUES ('bob@example.com', '$2a$10$iFcVFS.Zh.vAox8nf0VOKOlfhfQ4PK8P1ZFNDSlfauWZgdmxvRsLm');

INSERT INTO TB_USER_ROLE (USER_ID, ROLE_ID) VALUES (1, 1);
INSERT INTO TB_USER_ROLE (USER_ID, ROLE_ID) VALUES (2, 2);
