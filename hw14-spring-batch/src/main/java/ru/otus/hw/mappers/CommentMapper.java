package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.otus.hw.models.db.Comment;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "book", source = "book")
    MongoComment map(Comment comment, MongoBook book);
}
