package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.rest.dto.CommentDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CommentRestController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class CommentRestControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    void testCommentList() {
        var book = new Book("1", "Title_1", new Author("1", "Author_1"), new Genre("1", "Genre_1"));
        var comments = new Comment[]{
                new Comment("1", "Comment_1", book),
                new Comment("2", "Comment_2", book)};
        var commentsFlux = Flux.just(comments);

        when(commentRepository.findByBookId(anyString())).thenReturn(commentsFlux);

        var result = client
                .get().uri("/api/comments/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CommentDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<CommentDto> stepResult = null;
        for (var comment : comments) {
            stepResult = step.expectNext(CommentDto.toDto(comment));
            assertThat(stepResult).isNotNull();
        }
        stepResult.verifyComplete();
    }
}