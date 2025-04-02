package ru.otus.hw.processors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.db.Author;
import ru.otus.hw.models.mongo.MongoAuthor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, MongoAuthor> {

    private final AuthorMapper authorMapper;

    @Override
    public MongoAuthor process(@NonNull Author author) {
        log.info("Processing author - {}", author);
        return authorMapper.map(author);
    }
}
