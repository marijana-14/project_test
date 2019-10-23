INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO user (username, password, email) VALUES ('admin','$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'admin@gmail.com');
INSERT INTO user_role (user_id, role_id) VALUES ((SELECT id from user where username = 'admin'), (SELECT id from role where name = 'ROLE_ADMIN'));
