package ru.otus.hw.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindAll")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> fallbackFindAll() {
        return List.of();
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallbackFindById")
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public Optional<Author> fallbackFindById(long id) {
        return Optional.of(new Author(-1L, "ERROR_AUTHOR"));
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
