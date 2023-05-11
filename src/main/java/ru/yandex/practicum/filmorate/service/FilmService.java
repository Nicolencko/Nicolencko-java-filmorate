package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validate.FilmValidator;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage films;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage films) {
        this.films = films;
    }
    public void likeFromUser(Long filmId, Long userId) {
        Film film = films.getFilmById(filmId);
        FilmValidator.validateFilm(film);
        films.increaseLike(filmId, userId);
    }

    public void dislikeFromUser(Long filmId, Long userId) {
        Film film = films.getFilmById(filmId);
        FilmValidator.validateFilm(film);
        films.decreaseLike(filmId, userId);
    }

    public List<Film> getTopFilms(Integer count) {
        return films.getTopFilms(count);
    }

    public void createFilm(Film film) {
        films.createFilm(film);
    }

    public void updateFilm(Film film) {
        try {
            films.updateFilm(film);
        }
        catch (EntityNotFoundException e) {
            log.error("Film with id {} not found", film.getId());
            throw e;
        }
    }

    public List<Film> getFilms() {
        return films.getFilms();
    }

    public Film getFilmById(Long id) {

        Film film = films.getFilmById(id);
        if (film == null){
            throw new EntityNotFoundException("Film with id " + id + " not found");
        }
        return film;
    }






}
