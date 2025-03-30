package ru.otus.hw.services;

import ru.otus.hw.models.db.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> findAll();

    Optional<Genre> findById(long id);
}
