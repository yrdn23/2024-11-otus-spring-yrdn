package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<GenreDto> genreList() {
        return genreService.findAll().stream()
                .map(GenreDto::toDto)
                .toList();
    }

    @PostMapping("/api/genres")
    public ResponseEntity<GenreDto> genreSave(@Valid @RequestBody GenreDto genreDto) {
        var savedGenre = genreService.save(genreDto.toDomainObject());
        return ResponseEntity.ok(GenreDto.toDto(savedGenre));
    }
}
