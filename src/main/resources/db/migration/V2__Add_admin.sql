insert into user(id, active, password, username)
values (1, TRUE, '$2a$08$g4MBbezBx8cxYmUV3r0XseFmSsETxG/fCGIXAH4ruUQT6Cyg94Cdi', 'admin');
insert into user_role(user_id, roles)
VALUES (1, 'USER'), (1, 'ADMIN');