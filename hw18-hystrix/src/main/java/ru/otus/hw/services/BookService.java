package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findById(long id);

    Optional<Book> fallbackFindById(long id);

    List<Book> findAll();

    List<Book> fallbackFindAll();

    Book save(Book book);

    Book fallbackSave(Book book);

    Book save(BookDto bookDto);

    Book fallbackSave(BookDto bookDto);

    void deleteById(long id);

    void fallbackDeleteById(long id);
}
