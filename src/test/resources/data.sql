INSERT INTO category (category)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO rating (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO users(login, email, name, birth_date)
VALUES ('user1',
        'user1@email.com',
        'user1name',
        '1990-01-01');

INSERT INTO film(NAME, DESCRIPTION, DURATION, RELEASE_DATE)
VALUES ('film1', 'film1 description', 100, '2019-01-01');

INSERT INTO film_rating(film_id, rating_id)
VALUES (1, 1);

INSERT INTO film_category(film_id, category_id)
VALUES (1, 1);