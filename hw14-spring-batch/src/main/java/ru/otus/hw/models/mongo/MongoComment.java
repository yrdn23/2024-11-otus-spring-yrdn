package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.db.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MongoComment {
    @Id
    private String id;

    private String text;

    @DBRef(lazy = true)
    private Book book;

    public MongoComment(String text, Book book) {
        this.text = text;
        this.book = book;
    }
}
