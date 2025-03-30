package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.db.Comment;

@Component
public class CommentConverter {

    public String commentToString(Comment comment) {
        return "book: %s, id: %d, text: %s".formatted(
                comment.getBook().getTitle(),
                comment.getId(),
                comment.getText());
    }
}
