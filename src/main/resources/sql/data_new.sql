INSERT INTO permissions(id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT INTO groups(id, name)
VALUES (1, 'USER_GROUP'),
       (2, 'ADMIN_GROUP');

INSERT INTO groups_permission(id, group_id, permission_id)
VALUES (1, 1, 1),
       (2, 2, 1),
       (3, 2, 2);

INSERT INTO users(id, login_id, passwd, group_id)
VALUES (1, 'user', '{noop}user123', 1),
       (2, 'admin', '{noop}admin123', 2);

