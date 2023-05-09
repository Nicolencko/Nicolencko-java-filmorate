package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void checkFutureBirthday(LocalDate birthday){
        if(birthday.isAfter(LocalDate.now())){
            log.info("Birthday is not valid");
            throw new ValidateException("Birthday is not valid");
        }
    }

    public static void checkWhitespacesLogin(String login){
        if(login.contains(" ")){
            log.info("Login contains whitespaces");
            throw new ValidateException("Login contains whitespaces");
        }
    }

    public static void validateUser(User user) throws ValidateException{
        checkWhitespacesLogin(user.getLogin());
        checkFutureBirthday(user.getBirthday());
    }



}
