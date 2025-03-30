package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.db.Comment;
import ru.otus.hw.repositories.db.BookRepository;
import ru.otus.hw.repositories.db.CommentRepository;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Comments")
@DataJpaTest
class JpaCommentRepositoryTest {

    private static final long ID = 1L;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getCommentIds")
    void shouldReturnCorrectCommentById(long expectedCommentId) {
        var actualComment = repository.findById(expectedCommentId);
        var expectedComment = em.find(Comment.class, expectedCommentId);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать все комментарии книги")
    @Test
    void shouldReturnAllComments() {
        var book = bookRepository.findById(ID);
        assertThat(book.get().getComments()).size().isPositive();
        var actualComments = book.get().getComments();
        assertThat(actualComments).isNotEmpty()
                .containsExactlyElementsOf(List.of(repository.findById(ID).get()));
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(0, "Comment_0",
                bookRepository.findById(ID).get());
        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(ID, "Comment_0",
                bookRepository.findById(ID).get());

        assertThat(repository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(repository.findById(ID)).isPresent();
        repository.deleteById(ID);
        assertThat(repository.findById(ID)).isEmpty();
    }

    private static List<Long> getCommentIds() {
        return IntStream.range(1, 4).boxed()
                .map(Long::valueOf)
                .toList();
    }
}