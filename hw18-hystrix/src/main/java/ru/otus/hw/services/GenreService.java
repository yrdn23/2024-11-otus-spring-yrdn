package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> findAll();

    List<Genre> fallbackFindAll();

    Optional<Genre> findById(long id);

    Optional<Genre> fallbackFindById(long id);

    Genre save(Genre genre);

    Genre fallbackSave(Genre genre);
}
