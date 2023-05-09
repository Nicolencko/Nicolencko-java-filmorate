package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping
public class FilmController {

    private final FilmService films;

    @Autowired
    public FilmController(FilmService films) {
        this.films = films;
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        films.createFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.updateFilm(film);
        return film;
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return films.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable("id") String id) {
        return films.getGenreById(Long.parseLong(id));
    }

    @GetMapping("/mpa")
    public List<MPA> getMPAs() {
        return films.getMPAs();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPA(@PathVariable("id") String id) {
        return films.getMPAById(Long.parseLong(id));
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return films.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") String id) {
        return films.getFilmById(Long.parseLong(id));
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") String id, @PathVariable("userId") String userId) {
        films.likeFromUser(Long.parseLong(id), Long.parseLong(userId));
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable("id") String id, @PathVariable("userId") String userId) {
        films.dislikeFromUser(Long.parseLong(id), Long.parseLong(userId));
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count){
        return films.getTopFilms(count);
    }


}
