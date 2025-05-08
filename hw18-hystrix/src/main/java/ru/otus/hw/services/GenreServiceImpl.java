package ru.otus.hw.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindAll")
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> fallbackFindAll() {
        return List.of();
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindById")
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }

    @Override
    public Optional<Genre> fallbackFindById(long id) {
        return Optional.of(new Genre(-1L, "ERROR_GENRE"));
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallbackSave")
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Genre fallbackSave(Genre genre) {
        return new Genre(-1L, "ERROR_GENRE");
    }
}
