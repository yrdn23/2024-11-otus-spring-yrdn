package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/authors/list")
    public List<AuthorDto> authorList() {
        var authors = authorService.findAll().stream()
                .map(AuthorDto::toDto)
                .toList();
        if (authors.isEmpty()) {
            throw new EntityNotFoundException("Authors not found!");
        }
        return authors;
    }

    @PostMapping("/authors")
    public ResponseEntity<AuthorDto> authorAdd(@Valid @RequestBody AuthorDto authorDto) {
        var savedAuthor = authorService.save(authorDto.toDomainObject());
        return ResponseEntity.ok(AuthorDto.toDto(savedAuthor));
    }
}
