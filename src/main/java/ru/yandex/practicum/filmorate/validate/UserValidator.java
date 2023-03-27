package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void checkEmptyEmailAndAt(String email){
        if(email == null || !email.contains("@")){
            log.info("Email is not valid");
            throw new ValidateException("Email is not valid");
        }
    }

    public static void checkEmptyAndSpaceLogin(String login){
        if(login == null || login.isEmpty() || login.contains(" ")){
            log.info("Login is not valid");
            throw new ValidateException("Login is not valid");
        }
    }

    public static void checkFutureBirthday(LocalDate birthday){
        if(birthday.isAfter(LocalDate.now())){
            log.info("Birthday is not valid");
            throw new ValidateException("Birthday is not valid");
        }
    }

    public static void validateUser(User user) throws ValidateException{
        checkEmptyEmailAndAt(user.getEmail());
        checkEmptyAndSpaceLogin(user.getLogin());
        checkFutureBirthday(user.getBirthday());
    }



}
