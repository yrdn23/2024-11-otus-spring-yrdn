package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.mappers.BookMapper;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookMapper bookMapper;

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> bookList() {
        return bookService.findAll().stream()
                .map(bookMapper::map)
                .toList();
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> bookSave(@Valid @RequestBody BookDto bookDto) {
        var savedBook = bookService.save(bookDto);
        return ResponseEntity.ok(bookMapper.map(savedBook));
    }

    @PostMapping("/api/books/{id}")
    public ResponseEntity<String> bookDelete(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok("OK");
    }
}
