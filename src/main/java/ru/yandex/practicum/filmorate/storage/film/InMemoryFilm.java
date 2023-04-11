package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validate.FilmValidator;

import java.util.*;

import static java.lang.Math.max;

@Slf4j
@Component
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
        if(film.getLikesFromUsers() == null)
            film.setLikesFromUsers(new HashSet<>());
        if(film.getLikes() == null)
            film.setLikes(0);
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


    public void increaseLike(Film film){
        film.setLikes(film.getLikes() + 1);
    }

    public void decreaseLike(Film film){
        film.setLikes(max(film.getLikes() - 1, 0));
    }

}
