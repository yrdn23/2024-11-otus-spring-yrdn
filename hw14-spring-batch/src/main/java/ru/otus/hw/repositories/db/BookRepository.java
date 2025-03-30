package ru.otus.hw.repositories.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.db.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
