insert into user(id, active, password, username)
values (1, TRUE, '123', 'admin');
insert into user_role(user_id, roles)
VALUES (1, 'USER'), (1, 'ADMIN');