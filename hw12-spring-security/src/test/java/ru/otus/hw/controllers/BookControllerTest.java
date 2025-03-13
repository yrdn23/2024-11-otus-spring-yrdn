package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testBookListPageSuccessful() throws Exception {
        var books = List.of(
                new Book(1, "TestBook_1", new Author(), new Genre(), null),
                new Book(2, "TestBook_2", new Author(), new Genre(), null));
        when(bookService.findAll()).thenReturn(books);

        mvc.perform(get("/books/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attribute("books", hasSize(books.size())))
                .andExpect(model().attribute("books", equalTo(books)))
                .andExpect(content().string(containsString(books.get(0).getTitle())));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testBookEditPageSuccessful() throws Exception {
        var bookId = 3;
        var book = new Book(bookId, "TestBook_3", new Author(), new Genre(), null);
        when(bookService.findById(bookId)).thenReturn(Optional.of(book));
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

        mvc.perform(get("/books/bookEdit").param("id", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEdit"))
                .andExpect(model().attribute("book", equalTo(book)))
                .andExpect(content().string(containsString(book.getTitle())));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testBookEditPageFail() throws Exception {
        var bookId = 4;
        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        mvc.perform(get("/books/bookEdit").param("id", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andExpect(view().name("errorCustom"))
                .andExpect(content().string(containsString("Book not found")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testBookSaveSuccessful() throws Exception {
        var bookId = 5;
        var author = new Author(bookId, "TestAuthor_5");
        var genre = new Genre(bookId, "TestGenre_5");
        var book = new Book(bookId, "TestBook_5", author, genre, null);
        when(authorService.findById(bookId)).thenReturn(Optional.of(new Author()));
        when(genreService.findById(bookId)).thenReturn(Optional.of(new Genre()));
        when(bookService.save(book)).thenReturn(book);

        mvc.perform(post("/books/bookEdit")
                        .param("id", String.valueOf(bookId))
                        .flashAttr("book", book))
                .andExpect(redirectedUrl("/books/"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testBookAddPageSuccessful() throws Exception {
        var bookId = 6;
        var book = new Book(bookId, "TestBook_6", new Author(), new Genre(), null);
        when(bookService.save(book)).thenReturn(book);

        mvc.perform(get("/books/bookAdd")
                        .param("id", String.valueOf(bookId))
                        .flashAttr("book", book))
                .andExpect(view().name("bookEdit"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testBookDeleteSuccessful() throws Exception {
        var bookId = 7;
        mvc.perform(post("/books/%s".formatted(bookId)))
                .andExpect(redirectedUrl("/books/"));
    }

    @Test
    void testBookListPageUnauthorized() throws Exception {
        var author = new Author(9, "Author_9");
        var genre = new Genre(9, "Genre_9");
        var books = List.of(
                new Book(8, "Book_8", author, genre, null),
                new Book(9, "Book_9", author, genre, null));
        when(bookService.findAll()).thenReturn(books);

        mvc.perform(get("/books/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testBookEditPageUnauthorized() throws Exception {
        var author = new Author(9, "Author_9");
        var genre = new Genre(9, "Genre_9");
        var book = new Book(8, "Book_8", author, genre, null);
        when(bookService.findById(8)).thenReturn(Optional.of(book));

        mvc.perform(get("/books/9"))
                .andExpect(status().isUnauthorized());
    }
}