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
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.rest.dto.AuthorDto;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorRepository authorRepository;

    @GetMapping("/api/authors")
    public Flux<AuthorDto> authorList() {
        return authorRepository.findAll().map(AuthorDto::toDto);
    }

    @GetMapping("/api/authors/{id}")
    public Mono<AuthorDto> authorOne(@PathVariable String id) {
        return authorRepository.findById(id).map(AuthorDto::toDto);
    }

    @PostMapping("/api/authors")
    public Mono<AuthorDto> authorSave(@Valid @RequestBody AuthorDto authorDto) {
        return authorRepository.save(authorDto.toDomainObject()).map(AuthorDto::toDto);
    }
}
