package ru.otus.hw.rest.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;


@Component
public class BookMapper {

    public BookDto map (Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                AuthorDto.toDto(book.getAuthor()),
                GenreDto.toDto(book.getGenre()));
    }

}
