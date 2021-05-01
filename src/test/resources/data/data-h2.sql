-- INSERT INTO roles (`id`, `name`) VALUES(1, 'ROLE_MODERATOR');
-- INSERT INTO roles (`id`, `name`) VALUES(2, 'ROLE_ADMIN');
-- INSERT INTO roles (`id`, `name`) VALUES(3, 'ROLE_USER');

INSERT INTO users (`id`, `email`, `password`, `username`, `enabled`) VALUES(RANDOM_UUID(), 'userTest@example.com', '$2a$10$i5hyMMtwUZXJveIt/qeiKOJjSb412MaOXb5g1lMvMcK9vMMR10MH2', 'userTest', 1);

set @user_uuid = (select id from users where username like 'userTest');

INSERT INTO user_roles (`user_id`, `role_id`) VALUES(@user_uuid, 2);
