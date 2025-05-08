package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(long id);

    Optional<Comment> fallbackFindById(long id);

    List<Comment> findByBookId(long bookId);

    List<Comment> fallbackFindByBookId(long bookId);

    void deleteById(long id);

    void fallbackDeleteById(long id);

    Comment save(Comment comment);

    Comment fallbackSave(Comment comment);
}
