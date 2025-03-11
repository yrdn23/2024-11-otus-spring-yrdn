package ru.otus.hw.rest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.mappers.BookMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {BookMapper.class, BookRestController.class})
@TestPropertySource(properties = {"mongock.enabled=false"})
class BookRestControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Test
    void testBookList() {
        var books = new Book[]{
                new Book("1", "BookTitle_1", new Author("1", "Author_1"), new Genre("1", "Genre_1")),
                new Book("2", "BookTitle_2", new Author("2", "Author_2"), new Genre("2", "Genre_2"))};
        var booksFlux = Flux.just(books);

        when(bookRepository.findAll()).thenReturn(booksFlux);

        var result = client
                .get().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        for (var book : books) {
            stepResult = step.expectNext(bookMapper.map(book));
            assertThat(stepResult).isNotNull();
        }
        stepResult.verifyComplete();
    }

    @Test
    void testBookOne() {
        var book = new Book("3", "BookTitle_3", new Author("3", "Author_3"), new Genre("3", "Genre_3"));

        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));

        var result = client
                .get().uri("/api/books/".concat(book.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(bookMapper.map(book));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    @Disabled
    void testBookAdd() {
        var author = new Author("5", "Author_5");
        var genre = new Genre("5", "Genre_5");
        var book = new Book("5", "BookTitle_5", author, genre);
        var bookDto = new BookDto(null, "BookTitle_5", "5", "Author_5", "5", "Genre_5");

        when(authorRepository.findById(anyString())).thenReturn(Mono.just(author));
        when(genreRepository.findById(anyString())).thenReturn(Mono.just(genre));

        var result = client
                .post().uri("/api/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookDto), BookDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(bookDto);
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    @Disabled
    void testBookSave() {
        var author = new Author("4", "Author_4");
        var genre = new Genre("4", "Genre_4");
        var book = new Book("4", "BookTitle_4", author, genre);
        var bookDto = new BookDto("4", "BookTitle_4", "4", "Author_4", "4", "Genre_4");

        when(authorRepository.findById(anyString())).thenReturn(Mono.just(author));
        when(genreRepository.findById(anyString())).thenReturn(Mono.just(genre));
        when(bookRepository.findById(anyString())).thenReturn(Mono.just(book));

        var result = client
                .post().uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookDto), BookDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(bookDto);
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    void testBookDelete() {

        when(bookRepository.deleteById(anyString())).thenReturn(Mono.empty());

        client
                .post().uri("/api/books/7")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}