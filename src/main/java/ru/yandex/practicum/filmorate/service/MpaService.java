package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    private final FilmStorage films;

    @Autowired
    public MpaService(@Qualifier("filmDbStorage") FilmStorage films) {
        this.films = films;
    }

    public List<MPA> getMPAs() {
        return films.getMPAs();
    }

    public MPA getMPAById(Long id) {
        return Optional.ofNullable(films.getMPAById(id))
                .orElseThrow(() -> new EntityNotFoundException("MPA with id " + id + " not found"));
    }
}
