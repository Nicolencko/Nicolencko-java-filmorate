package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilm;
import ru.yandex.practicum.filmorate.validate.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage films = new InMemoryFilm();

    public void likeFromUser(Film film, Long userId) {
        FilmValidator.validateFilm(film);
        log.info("Film liked: {}", film);
        films.getFilmById(film.getId()).getLikesFromUsers().add(userId);
        films.increaseLike(film);
    }

    public void dislikeFromUser(Film film, Long userId) {
        FilmValidator.validateFilm(film);
        log.info("Film disliked: {}", film);
        films.getFilmById(film.getId()).getLikesFromUsers().remove(userId);
        films.decreaseLike(film);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> films = this.films.getFilms();
        Comparator<Film> comparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getLikesFromUsers().size() - o1.getLikesFromUsers().size();
            }
        };
        return films.stream().sorted(comparator).limit(count).collect(Collectors.toList());
    }

    public void createFilm(Film film) {
        films.createFilm(film);
    }

    public void updateFilm(Film film) {
        films.updateFilm(film);
    }

    public List<Film> getFilms() {
        return films.getFilms();
    }

    public Film getFilmById(Long id) {

        Film film = films.getFilmById(id);
        if (film == null){
            throw new DataNotFound("Film with id " + id + " not found");
        }
        return film;
    }

}
