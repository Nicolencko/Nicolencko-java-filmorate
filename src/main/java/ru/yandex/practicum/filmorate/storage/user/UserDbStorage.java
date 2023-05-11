package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUser(User user) {
        UserValidator.validateUser(user);
        log.info("User {} created", user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (login, email, name, birth_date) VALUES (?, ?, ?, ?)",
                    new String[]{"user_id"});
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void updateUser(User user) {
        UserValidator.validateUser(user);
        String userExistSql = "SELECT user_id FROM users";
        List<Long> userIds = jdbcTemplate.queryForList(userExistSql, Long.class);
        if (!userIds.contains(user.getId())) {
            throw new EntityNotFoundException("User with id " + user.getId() + " not found");
        }
        log.info("User {} updated", user);
        jdbcTemplate.update("UPDATE users SET login = ?, email = ?, name = ?, birth_date = ? WHERE user_id = ?",
                user.getLogin(), user.getEmail(), user.getName(), user.getBirthday(), user.getId());
    }

    @Override
    public User getUserById(Long id) {
        String userExistSql = "SELECT user_id FROM users";
        List<Long> userIds = jdbcTemplate.queryForList(userExistSql, Long.class);
        if (!userIds.contains(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        log.info("User with id {} found", id);
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?",
                new Object[]{id}, (rs, rowNum) -> new User(
                        rs.getLong("user_id"),
                        rs.getString("login"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate()
                )
        );
    }

    @Override
    public List<User> getUsers() {
        log.info("Get all users");
        return jdbcTemplate.query("SELECT * FROM users",
                (rs, rowNum) -> new User(
                        rs.getLong("user_id"),
                        rs.getString("login"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate()
                )
        );
    }

    @Override
    public void createFriends(Long userId, Long friendId) {
        String userExistSql = "SELECT user_id FROM users";
        List<Long> userIds = jdbcTemplate.queryForList(userExistSql, Long.class);
        if (!userIds.contains(userId)) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        if (!userIds.contains(friendId)) {
            throw new EntityNotFoundException("User with id " + friendId + " not found");
        }
        String friendsExistSql = "SELECT user_id, friend_id FROM USER_FRIEND";
        List<List<Long>> friendsIds = jdbcTemplate.query(friendsExistSql, (rs, rowNum) -> List.of(
                rs.getLong("user_id"),
                rs.getLong("friend_id")
        ));
        if (friendsIds.contains(List.of(friendId, userId))) {
            jdbcTemplate.update("INSERT INTO USER_FRIEND (user_id, friend_id, status) VALUES (?, ?, ?); " +
                            "UPDATE USER_FRIEND SET status = ? WHERE user_id = ? AND friend_id = ?",
                    userId, friendId, "friends", "friends", friendId, userId);
        } else {
            jdbcTemplate.update("INSERT INTO USER_FRIEND (user_id, friend_id, status) VALUES (?, ?, ?)",
                    userId, friendId, "pending");
        }
    }

    @Override
    public void deleteFriends(Long userId, Long friendId) {
        jdbcTemplate.update("DELETE FROM USER_FRIEND WHERE user_id = ? AND friend_id = ?",
                userId, friendId);

    }

    @Override
    public List<User> getFriends(Long userId) {
        String userExistSql = "SELECT user_id FROM users WHERE user_id = ?";
        List<Long> userIds = jdbcTemplate.queryForList(userExistSql, new Object[]{userId}, Long.class);
        if (userIds.isEmpty() || userIds.get(0) == null) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        String friendsSql = "SELECT u.user_id, u.login, u.email, u.name, u.birth_date FROM users u " +
                "JOIN USER_FRIEND uf ON u.user_id = uf.friend_id WHERE uf.user_id = ?";
        return jdbcTemplate.query(friendsSql, new Object[]{userId},
                (rs, rowNum) -> new User(
                        rs.getLong("user_id"),
                        rs.getString("login"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate()
                )
        );
    }
}
