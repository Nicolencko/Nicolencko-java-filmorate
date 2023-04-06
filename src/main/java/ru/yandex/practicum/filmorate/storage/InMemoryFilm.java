package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validate.FilmValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Slf4j
public class InMemoryFilm {
    Map<Integer, Film> films = new HashMap<>();

    public void addFilm(Film film) {
        FilmValidator.validateFilm(film);
        log.info("Film added: {}", film);
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        FilmValidator.validateFilm(film);
        log.info("Film updated: {}", film);
        films.put(film.getId(), film);
    }

    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }


}
