package ru.otus.hw.processors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.db.Book;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MongoBook> {

    private final BookMapper bookMapper;

    private final MongoAuthorRepository mongoAuthorRepository;

    private final MongoGenreRepository mongoGenreRepository;

    @Override
    public MongoBook process(@NonNull Book book) {
        log.info("Processing book - {}", book);
        return bookMapper.map(book,
                mongoAuthorRepository.findById(String.valueOf(book.getAuthor().getId())).get(),
                mongoGenreRepository.findById(String.valueOf(book.getGenre().getId())).get());
    }
}
