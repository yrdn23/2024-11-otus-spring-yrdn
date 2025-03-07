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
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.GenreDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = GenreRestController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class GenreRestControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void testGenreList() {
        var genres = new Genre[]{
                new Genre("1", "Genre_1"),
                new Genre("2", "Genre_2")};
        var genresFlux = Flux.just(genres);

        when(genreRepository.findAll()).thenReturn(genresFlux);

        var result = client
                .get().uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GenreDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<GenreDto> stepResult = null;
        for (var genre : genres) {
            stepResult = step.expectNext(GenreDto.toDto(genre));
            assertThat(stepResult).isNotNull();
        }
        stepResult.verifyComplete();
    }

    @Test
    void testGenreOne() {
        var genre = new Genre("3", "Genre_3");

        when(genreRepository.findById(genre.getId())).thenReturn(Mono.just(genre));

        var result = client
                .get().uri("/api/genres/".concat(genre.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GenreDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<GenreDto> stepResult = step.expectNext(GenreDto.toDto(genre));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @Test
    void testGenreSave() {
        var genre = new Genre("4", "Genre_4");

        when(genreRepository.save(any())).thenReturn(Mono.just(genre));

        var result = client
                .post().uri("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(GenreDto.toDto(genre)), GenreDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GenreDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<GenreDto> stepResult = step.expectNext(GenreDto.toDto(genre));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}