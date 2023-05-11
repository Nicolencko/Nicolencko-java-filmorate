package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;


public interface UserStorage {
    void createUser(User user);
    void updateUser(User user);

    User getUserById(Long id);
    List<User> getUsers();

    void createFriends(Long userId, Long friendId);

    void deleteFriends(Long userId, Long friendId);

    List<User> getFriends(Long userId);
}
