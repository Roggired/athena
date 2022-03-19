package ru.yofik.messenger.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yofik.messenger.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long create(User user) {
        final var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                "INSERT INTO users(login, password) " +
                        "VALUES(:" + Params.LOGIN.name() + ", :" + Params.PASSWORD.name() + ") RETURNING id",
                new MapSqlParameterSource()
                        .addValue(Params.LOGIN.name(), user.getLogin())
                        .addValue(Params.PASSWORD.name(), user.getPassword()),
                keyHolder,
                new String[]{"id"}
        );

        return keyHolder.getKeyAs(Long.class);
    }

    public Optional<User> findUserById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM users " +
                            "WHERE id=:" + Params.ID.name(),
                    Map.of(Params.ID.name(), id),
                    new UserRowMapper()
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean isUserExistsByLogin(String login) {
        return findUserByLogin(login).isPresent();
    }

    public Optional<User> findUserByLogin(String login) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM users " +
                            "WHERE login=:" + Params.LOGIN.name(),
                    Map.of(Params.LOGIN.name(), login),
                    new UserRowMapper()
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private enum Params {
        ID,
        LOGIN,
        PASSWORD
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");
            var login = rs.getString("login");
            var password = rs.getString("password");
            return new User(id, login, password);
        }
    }
}
