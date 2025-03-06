package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "001")
public class InitChangeLog {

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Author author1;

    private Author author2;

    private Author author3;

    private Book book1;

    private Book book2;

    private Book book3;

    @ChangeSet(order = "000", id = "dropDB", author = "user", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "addGenres", author = "user", runAlways = true)
    public void addGenres(GenreRepository genreRepository) {
        genre1 = genreRepository.save(new Genre("1", "Genre_1")).block();
        genre2 = genreRepository.save(new Genre("2", "Genre_2")).block();
        genre3 = genreRepository.save(new Genre("3", "Genre_3")).block();
    }

    @ChangeSet(order = "001", id = "addAuthors", author = "user", runAlways = true)
    public void addAuthors(AuthorRepository authorRepository) {
        author1 = authorRepository.save(new Author("1", "Author_1")).block();
        author2 = authorRepository.save(new Author("2", "Author_2")).block();
        author3 = authorRepository.save(new Author("3", "Author_3")).block();
    }

    @ChangeSet(order = "001", id = "addBooks", author = "user", runAlways = true)
    public void addBooks(BookRepository bookRepository) {
        book1 = bookRepository.save(new Book("1", "Book_1", author1, genre1)).block();
        book2 = bookRepository.save(new Book("2", "Book_2", author2, genre2)).block();
        book3 = bookRepository.save(new Book("3", "Book_3", author3, genre3)).block();
    }

    @ChangeSet(order = "001", id = "addComments", author = "user", runAlways = true)
    public void addComments(CommentRepository commentRepository) {
        commentRepository.save(new Comment("1", "Comment_1", book1)).block();
        commentRepository.save(new Comment("2", "Comment_2", book2)).block();
        commentRepository.save(new Comment("3", "Comment_3", book2)).block();
        commentRepository.save(new Comment("4", "Comment_4", book2)).block();
        commentRepository.save(new Comment("5", "Comment_5", book3)).block();
        commentRepository.save(new Comment("6", "Comment_6", book3)).block();
    }
}
