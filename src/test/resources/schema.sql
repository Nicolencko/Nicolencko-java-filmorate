CREATE TABLE IF NOT EXISTS film
(
    film_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR,
    duration     INT          NOT NULL,
    release_date DATE
);

CREATE TABLE IF NOT EXISTS rating
(
    rating_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_rating
(
    film_id   BIGINT NOT NULL,
    rating_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, rating_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id),
    FOREIGN KEY (rating_id) REFERENCES rating (rating_id)
);

CREATE TABLE IF NOT EXISTS category
(
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category    VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_category
(
    film_id     BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, category_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id),
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    login      VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    name       VARCHAR(255),
    birth_date DATE
);

CREATE TABLE IF NOT EXISTS film_like
(
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES film (film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS user_friend
(
    user_id   BIGINT       NOT NULL,
    friend_id BIGINT       NOT NULL,
    status    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friend_id) REFERENCES users (user_id)
);
