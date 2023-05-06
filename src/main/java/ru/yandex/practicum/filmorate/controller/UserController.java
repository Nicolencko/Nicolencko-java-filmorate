package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService users = new UserService();

    @GetMapping
    public List<User> getUsers() {
        return users.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        users.createUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.updateUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") String id) {
        System.out.println(id);
        return users.getUserById(Long.parseLong(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") String id, @PathVariable("friendId") String friendId) {
        users.createFriends(users.getUserById(Long.parseLong(id)), users.getUserById(Long.parseLong(friendId)));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") String id, @PathVariable("friendId") String friendId) {
        users.deleteFriends(users.getUserById(Long.parseLong(id)), users.getUserById(Long.parseLong(friendId)));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") String id) {
        return users.getFriends(users.getUserById(Long.parseLong(id)));
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable("id") String id, @PathVariable("friendId") String friendId) {
        return users.getCommonFriends(users.getUserById(Long.parseLong(id)), users.getUserById(Long.parseLong(friendId)));
    }



}
