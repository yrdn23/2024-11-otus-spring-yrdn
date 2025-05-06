package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAll();

    List<Author> fallbackFindAll();

    Optional<Author> findById(long id);

    Optional<Author> fallbackFindById(long id);

    Author save(Author author);
}
