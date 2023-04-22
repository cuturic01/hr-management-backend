INSERT INTO candidates (candidate_name, date_of_birth, contact_number, email) VALUES ('John Doe', '1990-01-01', '1234567890', 'johndoe@example.com');
INSERT INTO candidates (candidate_name, date_of_birth, contact_number, email) VALUES ('Jane Smith', '1995-02-15', '9876543210', 'janesmith@example.com');
INSERT INTO candidates (candidate_name, date_of_birth, contact_number, email) VALUES ('Bob Johnson', '1988-06-30', '5555555555', 'bobjohnson@example.com');
INSERT INTO candidates (candidate_name, date_of_birth, contact_number, email) VALUES ('Sarah Lee', '1992-12-25', '9999999999', 'sarahlee@example.com');
INSERT INTO candidates (candidate_name, date_of_birth, contact_number, email) VALUES ('Mike Brown', '1998-09-05', '7777777777', 'mikebrown@example.com');

INSERT INTO skills (skill_name) VALUES ('Java');
INSERT INTO skills (skill_name) VALUES ('Python');
INSERT INTO skills (skill_name) VALUES ('SQL');
INSERT INTO skills (skill_name) VALUES ('HTML');
INSERT INTO skills (skill_name) VALUES ('CSS');

INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (1, 1);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (1, 2);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (2, 4);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (2, 5);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (3, 3);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (3, 5);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (4, 1);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (4, 4);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (5, 2);
INSERT INTO candidate_skill (candidate_id, skill_id) VALUES (5, 3);