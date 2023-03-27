package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validate.FilmValidator;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        try {
            FilmValidator.validateFilm(film);
        } catch (ValidateException e) {
            log.info("Film is not valid");
            return null;
        }
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        try{
            FilmValidator.validateFilm(film);
        } catch (ValidateException e) {
            log.info("Film is not valid");
            return null;
        }
        films.put(film.getId(), film);
        log.info("Film updated: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }
}
