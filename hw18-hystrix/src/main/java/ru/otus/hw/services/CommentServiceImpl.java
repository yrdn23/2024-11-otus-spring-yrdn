package ru.otus.hw.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindById")
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Optional<Comment> fallbackFindById(long id) {
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindByBookId")
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public List<Comment> fallbackFindByBookId(long bookId) {
        return List.of();
    }

    @Transactional
    @Override
    @HystrixCommand(fallbackMethod = "fallbackDeleteById")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void fallbackDeleteById(long id) {
        log.error("FALLBACK DELETED COMMENT ID: {}", id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallbackSave")
    public Comment save(Comment comment) {
        var book = bookRepository.findById(comment.getBook().getId()).orElse(null);
        comment.setBook(book);
        return commentRepository.save(comment);
    }

    @Override
    public Comment fallbackSave(Comment comment) {
        log.error("FALLBACK SAVED COMMENT ID: {}", comment.getId());
        return new Comment(-1L, "ERROR_COMMENT", null);
    }
}
