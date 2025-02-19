package ru.otus.hw.repositories;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Comments")
@DataMongoTest
class CommentRepositoryTest {

    private static final String ID = "1";
    private static final String ID2 = "2";

    @Autowired
    private CommentRepository repository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getComments")
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        var actualComment = repository.findById(expectedComment.getId());
        System.out.println(actualComment.get().getBook().getTitle());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать все комментарии книги")
    @Test
    void shouldReturnAllComments() {
        var actualComments = commentRepository.findByBookId(ID2);
        var expectedComments = getComments().stream()
                .filter(comment -> ID2.equals(comment.getBook().getId()))
                .toList();
        assertThat(actualComments).isNotEmpty()
                .containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @Disabled
    void shouldSaveNewComment() {
        var expectedComment = new Comment("0", "Comment_0",
                bookRepository.findById(ID).orElseThrow());
        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> Objects.nonNull(comment.getId()))
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @Disabled
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(ID, "Comment_0",
                bookRepository.findById(ID).get());

        assertThat(repository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> Objects.nonNull(comment.getId()))
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    @Disabled
    void shouldDeleteComment() {
        assertThat(repository.findById(ID)).isPresent();
        repository.deleteById(ID);
        assertThat(repository.findById(ID)).isEmpty();
    }

    private static List<Comment> getComments() {
        var genre1 = new Genre("1", "Genre_1");
        var genre2 = new Genre("2", "Genre_2");
        var genre3 = new Genre("3", "Genre_3");
        var author1 = new Author("1", "Author_1");
        var author2 = new Author("2", "Author_2");
        var author3 = new Author("3", "Author_3");
        var book1 = new Book("1", "Book_1", author1, genre1);
        var book2 = new Book("2", "Book_2", author2, genre2);
        var book3 = new Book("3", "Book_3", author3, genre3);
        return List.of(
                new Comment("1", "Comment_1", book1),
                new Comment("2", "Comment_2", book2),
                new Comment("3", "Comment_3", book2),
                new Comment("4", "Comment_4", book2),
                new Comment("5", "Comment_5", book3),
                new Comment("6", "Comment_6", book3));
    }
}