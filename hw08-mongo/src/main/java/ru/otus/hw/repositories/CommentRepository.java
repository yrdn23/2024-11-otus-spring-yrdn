package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
//    @Query("select c from Comment c where c.book.id = :bookId")
    List<Comment> findByBookId(String bookId);
}
