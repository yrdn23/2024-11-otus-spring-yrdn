package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/books/list")
    public List<BookDto> bookList() {
        return bookService.findAll().stream()
                .map(BookDto::toDto)
                .toList();
    }

    @PostMapping("/books")
    public ResponseEntity<BookDto> bookAdd(@Valid @RequestBody BookDto bookDto) {
        var savedBook = bookService.save(bookDto);
        return ResponseEntity.ok(BookDto.toDto(savedBook));
    }
}
