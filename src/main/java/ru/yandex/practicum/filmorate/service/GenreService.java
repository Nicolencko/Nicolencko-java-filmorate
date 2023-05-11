package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class GenreService {
    private final FilmStorage films;

    @Autowired
    public GenreService(@Qualifier("filmDbStorage") FilmStorage films) {
        this.films = films;
    }

    public List<Genre> getGenres() {
        return films.getGenres();
    }

    public Genre getGenreById(Long id) {
        Genre genre = films.getGenreById(id);
        if (genre == null){
            throw new EntityNotFoundException("Genre with id " + id + " not found");
        }
        return genre;
    }
}
