package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Test
    void testGenreEditPageSuccessful() throws Exception {
        var genreId = 3;
        var genre = new Genre(genreId, "Genre_3");
        when(genreService.findById(genreId)).thenReturn(Optional.of(genre));

        mvc.perform(get("/genres/genreEdit").param("id", String.valueOf(genreId)))
                .andExpect(status().isOk())
                .andExpect(view().name("genreEdit"))
                .andExpect(model().attribute("genre", equalTo(genre)))
                .andExpect(content().string(containsString(genre.getName())));
    }

    @Test
    void testGenreEditPageFail() throws Exception {
        var genreId = 4;
        when(genreService.findById(genreId)).thenReturn(Optional.empty());

        mvc.perform(get("/genres/genreEdit").param("id", String.valueOf(genreId)))
                .andExpect(status().isOk())
                .andExpect(view().name("errorCustom"))
                .andExpect(content().string(containsString("Genre not found")));
    }

    @Test
    void testGenreAddPageSuccessful() throws Exception {
        var genreId = 6;
        var genre = new Genre(genreId, "Genre_6");
        when(genreService.save(genre)).thenReturn(genre);

        mvc.perform(get("/genres/genreAdd")
                        .param("id", String.valueOf(genreId))
                        .flashAttr("genre", genre))
                .andExpect(view().name("genreEdit"));
    }
}