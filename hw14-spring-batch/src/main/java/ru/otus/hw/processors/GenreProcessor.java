package ru.otus.hw.processors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.db.Genre;
import ru.otus.hw.models.mongo.MongoGenre;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, MongoGenre> {

    private final GenreMapper genreMapper;

    @Override
    public MongoGenre process(@NonNull Genre genre) {
        log.info("Processing genre - {}", genre);
        return genreMapper.map(genre);
    }
}
