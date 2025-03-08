package ru.otus.hw.rest.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.BookDto;

@Slf4j
@Component
public class BookMapper {

    public BookDto map(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getAuthor().getFullName(),
                book.getGenre().getId(),
                book.getGenre().getName());
    }

}
