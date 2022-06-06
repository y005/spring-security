DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS groups_permission CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE permissions
(
    id bigint NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups
(
    id bigint NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups_permission
(
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    permission_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT const1 UNIQUE (group_id, permission_id),
    CONSTRAINT const2 FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT const3 FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE users
(
    id bigint NOT NULL,
    login_id varchar(20) NOT NULL,
    passwd varchar(20) NOT NULL,
    group_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT const4 UNIQUE (login_id),
    CONSTRAINT const5 FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);