package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserValidator;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        try {
            UserValidator.validateUser(user);
        } catch (ValidateException e) {
            log.info("User is not valid");
            return null;
        }
        users.put(user.getId(), user);
        log.info("User added: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        try{
            UserValidator.validateUser(user);
        } catch (ValidateException e) {
            log.info("User is not valid");
            return null;
        }
        users.put(user.getId(), user);
        log.info("User updated: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return List.copyOf(users.values());
    }

}
