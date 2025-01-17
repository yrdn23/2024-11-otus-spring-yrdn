package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        var params = new MapSqlParameterSource()
                .addValue("id", id, Types.NUMERIC);
        return Optional.ofNullable(jdbcTemplate.query(
                "select b.id, b.title, b.author_id, a.full_name, bg.book_id, bg.genre_id, g.name " +
                        "from books b " +
                        "join authors a on b.author_id = a.id " +
                        "join books_genres bg on b.id = bg.book_id " +
                        "join genres g on bg.genre_id = g.id " +
                        "where b.id = :id",
                params,
                new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
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

    private List<Book> getAllBooksWithoutGenres() {
        return jdbcTemplate.query("select b.id, b.title, b.author_id, a.full_name " +
                        "from books b, authors a where b.author_id = a.id",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcTemplate.query("select book_id, genre_id from books_genres",
                new DataClassRowMapper<>(BookGenreRelation.class));
    }

    private void mergeBooksInfo(
            List<Book> booksWithoutGenres,
            List<Genre> genres,
            List<BookGenreRelation> relations
    ) {
        var genresMap = genres.stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));
        booksWithoutGenres.forEach(
                book -> book.setGenres(
                        relations.stream()
                                .filter(relation -> relation.bookId == book.getId())
                                .map(relation -> genresMap.get(relation.genreId))
                                .toList()));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle(), Types.VARCHAR)
                .addValue("author_id", book.getAuthor().getId(), Types.NUMERIC);
        jdbcTemplate.update("insert into books (title, author_id) values (:title, :author_id)", params, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource()
                .addValue("id", book.getId(), Types.NUMERIC)
                .addValue("title", book.getTitle(), Types.VARCHAR)
                .addValue("author_id", book.getAuthor().getId(), Types.NUMERIC);

        var rowUpdated = jdbcTemplate.update("update books set title = :title, author_id = :author_id " +
                "where id = :id", params);

        if (rowUpdated == 0) {
            throw new EntityNotFoundException("No rows was updated!");
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        SqlParameterSource[] batchValues = book.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("book_id", book.getId(), Types.NUMERIC)
                        .addValue("genre_id", genre.getId(), Types.NUMERIC))
                .toArray(SqlParameterSource[]::new);
        jdbcTemplate.batchUpdate("insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)",
                batchValues);
    }

    private void removeGenresRelationsFor(Book book) {
        var params = new MapSqlParameterSource()
                .addValue("book_id", book.getId());
        jdbcTemplate.update("delete from books_genres where book_id = :book_id", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong("id"),
                    rs.getString("title"),
                    new Author(rs.getLong("author_id"), rs.getString("full_name")),
                    List.of());
        }
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {
        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }

            List<Genre> genres = new ArrayList<>();
            genres.add(new Genre(rs.getLong("genre_id"), rs.getString("name")));

            var book = new Book(
                    rs.getLong("id"),
                    rs.getString("title"),
                    new Author(rs.getLong("author_id"), rs.getString("full_name")),
                    genres);

            while (rs.next()) {
                genres.add(new Genre(rs.getLong("genre_id"), rs.getString("name")));
            }

            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
