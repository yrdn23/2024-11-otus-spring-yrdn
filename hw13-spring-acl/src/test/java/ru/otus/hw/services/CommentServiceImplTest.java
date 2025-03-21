package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    private static final long ID = 1L;
    private static final String NEW_TEXT = "New Text";

    @Autowired
    private CommentService commentService;

    @Test
    @Transactional(readOnly = true)
    void shouldReturnCorrectCommentById() {
        Optional<Comment> comment = commentService.findById(ID);
        assertTrue(comment.isPresent());
        assertEquals(ID, comment.get().getId());
    }

    @Test
    @Transactional(readOnly = true)
    void shouldReturnAllComments() {
        List<Comment> comments = commentService.findByBookId(ID);
        assertEquals(1, comments.size());
    }

    @Test
    @Transactional
    @Rollback
    void shouldInsertComment() {
        var id = commentService.insert(NEW_TEXT, ID).getId();

        Optional<Comment> comment = commentService.findById(id);
        assertTrue(comment.isPresent());
        assertEquals(id, comment.get().getId());
        assertEquals(NEW_TEXT, comment.get().getText());
        assertEquals(ID, comment.get().getBook().getId());
    }

    @Test
    @Transactional
    @Rollback
    void shouldUpdateComment() {
        Optional<Comment> comment = commentService.findById(ID);
        assertTrue(comment.isPresent());

        commentService.update(ID, NEW_TEXT, ID + 1);

        Optional<Comment> updateComment = commentService.findById(ID);
        assertTrue(updateComment.isPresent());
        assertEquals(ID, updateComment.get().getId());
        assertEquals(NEW_TEXT, updateComment.get().getText());
        assertEquals(ID + 1, updateComment.get().getBook().getId());
    }

    @Test
    @Transactional
    @Rollback
    void shouldDeleteComment() {
        Optional<Comment> comment = commentService.findById(ID);
        assertTrue(comment.isPresent());

        commentService.deleteById(ID);

        Optional<Comment> deletedComment = commentService.findById(ID);
        assertFalse(deletedComment.isPresent());
    }
}