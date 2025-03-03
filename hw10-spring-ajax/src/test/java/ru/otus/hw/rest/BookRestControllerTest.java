package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @Test
    void testBookList() throws Exception {
        List<Book> books = List.of(
                new Book(1, "BookTitle_1", new Author(1, "Author_1"), new Genre(1, "Genre_1"), null),
                new Book(2, "BookTitle_2", new Author(2, "Author_2"), new Genre(2, "Genre_2"), null));
        when(bookService.findAll()).thenReturn(books);

        List<BookDto> booksDto = books.stream().map(BookDto::toDto).toList();

        mvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(booksDto)));
    }

    @Test
    void testBookAdd() throws Exception {
        Book book = new Book(3, "BookTitle_3", new Author(3, "Author_3"), new Genre(3, "Genre_3"), null);

        when(bookService.save(BookDto.toDto(book))).thenReturn(book);

        mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookDto.toDto(book))))
                .andExpect(content().json(mapper.writeValueAsString(BookDto.toDto(book))));
    }
}