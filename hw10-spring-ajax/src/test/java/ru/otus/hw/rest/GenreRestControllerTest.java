package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreRestController.class)
class GenreRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @Test
    void testGenreList() throws Exception {
        List<Genre> genres = List.of(
                new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2"));
        when(genreService.findAll()).thenReturn(genres);

        mvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genres)));
    }

    @Test
    void testAuthorSave() throws Exception {
        Genre genre = new Genre(3, "Genre_3");
        when(genreService.save(genre)).thenReturn(genre);

        mvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(GenreDto.toDto(genre))))
                .andExpect(content().json(mapper.writeValueAsString(genre)));
    }
}