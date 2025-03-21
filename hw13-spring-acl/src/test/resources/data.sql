insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 2), ('Comment_4', 3);

insert into users(name, password, roles)
values ('user','$2y$10$A1GBGnymxG8ex8lNJ0UhVOkH5PaeowC3FF1STsrkyvgo2mDRafyXm', 'ROLE_USER'),
       ('admin','$2y$10$Rdsf/qZNsbwRIu8DaMRv8uHJiIBLvJah/4xN2Y/1pLfTlIDgMhlau', 'ROLE_ADMIN');