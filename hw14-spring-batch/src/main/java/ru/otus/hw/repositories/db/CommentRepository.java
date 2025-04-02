package ru.otus.hw.repositories.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.db.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);
}
