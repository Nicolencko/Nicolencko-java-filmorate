package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFound;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.validate.FilmValidator;

import java.sql.Date;
import java.util.List;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Set;


@Qualifier("filmDbStorage")
@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFilm(Film film) {
        FilmValidator.validateFilm(film);
        log.info("Film added: {}", film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO film (name, description, release_date, duration) VALUES (?, ?, ?, ?)",
                    new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        if (film.getGenres() != null) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .toList();
            for (Genre genre : uniqueGenres) {
                jdbcTemplate.update("INSERT INTO film_category (film_id, category_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
        if (film.getMpa() != null) {
            jdbcTemplate.update("INSERT INTO film_rating (film_id, rating_id) VALUES (?, ?)",
                    film.getId(), film.getMpa().getId());
        }
    }

    @Override
    public void updateFilm(Film film) {
        FilmValidator.validateFilm(film);
        String filmExistSql = "SELECT film_id FROM film";
        List<Long> filmIds = jdbcTemplate.queryForList(filmExistSql, Long.class);
        if (!filmIds.contains(film.getId())) {
            throw new DataNotFound("Film with id " + film.getId() + " not found");
        }
        log.info("Film updated: {}", film);
        jdbcTemplate.update("UPDATE film SET name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        if (film.getGenres() != null) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .toList();
            film.setGenres(uniqueGenres);
            jdbcTemplate.update("DELETE FROM film_category WHERE film_id = ?", film.getId());
            for (Genre genre : uniqueGenres) {
                jdbcTemplate.update("INSERT INTO film_category (film_id, category_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
        if (film.getMpa() != null) {
            jdbcTemplate.update("DELETE FROM film_rating WHERE film_id = ?", film.getId());
            jdbcTemplate.update("INSERT INTO film_rating (film_id, rating_id) VALUES (?, ?)",
                    film.getId(), film.getMpa().getId());
        }

    }

    @Override
    public Film getFilmById(Long id) {
        String categorySql = "SELECT c.category_id, c.category FROM category c " +
                "JOIN film_category fc ON c.category_id = fc.category_id " +
                "WHERE fc.film_id = ?";

        String sql = "SELECT f.*, r.rating_id, r.name AS rating_name FROM film f " +
                "JOIN film_rating fr ON f.film_id = fr.film_id " +
                "JOIN rating r ON fr.rating_id = r.rating_id " +
                "WHERE f.film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Film(rs.getLong("film_id"), rs.getString("name"), rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(), rs.getInt("duration"),
                    null, new MPA(rs.getLong("rating_id"), rs.getString("rating_name"))));
            List<Genre> genres = jdbcTemplate.query(categorySql, new Object[]{id}, (rs, rowNum) -> new Genre(rs.getLong("category_id"), rs.getString("category")));
            film.setGenres(genres);
            return film;
        }
        catch (EmptyResultDataAccessException e) {
            throw new DataNotFound("Film with id " + id + " not found");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT f.*, r.rating_id, r.name AS rating_name FROM film f " +
                "JOIN film_rating fr ON f.film_id = fr.film_id " +
                "JOIN rating r ON fr.rating_id = r.rating_id";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> new Film(rs.getLong("film_id"), rs.getString("name"), rs.getString("description"),
                rs.getDate("release_date").toLocalDate(), rs.getInt("duration"),
                null, new MPA(rs.getLong("rating_id"), rs.getString("rating_name"))));
        for (Film film : films) {
            String categoryFilm = "SELECT c.category_id, c.category FROM category c " +
                    "JOIN film_category fc ON c.category_id = fc.category_id " +
                    "WHERE fc.film_id = ?";
            List<Genre> genres = jdbcTemplate.query(categoryFilm, new Object[]{film.getId()}, (rs, rowNum) -> new Genre(rs.getLong("category_id"), rs.getString("category")));
            film.setGenres(genres);
        }
        return films;
    }


    @Override
    public void increaseLike(Long filmId, Long userId) {
        log.info("Film liked: {} by user: {}", filmId, userId);
        jdbcTemplate.update("INSERT INTO film_like (film_id, user_id) VALUES (?, ?)", filmId, userId);
    }

    @Override
    public void decreaseLike(Long filmId, Long userId) {
        String userExist = "SELECT user_id FROM users";
        List<Long> userIds = jdbcTemplate.queryForList(userExist, Long.class);
        if (!userIds.contains(userId)) {
            throw new DataNotFound("User with id " + userId + " not found");
        }


        log.info("Film disliked: {} by user: {}", filmId, userId);
        jdbcTemplate.update("DELETE FROM film_like WHERE film_id = ? AND user_id = ?", filmId, userId);
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM category";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getLong("category_id"), rs.getString("category")));
    }

    @Override
    public Genre getGenreById(Long id) {
        String sql = "SELECT category_id FROM category";
        List<Long> genreIds = jdbcTemplate.queryForList(sql, Long.class);
        if (!genreIds.contains(id)) {
            return null;
        }
        String genreSql = "SELECT * FROM category WHERE category_id = ?";
        return jdbcTemplate.queryForObject(genreSql, new Object[]{id}, (rs, rowNum) -> new Genre(rs.getLong("category_id"), rs.getString("category")));

    }

    @Override
    public List<MPA> getMPAs() {
        String sql = "SELECT * FROM rating";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new MPA(rs.getLong("rating_id"), rs.getString("name")));
    }

    @Override
    public MPA getMPAById(Long id) {
        String mpaExistSql = "SELECT rating_id FROM rating";
        List<Long> mpaIds = jdbcTemplate.queryForList(mpaExistSql, Long.class);
        if (!mpaIds.contains(id)) {
            return null;
        }
        String sql = "SELECT * FROM rating WHERE rating_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new MPA(rs.getLong("rating_id"), rs.getString("name")));
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sql = "SELECT f.*, r.rating_id, r.name AS rating_name, COUNT(fl.film_id) AS likes FROM film f " +
                "JOIN film_rating fr ON f.film_id = fr.film_id " +
                "JOIN rating r ON fr.rating_id = r.rating_id " +
                "LEFT JOIN film_like fl ON f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, new Object[]{count}, (rs, rowNum) -> new Film(rs.getLong("film_id"), rs.getString("name"), rs.getString("description"),
                rs.getDate("release_date").toLocalDate(), rs.getInt("duration"),
                null, new MPA(rs.getLong("rating_id"), rs.getString("rating_name"))));
        for (Film film : films) {
            String categoryFilm = "SELECT c.category_id, c.category FROM category c " +
                    "JOIN film_category fc ON c.category_id = fc.category_id " +
                    "WHERE fc.film_id = ?";
            List<Genre> genres = jdbcTemplate.query(categoryFilm, new Object[]{film.getId()}, (rs, rowNum) -> new Genre(rs.getLong("category_id"), rs.getString("category")));
            film.setGenres(genres);
        }
        return films;
    }


}
