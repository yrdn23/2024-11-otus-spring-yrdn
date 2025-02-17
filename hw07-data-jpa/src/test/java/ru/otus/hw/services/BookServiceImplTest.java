package ru.otus.hw.services;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookServiceImplTest {

    private static final long ID = 1L;
    private static final String NEW_TITLE = "New Title";

    @Autowired
    private BookService bookService;

    @Test
    void shouldReturnCorrectBookById() {
        Optional<Book> book = bookService.findById(ID);
        assertTrue(book.isPresent());
        assertEquals(ID, book.get().getId());
    }

    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> books = bookService.findAll();
        assertEquals(3, books.size());
    }

    @Test
    void shouldSaveNewBook() {
        var id = bookService.insert(NEW_TITLE, ID, ID).getId();

        Optional<Book> book = bookService.findById(id);
        assertTrue(book.isPresent());
        assertEquals(id, book.get().getId());
        assertEquals(NEW_TITLE, book.get().getTitle());
        assertEquals(ID, book.get().getAuthor().getId());
        assertEquals(ID, book.get().getGenre().getId());
    }

    @Test
    void shouldUpdateBook() {
        Optional<Book> book = bookService.findById(ID);
        assertTrue(book.isPresent());

        bookService.update(ID, NEW_TITLE, ID + 1, ID + 1);

        Optional<Book> updatedBook = bookService.findById(ID);
        assertTrue(updatedBook.isPresent());
        assertEquals(ID, updatedBook.get().getId());
        assertEquals(NEW_TITLE, updatedBook.get().getTitle());
        assertEquals(ID + 1, updatedBook.get().getAuthor().getId());
        assertEquals(ID + 1, updatedBook.get().getGenre().getId());
    }

    @Test
    void shouldNotUpdateBookWithExceptionByAuthor() {
        Optional<Book> book = bookService.findById(ID);
        assertTrue(book.isPresent());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(ID, NEW_TITLE, ID + 100, ID + 1));

        assertEquals("Author with id 101 not found", exception.getMessage());
    }

    @Test
    void shouldNotUpdateBookWithExceptionByGenre() {
        Optional<Book> book = bookService.findById(ID);
        assertTrue(book.isPresent());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(ID, NEW_TITLE, ID + 1, ID + 100));

        assertEquals("Genre with id 101 not found", exception.getMessage());
    }

    @Test
    void shouldDeleteBook() {
        Optional<Book> book = bookService.findById(ID);
        assertTrue(book.isPresent());

        bookService.deleteById(ID);

        Optional<Book> deletedBook = bookService.findById(ID);
        assertFalse(deletedBook.isPresent());
    }
}