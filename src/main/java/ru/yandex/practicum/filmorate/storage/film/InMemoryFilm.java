package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.validate.FilmValidator;

import java.util.*;

import static java.lang.Math.max;

@Slf4j
@Component
@Qualifier("inMemoryFilm")
public class InMemoryFilm implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Set<Long> ids = new HashSet<>();
    private Long id = 0L;

    public void createFilm(Film film) {
        FilmValidator.validateFilm(film);
        if(film.getId() == null){
           while (ids.contains(id)){
               id++;
           }
              film.setId(id++);
        }
        log.info("Film added: {}", film);
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        FilmValidator.validateFilm(film);
        log.info("Film updated: {}", film);
        films.put(film.getId(), film);
    }

    public Film getFilmById(Long id) {
        return films.get(id);
    }

    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }


    public void increaseLike(Long filmID, Long userId) {

    }

    public void decreaseLike(Long filmID, Long userId) {

    }

    public List<Film> getTopFilms(Integer count) {
        return null;
    }

    public List<Genre> getGenres() {
        return null;
    }

    public Genre getGenreById(Long id) {
        return null;
    }

    public List<MPA> getMPAs() {
        return null;
    }

    public MPA getMPAById(Long id) {
        return null;
    }

}
