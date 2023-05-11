package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(1L);
                    assertThat(user.getName()).isEqualTo("user1name");
                    assertThat(user.getEmail()).isEqualTo("user1@email.com");
                    assertThat(user.getLogin()).isEqualTo("user1");
                });
    }

    @Test
    public void testFindUserByWrongId(){
        assertThrows(EntityNotFoundException.class, () -> userStorage.getUserById(100L));
    }

    @Test
    public void testFindFilmById(){
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(1L);
                    assertThat(film.getName()).isEqualTo("film1");
                    assertThat(film.getMpa().getName()).isEqualTo("G");
                    assertThat(film.getGenres().size()).isEqualTo(1);
                });
    }

    @Test
    public void testFindFilmByWrongId(){
        assertThrows(EntityNotFoundException.class, () -> filmStorage.getFilmById(100L));
    }

    @Test
    public void testFindGenreById(){
        Optional< Genre> genreOptional = Optional.ofNullable(filmStorage.getGenreById(1L));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre.getId()).isEqualTo(1L);
                    assertThat(genre.getName()).isEqualTo("Комедия");
                });
    }

    @Test
    public void testFindGenres(){
        assertThat(filmStorage.getGenres().size()).isEqualTo(6);
    }

    @Test
    public void testFindMPA(){
        Optional<MPA> mpaOptional = Optional.ofNullable(filmStorage.getMPAById(1L));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                    assertThat(mpa.getId()).isEqualTo(1L);
                    assertThat(mpa.getName()).isEqualTo("G");
                });
    }

    @Test
    public void testFindMPAs(){
        assertThat(filmStorage.getMPAs().size()).isEqualTo(5);
    }
}
