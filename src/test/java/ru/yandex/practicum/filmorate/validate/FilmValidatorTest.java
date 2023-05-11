package ru.yandex.practicum.filmorate.validate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.validate.FilmValidator;



import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidatorTest {
    @Test
    public void validateEmptyName(){
        Film film = new Film(1L, "", "description", LocalDate.now(), 100);
        assertThrows(ValidateException.class, () -> FilmValidator.validateFilm(film));
    }

    @Test
    public void validateCorrectFilm(){
        Film film = new Film(1L, "name", "description", LocalDate.now(), 100);
        FilmValidator.validateFilm(film);
    }

    @Test
    public void validateDescriptionMaxLength() {
        Film film = new Film(1L, "name", "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription", LocalDate.now(), 100);
        assertThrows(ValidateException.class, () -> FilmValidator.validateFilm(film));
    }

    @Test
    public void validateDate() {
        Film film = new Film(1L, "name", "description", LocalDate.of(1894, 1, 1), 100);
        assertThrows(ValidateException.class, () -> FilmValidator.validateFilm(film));
    }

    @Test
    public void validateNegativeDuration() {
        Film film = new Film(1L, "name", "description", LocalDate.now(), -100);
        assertThrows(ValidateException.class, () -> FilmValidator.validateFilm(film));
    }

}
