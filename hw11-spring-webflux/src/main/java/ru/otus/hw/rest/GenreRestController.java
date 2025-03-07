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
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.GenreDto;

@RestController
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/genres")
    public Flux<GenreDto> genreList() {
        return genreRepository.findAll().map(GenreDto::toDto);
    }

    @GetMapping("/api/genres/{id}")
    public Mono<GenreDto> genreOne(@PathVariable String id) {
        return genreRepository.findById(id).map(GenreDto::toDto);
    }

    @PostMapping("/api/genres")
    public Mono<GenreDto> genreSave(@Valid @RequestBody GenreDto genreDto) {
        return genreRepository.save(genreDto.toDomainObject()).map(GenreDto::toDto);
    }
}
