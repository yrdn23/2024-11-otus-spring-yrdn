package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.rest.dto.CommentDto;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentRepository commentRepository;

    @GetMapping("/api/comments/{id}")
    public Flux<CommentDto> commentList(@PathVariable String id) {
        return commentRepository.findByBookId(id).map(CommentDto::toDto);
    }
}
