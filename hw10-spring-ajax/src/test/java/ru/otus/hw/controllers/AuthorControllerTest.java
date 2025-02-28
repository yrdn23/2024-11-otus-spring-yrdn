package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void testAuthorEditPageSuccessful() throws Exception {
        var authorId = 3;
        var author = new Author(authorId, "Author_3");
        when(authorService.findById(authorId)).thenReturn(Optional.of(author));

        mvc.perform(get("/authors/authorEdit").param("id", String.valueOf(authorId)))
                .andExpect(status().isOk())
                .andExpect(view().name("authorEdit"))
                .andExpect(model().attribute("author", equalTo(author)))
                .andExpect(content().string(containsString(author.getFullName())));
    }

    @Test
    void testAuthorEditPageFail() throws Exception {
        var authorId = 4;
        when(authorService.findById(authorId)).thenReturn(Optional.empty());

        mvc.perform(get("/authors/authorEdit").param("id", String.valueOf(authorId)))
                .andExpect(status().isOk())
                .andExpect(view().name("errorCustom"))
                .andExpect(content().string(containsString("Author not found")));
    }

    @Test
    void testAuthorSaveSuccessful() throws Exception {
        var authorId = 5;
        var author = new Author(authorId, "Author_5");
        when(authorService.save(author)).thenReturn(author);

        mvc.perform(post("/authors/authorEdit")
                        .param("id", String.valueOf(authorId))
                        .flashAttr("author", author))
                .andExpect(redirectedUrl("/authors/"));
    }

    @Test
    void testAuthorAddPageSuccessful() throws Exception {
        var authorId = 6;
        var author = new Author(authorId, "Author_6");
        when(authorService.save(author)).thenReturn(author);

        mvc.perform(get("/authors/authorAdd")
                        .param("id", String.valueOf(authorId))
                        .flashAttr("author", author))
                .andExpect(view().name("authorEdit"));
    }
}