package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUser;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validate.UserValidator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage users;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage users) {
        this.users = users;
    }
    public void createFriends(Long userId, Long friendId) {
        User user = users.getUserById(userId);
        User friend = users.getUserById(friendId);
        UserValidator.validateUser(user);
        UserValidator.validateUser(friend);
        users.createFriends(userId, friendId);
        // users.updateUser(user);
        // users.updateUser(friend);
    }

    public void deleteFriends(Long userId, Long friendId) {
        User user = users.getUserById(userId);
        User friend = users.getUserById(friendId);
        UserValidator.validateUser(user);
        UserValidator.validateUser(friend);
        users.deleteFriends(userId, friendId);
        // users.updateUser(user);
        // users.updateUser(friend);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = users.getUserById(userId);
        User friend = users.getUserById(friendId);
        UserValidator.validateUser(user);
        UserValidator.validateUser(friend);
        List<User> friends = getFriends(userId);
        List<User> friends2 = getFriends(friendId);
        if (friends == null || friends2 == null){
            return null;
        }
        return friends.stream().filter(friends2::contains).collect(Collectors.toList());
    }

    public void createUser(User user) {
        users.createUser(user);
    }

    public void updateUser(User user) {
        try {
            users.updateUser(user);
        } catch (DataNotFound e) {
            log.error("User with id {} not found", user.getId());
            throw e;
        }
    }

    public User getUserById(Long id) {

        User user = users.getUserById(id);
        if (user == null){
            throw new DataNotFound("User with id " + id + " not found");
        }
        return user;
    }

    public List<User> getFriends(Long id) {
        User user = users.getUserById(id);
        if (user == null){
            throw new DataNotFound("User with id " + id + " not found");
        }
        return users.getFriends(id);
    }

    public List<User> getUsers() {
        return users.getUsers();
    }
}
