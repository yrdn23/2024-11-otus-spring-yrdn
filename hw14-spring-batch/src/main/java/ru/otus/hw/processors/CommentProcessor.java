package ru.otus.hw.processors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.db.Comment;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, MongoComment> {

    private final CommentMapper commentMapper;

    private final MongoBookRepository mongoBookRepository;

    @Override
    public MongoComment process(@NonNull Comment comment) {
        log.info("Processing comment - {}", comment);
        return commentMapper.map(comment,
                mongoBookRepository.findById(String.valueOf(comment.getBook().getId())).get());
    }
}
