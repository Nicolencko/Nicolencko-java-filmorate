package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUser;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validate.UserValidator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage users = new InMemoryUser();
    public void createFriends(User user, User friend) {
        UserValidator.validateUser(user);
        UserValidator.validateUser(friend);
        log.info("User {} added friend {}", user, friend);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        users.updateUser(user);
        users.updateUser(friend);
    }

    public void deleteFriends(User user, User friend) {
        UserValidator.validateUser(user);
        UserValidator.validateUser(friend);
        log.info("User {} deleted friend {}", user, friend);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        users.updateUser(user);
        users.updateUser(friend);
    }

    public List<User> getCommonFriends(User user, User friend) {
        UserValidator.validateUser(user);
        UserValidator.validateUser(friend);
        List<User> friends = getFriends(user);
        List<User> friends2 = getFriends(friend);
        if (friends == null || friends2 == null){
            return null;
        }

        return user.getFriends().stream().filter(friend.getFriends()::contains).map(users::getUserById).collect(Collectors.toList());
    }

    public void createUser(User user) {
        users.createUser(user);
    }

    public void updateUser(User user) {
        users.updateUser(user);
    }

    public User getUserById(Long id) {

        User user = users.getUserById(id);
        if (user == null){
            throw new DataNotFound("User with id " + id + " not found");
        }
        return user;
    }

    public List<User> getFriends(User user) {
        Set<Long> friends = user.getFriends();
        if (friends == null){
            return null;
        }
        return user.getFriends().stream().map(users::getUserById).collect(Collectors.toList());
    }

    public List<User> getUsers() {
        return users.getUsers();
    }
}
