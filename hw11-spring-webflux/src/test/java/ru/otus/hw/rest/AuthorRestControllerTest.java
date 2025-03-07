package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.rest.dto.AuthorDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AuthorRestController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class AuthorRestControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void testAuthorList() {
        var authors = new Author[]{
                new Author("1", "Author_1"),
                new Author("2", "Author_2")};
        var authorsFlux = Flux.just(authors);

        when(authorRepository.findAll()).thenReturn(authorsFlux);

        var result = client
                .get().uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<AuthorDto> stepResult = null;
        for (Author author : authors) {
            stepResult = step.expectNext(AuthorDto.toDto(author));
            assertThat(stepResult).isNotNull();
        }
        stepResult.verifyComplete();
    }

    @Test
    void testAuthorOne() {
        Author author = new Author("3", "Author_3");

        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));

        var result = client
                .get().uri("/api/authors/".concat(author.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<AuthorDto> stepResult = step.expectNext(AuthorDto.toDto(author));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    void testAuthorSave() {
        Author author = new Author("4", "Author_4");

        when(authorRepository.save(any())).thenReturn(Mono.just(author));

        var result = client
                .post().uri("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(AuthorDto.toDto(author)), AuthorDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<AuthorDto> stepResult = step.expectNext(AuthorDto.toDto(author));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}