package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("select id, name from genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        var params = new MapSqlParameterSource()
                .addValue("id", id, Types.NUMERIC);
        var genres = jdbcTemplate.query("select id, name from genres where id = :id",
                params,
                new GenreRowMapper());

        if (genres.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(genres.get(0));
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(rs.getLong("id"), rs.getString("name"));
        }
    }
}
