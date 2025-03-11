package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.mappers.BookMapper;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @GetMapping("/api/books")
    public Flux<BookDto> bookList() {
        return bookRepository.findAll().map(bookMapper::map);
    }

    @GetMapping("/api/books/{id}")
    public Mono<BookDto> bookOne(@PathVariable String id) {
        return bookRepository.findById(id).map(bookMapper::map);
    }

    @PostMapping("/api/books/add")
    public Mono<BookDto> bookAdd(@Valid @RequestBody BookDto bookDto) {
        return Mono.just(new Book(null, bookDto.getTitle(), null, null))
                .flatMap(book -> authorRepository.findById(bookDto.getAuthorId())
                        .flatMap(author -> genreRepository.findById(bookDto.getGenreId())
                                .flatMap(genre -> bookRepository.save(book.setAuthor(author).setGenre(genre)))
                        )
                )
                .map(bookMapper::map);
    }

    @PostMapping("/api/books")
    public Mono<BookDto> bookSave(@Valid @RequestBody BookDto bookDto) {
        return bookRepository.findById(bookDto.getId())
                .flatMap(book -> authorRepository.findById(bookDto.getAuthorId())
                        .flatMap(author -> genreRepository.findById(bookDto.getGenreId())
                                .flatMap(genre -> bookRepository.save(book.setAuthor(author).setGenre(genre)))
                        )
                )
                .map(bookMapper::map);
    }

    @PostMapping("/api/books/{id}")
    public Mono<Void> bookDelete(@PathVariable("id") String id) {
        return bookRepository.deleteById(id);
    }
}
