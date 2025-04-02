package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.db.Author;
import ru.otus.hw.models.db.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MongoBook {
    @Id
    private String id;

    private String title;

    private Author author;

    private Genre genre;

    public MongoBook(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
