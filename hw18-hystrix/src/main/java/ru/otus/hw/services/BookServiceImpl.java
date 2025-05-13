package ru.otus.hw.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.BookDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindById")
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> fallbackFindById(long id) {
        return Optional.of(new Book(-1L, "ERROR_BOOK", null, null, null));
    }

    @Transactional(readOnly = true)
    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindAll")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> fallbackFindAll() {
        return List.of();
    }

    @Transactional
    @Override
    @HystrixCommand(fallbackMethod = "fallbackSave")
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book fallbackSave(Book book) {
        return new Book(-1L, "ERROR_BOOK", null, null, null);
    }

    @Transactional
    @Override
    @HystrixCommand(fallbackMethod = "fallbackSave")
    public Book save(BookDto bookDto) {
        return save(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor().getId(), bookDto.getGenre().getId());
    }

    @Override
    public Book fallbackSave(BookDto bookDto) {
        return new Book(-1L, "ERROR_BOOK", null, null, null);
    }

    @Transactional
    @Override
    @HystrixCommand(fallbackMethod = "fallbackDeleteById")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void fallbackDeleteById(long id) {
        log.error("FALLBACK DELETED BOOK ID: {}", id);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre, null);
        return bookRepository.save(book);
    }
}
