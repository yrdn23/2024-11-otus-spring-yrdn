package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        var params = new MapSqlParameterSource()
                .addValue("id", id, Types.NUMERIC);
        var books = jdbcTemplate.query(
                "select b.id, b.title, b.author_id, a.full_name, b.genre_id, g.name " +
                        "from books b " +
                        "join authors a on b.author_id = a.id " +
                        "join genres g on b.genre_id = g.id " +
                        "where b.id = :id",
                params,
                new BookRowMapper());

        if (books.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(books.get(0));
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query(
                "select b.id, b.title, b.author_id, a.full_name, b.genre_id, g.name " +
                        "from books b " +
                        "join authors a on b.author_id = a.id " +
                        "join genres g on b.genre_id = g.id ",
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var params = new MapSqlParameterSource()
                .addValue("id", id, Types.NUMERIC);
        jdbcTemplate.update("delete from books where id = :id",
                params);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle(), Types.VARCHAR)
                .addValue("author_id", book.getAuthor().getId(), Types.NUMERIC)
                .addValue("genre_id", book.getGenre().getId(), Types.NUMERIC);
        jdbcTemplate.update("insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                params,
                keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource()
                .addValue("id", book.getId(), Types.NUMERIC)
                .addValue("title", book.getTitle(), Types.VARCHAR)
                .addValue("author_id", book.getAuthor().getId(), Types.NUMERIC)
                .addValue("genre_id", book.getGenre().getId(), Types.NUMERIC);

        var rowUpdated = jdbcTemplate.update("update books set title = :title, " +
                "author_id = :author_id, genre_id = :genre_id where id = :id", params);

        if (rowUpdated == 0) {
            throw new EntityNotFoundException("No rows was updated!");
        }

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong("id"),
                    rs.getString("title"),
                    new Author(rs.getLong("author_id"), rs.getString("full_name")),
                    new Genre(rs.getLong("genre_id"), rs.getString("name")));
        }
    }
}
