package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilm;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    InMemoryFilm films = new InMemoryFilm();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        films.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.updateFilm(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return films.getFilms();
    }
}
