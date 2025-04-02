package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.otus.hw.models.db.Genre;
import ru.otus.hw.models.mongo.MongoGenre;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMapper {
    MongoGenre map(Genre genre);
}
