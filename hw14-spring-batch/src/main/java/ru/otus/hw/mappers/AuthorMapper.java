package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.otus.hw.models.db.Author;
import ru.otus.hw.models.mongo.MongoAuthor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {
    MongoAuthor map(Author author);
}
