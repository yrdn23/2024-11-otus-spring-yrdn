package ru.otus.hw.repositories.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.db.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
