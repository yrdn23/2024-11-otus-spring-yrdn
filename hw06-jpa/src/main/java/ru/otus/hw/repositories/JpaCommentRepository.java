package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @Override
    public Optional<Comment> findById(long id) {
        // TODO
        return Optional.empty();
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        // TODO
        return List.of();
    }

    @Override
    public Comment save(Comment comment) {
        //TODO
        return null;
    }

    @Override
    public void deleteById(long id) {
        //TODO
    }
}
