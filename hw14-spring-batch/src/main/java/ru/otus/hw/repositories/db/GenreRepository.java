package ru.otus.hw.repositories.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.db.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
