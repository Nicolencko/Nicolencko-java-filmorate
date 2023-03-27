package ru.yandex.practicum.filmorate.validate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.validate.UserValidator;

import java.time.LocalDate;

public class UserValidatorTest {

    @Test
    public void validateEmptyEmail() {
        User user = new User(1, "login", "", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidateException.class, () -> UserValidator.validateUser(user));
    }

    @Test
    public void validateEmailWithoutAt() {
        User user = new User(1, "login", "email", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidateException.class, () -> UserValidator.validateUser(user));
    }

    @Test
    public void validateEmptyLogin() {
        User user = new User(1, "", "email@", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidateException.class, () -> UserValidator.validateUser(user));
    }

    @Test
    public void validateLoginWithSpace() {
        User user = new User(1, "login ", "email@", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidateException.class, () -> UserValidator.validateUser(user));
    }

    @Test
    public void validateFutureBirthday() {
        User user = new User(1, "login", "email@", "name", LocalDate.of(3031, 1, 1));
        assertThrows(ValidateException.class, () -> UserValidator.validateUser(user));
    }

    @Test
    public void validateUser() {
        User user = new User(1, "login", "email@", "name", LocalDate.of(2000, 1, 1));
        assertDoesNotThrow(() -> UserValidator.validateUser(user));
    }
}
