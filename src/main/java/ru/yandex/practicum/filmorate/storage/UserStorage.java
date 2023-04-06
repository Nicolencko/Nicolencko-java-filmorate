package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserStorage {
    Map<Integer, User> users = new HashMap<>();

    public void addUser(User user) {
        UserValidator.validateUser(user);
        log.info("User added: {}", user);
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        UserValidator.validateUser(user);
        log.info("User updated: {}", user);
        users.put(user.getId(), user);
    }

    public List<User> getUsers() {
        return List.copyOf(users.values());
    }
}
