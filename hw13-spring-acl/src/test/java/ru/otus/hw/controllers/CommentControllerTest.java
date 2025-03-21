package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCommentListPageSuccessful() throws Exception {
        var book = new Book(1, "BookTitle_1", new Author(), new Genre(), null);
        var comments = List.of(
                new Comment(1, "Comment_1", book),
                new Comment(2, "Comment_2", book));
        when(commentService.findByBookId(1)).thenReturn(comments);
        when(bookService.findById(1)).thenReturn(Optional.of(book));

        mvc.perform(get("/comments/?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("commentList"))
                .andExpect(model().attribute("comments", hasSize(comments.size())))
                .andExpect(model().attribute("comments", equalTo(comments)))
                .andExpect(model().attribute("book", equalTo(book)))
                .andExpect(content().string(containsString(comments.get(0).getText())));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCommentEditPageSuccessful() throws Exception {
        var id = 3;
        var book = new Book(id, "Title_1", null, null, null);
        var comment = new Comment(id, "Comment_3", book);
        when(commentService.findById(id)).thenReturn(Optional.of(comment));

        mvc.perform(get("/comments/commentEdit").param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(view().name("commentEdit"))
                .andExpect(model().attribute("comment", equalTo(comment)))
                .andExpect(content().string(containsString(comment.getText())));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCommentEditPageFail() throws Exception {
        var id = 4;
        when(commentService.findById(id)).thenReturn(Optional.empty());

        mvc.perform(get("/comments/commentEdit").param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(view().name("errorCustom"))
                .andExpect(content().string(containsString("Comment not found")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCommentSaveSuccessful() throws Exception {
        var id = 5;
        var author = new Author(id, "TestAuthor_5");
        var genre = new Genre(id, "TestGenre_5");
        var book = new Book(id, "TestBook_5", author, genre, null);
        var comment = new Comment(id, "Comment_1", book);
        when(bookService.findById(id)).thenReturn(Optional.of(book));
        when(commentService.save(comment)).thenReturn(comment);

        mvc.perform(post("/comments/commentEdit?book_id=%s".formatted(id))
                        .with(csrf())
                        .param("id", String.valueOf(id))
                        .flashAttr("book", book))
                .andExpect(redirectedUrl("/comments/?id=%s".formatted(id)));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCommentAddPageSuccessful() throws Exception {
        var id = 1;
        var book = new Book(id, "Book_1", null, null, null);
        when(bookService.findById(id)).thenReturn(Optional.of(book));

        mvc.perform(get("/comments/commentAdd?book_id=%s".formatted(id))
                        .param("id", String.valueOf(id))
                        .flashAttr("book", book))
                .andExpect(view().name("commentEdit"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testCommentDeleteSuccessful() throws Exception {
        var id = 1;
        mvc.perform(post("/comments/commentDelete?book_id=%s".formatted(id))
                        .with(csrf())
                        .param("id", String.valueOf(id)))
                .andExpect(redirectedUrl("/comments/?id=%s".formatted(id)));
    }

    @Test
    void testCommentListPageUnauthorized() throws Exception {
        var author = new Author(9, "Author_9");
        var genre = new Genre(9, "Genre_9");
        var book = new Book(9, "Book_9", author, genre, null);
        var comments = List.of(
                new Comment(8, "Comment_8", book),
                new Comment(9, "Comment_9", book));
        when(commentService.findByBookId(9)).thenReturn(comments);

        mvc.perform(get("/comments/id=9"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCommentEditPageUnauthorized() throws Exception {
        var author = new Author(9, "Author_9");
        var genre = new Genre(9, "Genre_9");
        var book = new Book(9, "Book_9", author, genre, null);
        var comment = new Comment(9, "Comment_9", book);
        when(commentService.findById(9)).thenReturn(Optional.of(comment));

        mvc.perform(get("/comment/9"))
                .andExpect(status().isUnauthorized());
    }

}