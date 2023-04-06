package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {

    public static void checkDescriptionMaxLength(String description) {
        if(description.length() > 200){
            log.info("Film description is too long");
            throw new ValidateException("Film description is too long");
        }
    }

    public static void checkDate(LocalDate date) {
        if(date.isBefore(LocalDate.of(1895, 1, 28))){
            log.info("Film release date is too early");
            throw new ValidateException("Film release date is too early");
        }
    }

    public static void validateFilm(Film film) throws ValidateException{
        checkDescriptionMaxLength(film.getDescription());
        checkDate(film.getReleaseDate());
    }
}
