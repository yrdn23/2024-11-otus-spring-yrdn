package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.db.Book;
import ru.otus.hw.repositories.db.AuthorRepository;
import ru.otus.hw.repositories.db.BookRepository;
import ru.otus.hw.repositories.db.GenreRepository;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Books")
@DataJpaTest
class JpaBookRepositoryTest {

    private static final long ID = 1L;

    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getBookIds")
    void shouldReturnCorrectBookById(long expectedBookId) {
        var actualBook = repository.findById(expectedBookId);
        var expectedBook = em.find(Book.class, expectedBookId);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).isNotEmpty()
                .containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500",
                authorRepository.findById(ID).orElse(null),
                genreRepository.findById(ID).orElse(null),
                null);
        var returnedBook = repository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500",
                authorRepository.findById(ID + 1).orElse(null),
                genreRepository.findById(ID + 1).orElse(null),
                null);

        assertThat(repository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        assertThat(repository.findById(ID)).isPresent();
        repository.deleteById(ID);
        assertThat(repository.findById(ID)).isEmpty();
    }

    private List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> em.find(Book.class, id))
                .toList();
    }

    private static List<Long> getBookIds() {
        return IntStream.range(1, 4).boxed()
                .map(Long::valueOf)
                .toList();
    }
}