package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void createFilm(Film film);
    void updateFilm(Film film);
    List<Film> getFilms();

    void increaseLike(Film film);

    void decreaseLike(Film film);

    Film getFilmById(Long id);
}
