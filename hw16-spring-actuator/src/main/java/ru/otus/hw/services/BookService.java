package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(long id);

    List<Book> findAll();

    Long count();

    Book insert(String title, long authorId, long genreId);

    Book update(long id, String title, long authorId, long genreId);

    Book save(Book book);

    Book save(BookDto bookDto);

    void deleteById(long id);
}
