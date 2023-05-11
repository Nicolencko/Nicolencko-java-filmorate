package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface FilmStorage {
    void createFilm(Film film);
    void updateFilm(Film film);
    List<Film> getFilms();

    void increaseLike(Long filmId, Long userId);

    void decreaseLike(Long filmId, Long userId);

    List<Film> getTopFilms(Integer count);

    Film getFilmById(Long id);

    List<Genre> getGenres();

    Genre getGenreById(Long id);

    List<MPA> getMPAs();

    MPA getMPAById(Long id);
}
