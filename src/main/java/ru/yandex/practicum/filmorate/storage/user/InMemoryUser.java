package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryUser implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private final Set<Long> ids = new HashSet<>();
    private Long id = 0L;
    public void createUser(User user) {
        UserValidator.validateUser(user);

        if (user.getId() == null) {
            while (ids.contains(id)) {
                id++;
            }
            user.setId(id++);
        }
        log.info("User added: {}", user);
        if(user.getFriends() == null)
            user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        UserValidator.validateUser(user);
        log.info("User updated: {}", user);
        users.put(user.getId(), user);
    }

    public User getUserById(Long id) {
        return users.get(id);
    }
    public List<User> getUsers() {
        return List.copyOf(users.values());
    }
}
